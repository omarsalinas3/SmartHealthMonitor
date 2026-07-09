// tv/.../tv/TvViewModel.kt
package mx.utng.smarthealthmonitor.tv

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.*

/**
 * ViewModel del módulo Android TV.
 * Expone [state] con lecturas mapeadas a TvLecturaDisplay.
 * Se instancia con [TvViewModelFactory] que recibe Context
 * para garantizar que TvRepository esté inicializado.
 */
class TvViewModel(context: Context) : ViewModel() {

    init {
        // Garantizar init aunque TvApplication no lo haya llamado
        TvRepository.init(context)
    }

    // FC actual del wearable
    val fc: StateFlow<Int> = TvRepository.fcFlow
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), 0)

    /** Estado UI unificado: lecturas mapeadas + FC actual */
    val state: StateFlow<TvUiState> = combine(
        TvRepository.obtenerHistorial(),
        TvRepository.fcFlow
    ) { lecturas, fc ->
        TvUiState(
            lecturas = lecturas.map { l ->
                TvLecturaDisplay(
                    id     = l.id,
                    bpm    = l.valorBpm,
                    estado = if (l.esNormal) "Normal" else "⚠ Alta",
                    hora   = l.hora
                )
            },
            fcActual = fc
        )
    }.stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(5_000),
        TvUiState()
    )

    // Mantener historial separado para compatibilidad con TvDetailScreen (conversión Room)
    val historial = TvRepository.obtenerHistorial()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), emptyList())
}

/** Factory que pasa Context al ViewModel para inicializar Room */
class TvViewModelFactory(private val context: Context) : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(TvViewModel::class.java)) {
            return TvViewModel(context.applicationContext) as T
        }
        throw IllegalArgumentException("Unknown ViewModel: ${modelClass.name}")
    }
}
