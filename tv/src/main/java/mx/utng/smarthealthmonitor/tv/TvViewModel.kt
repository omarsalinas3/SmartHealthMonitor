package mx.utng.smarthealthmonitor.tv
import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import mx.utng.smarthealthmonitor.tv.data.TvNeonRepository
import mx.utng.smarthealthmonitor.tv.mqtt.MqttTvSubscriber
 
class TvViewModel(private val context: Context) : ViewModel() {
    private val neonRepo = TvNeonRepository()
    private val _state   = MutableStateFlow(TvUiState())
    val state: StateFlow<TvUiState> = _state.asStateFlow()
    
    private val mqttSubscriber = MqttTvSubscriber(context) { tvMsg ->
        _state.update { it.copy(fcActual = tvMsg.bpm) }
    }
 
    init { 
        cargarDatos()
        mqttSubscriber.connect()
    }
 
    fun cargarDatos() {
        viewModelScope.launch {
            _state.update { it.copy(isLoading=true) }
            try {
                val lecturas = neonRepo.obtenerHistorialCompleto(50)
                val stats    = neonRepo.obtenerEstadisticas()
                val alertas  = neonRepo.obtenerAlertas()
                _state.update { it.copy(
                    lecturas     = lecturas.map { dto -> dto.toTvLecturaDisplay() },
                    estadisticas = stats.map { dto -> dto.toTvLecturaDisplay() },
                    alertas      = alertas.map { dto -> dto.toTvLecturaDisplay() },
                    isLoading    = false
                )}
            } catch (e: Exception) {
                _state.update { it.copy(error=e.message, isLoading=false) }
            }
        }
    }
    
    fun refresh() = cargarDatos()
    
    override fun onCleared() {
        super.onCleared()
        mqttSubscriber.disconnect()
    }
}
 
/** Factory que pasa Context al ViewModel */
class TvViewModelFactory(private val context: Context) : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(TvViewModel::class.java)) {
            return TvViewModel(context.applicationContext) as T
        }
        throw IllegalArgumentException("Unknown ViewModel: ${modelClass.name}")
    }
}
