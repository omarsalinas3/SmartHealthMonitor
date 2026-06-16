// wear/.../data/WearHealthRepository.kt
package mx.utng.smarthealthmonitor.wear.data

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

/**
 * Repositorio singleton del módulo Wear OS.
 * El reloj tiene su propio estado local de FC y pasos
 * que se actualiza desde WearMainActivity (SensorManager).
 * Equivalente al SmartHealthRepository del módulo app.
 */
object WearHealthRepository {

    private val _fcFlow = MutableStateFlow(0)
    val fcFlow: StateFlow<Int> = _fcFlow.asStateFlow()

    private val _pasosFlow = MutableStateFlow(0)
    val pasosFlow: StateFlow<Int> = _pasosFlow.asStateFlow()

    fun actualizarFC(bpm: Int) {
        _fcFlow.value = bpm
    }

    fun actualizarPasos(pasos: Int) {
        _pasosFlow.value = pasos
    }
}
