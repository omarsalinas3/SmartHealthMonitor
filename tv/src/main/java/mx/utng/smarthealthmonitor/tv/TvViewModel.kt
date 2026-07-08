// tv/.../tv/TvViewModel.kt
package mx.utng.smarthealthmonitor.tv

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.*
import mx.utng.smarthealthmonitor.tv.db.TvLecturaFC

/**
 * ViewModel del módulo Android TV.
 * Observa TvRepository (local) y expone StateFlows al Fragment.
 * Ejercicio 03 — S11.
 */
class TvViewModel : ViewModel() {

    // FC actual del wearable (o 0 si no hay dato)
    val fc: StateFlow<Int> = TvRepository.fcFlow
        .stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5_000),
            0
        )

    // Historial de lecturas desde Room DAO
    val historial: StateFlow<List<TvLecturaFC>> =
        TvRepository.obtenerHistorial()
            .stateIn(
                viewModelScope,
                SharingStarted.WhileSubscribed(5_000),
                emptyList()
            )
}
