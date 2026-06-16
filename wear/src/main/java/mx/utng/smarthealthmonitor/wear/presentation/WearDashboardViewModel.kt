// presentation/WearDashboardViewModel.kt
package mx.utng.smarthealthmonitor.wear.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import mx.utng.smarthealthmonitor.wear.data.WearHealthRepository
import mx.utng.smarthealthmonitor.wear.data.WearLecturaFC

class WearDashboardViewModel : ViewModel() {

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
}
