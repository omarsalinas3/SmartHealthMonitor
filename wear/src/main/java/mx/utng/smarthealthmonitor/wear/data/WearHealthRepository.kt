// wear/.../data/WearHealthRepository.kt
package mx.utng.smarthealthmonitor.wear.data

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

/** Lectura de FC local en el reloj (sin Room, en memoria). */
data class WearLecturaFC(
    val id: Int,
    val valorBpm: Int,
    val hora: String,
    val esNormal: Boolean = valorBpm in 60..100
)

/**
 * Repositorio singleton del módulo Wear OS.
 * Mantiene FC, pasos e historial en memoria.
 */
object WearHealthRepository {

    private val _fcFlow = MutableStateFlow(0)
    val fcFlow: StateFlow<Int> = _fcFlow.asStateFlow()

    private val _pasosFlow = MutableStateFlow(0)
    val pasosFlow: StateFlow<Int> = _pasosFlow.asStateFlow()

    // Historial en memoria (últimas 50 lecturas, orden descendente)
    private val _historialFlow = MutableStateFlow<List<WearLecturaFC>>(emptyList())
    val historialFlow: StateFlow<List<WearLecturaFC>> = _historialFlow.asStateFlow()

    private var contadorId = 0

    fun actualizarFC(bpm: Int) {
        _fcFlow.value = bpm
        // Agregar al historial local
        val hora = SimpleDateFormat("HH:mm", Locale.getDefault()).format(Date())
        val nueva = WearLecturaFC(id = ++contadorId, valorBpm = bpm, hora = hora)
        _historialFlow.value = (listOf(nueva) + _historialFlow.value).take(50)
    }

    fun actualizarPasos(pasos: Int) {
        _pasosFlow.value = pasos
    }
}
