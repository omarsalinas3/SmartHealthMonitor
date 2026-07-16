// presentation/WearDashboardViewModel.kt
package mx.utng.smarthealthmonitor.wear.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import mx.utng.smarthealthmonitor.wear.data.WearHealthRepository
import mx.utng.smarthealthmonitor.wear.data.WearLecturaFC
import mx.utng.smarthealthmonitor.wear.mqtt.MqttWearPublisher
import android.app.Application
import androidx.lifecycle.AndroidViewModel

class WearDashboardViewModel(application: Application) : AndroidViewModel(application) {

    private val mqttPublisher = MqttWearPublisher(application)

    // FC del sensor del reloj en tiempo real
    val fc: StateFlow<Int> = WearHealthRepository.fcFlow
        .map { if (it == 0) 72 else it }  // valor por defecto
        .stateIn(
            scope   = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = 72
        )

    // ⭐ Reto adicional: pasos del día
    val pasos: StateFlow<Int> = WearHealthRepository.pasosFlow
        .stateIn(
            scope        = viewModelScope,
            started      = SharingStarted.WhileSubscribed(5_000),
            initialValue = 0
        )

    // Historial de FC desde WearHealthRepository (en memoria)
    val historial: StateFlow<List<WearLecturaFC>> = WearHealthRepository.historialFlow
        .stateIn(
            scope        = viewModelScope,
            started      = SharingStarted.WhileSubscribed(5_000),
            initialValue = emptyList()
        )

    init {
        mqttPublisher.connect()
        viewModelScope.launch {
            fc.collect { bpm ->
                val estado = when { 
                    bpm < 60 -> "FC Baja"
                    bpm > 100 -> "FC Alta"
                    else -> "Normal" 
                }
                mqttPublisher.publishFC(bpm, estado)
            }
        }
    }
 
    override fun onCleared() {
        super.onCleared()
        mqttPublisher.disconnect()
    }
}
