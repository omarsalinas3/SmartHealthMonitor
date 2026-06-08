// data/SmartHealthRepository.kt
package mx.utng.smarthealthmonitor.data

import android.content.Context
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.launch
import mx.utng.smarthealthmonitor.data.db.LecturaFC
import mx.utng.smarthealthmonitor.data.db.LecturaFCDao
import mx.utng.smarthealthmonitor.data.db.SmartHealthDB

/**
 * Repositorio singleton que centraliza los datos de salud.
 * El WearListenerService escribe aquí.
 * El ViewModel lee de aquí.
 * Ejercicio 02: persiste historial en Room DB.
 */
object SmartHealthRepository {

    private val scope = CoroutineScope(Dispatchers.IO + SupervisorJob())

    // FC actual del wearable (bpm)
    private val _fcFlow = MutableStateFlow(0)
    val fcFlow: StateFlow<Int> = _fcFlow.asStateFlow()

    // Pasos del día actual
    private val _pasosFlow = MutableStateFlow(0)
    val pasosFlow: StateFlow<Int> = _pasosFlow.asStateFlow()

    // SpO2
    private val _spO2Flow = MutableStateFlow(0)
    val spO2Flow: StateFlow<Int> = _spO2Flow.asStateFlow()

    // DAO de Room (se inicializa en Application.onCreate)
    private var dao: LecturaFCDao? = null

    fun init(context: Context) {
        dao = SmartHealthDB.getDatabase(context).lecturaDao()
    }

    fun actualizarFC(bpm: Int) {
        _fcFlow.value = bpm
        // Persistir en Room automáticamente (Ejercicio 02)
        scope.launch {
            dao?.insertar(LecturaFC(valorBpm = bpm))
        }
    }

    fun actualizarPasos(pasos: Int) {
        _pasosFlow.value = pasos
    }

    fun actualizarSpO2(spo2: Int) {
        _spO2Flow.value = spo2
    }

    // Flow del historial desde Room — actualización reactiva
    fun obtenerHistorial(): Flow<List<LecturaFC>> =
        dao?.obtenerUltimas() ?: emptyFlow()

    // Reto adicional: limpiar lecturas con más de 7 días
    fun limpiarHistorialAntiguo() {
        scope.launch {
            val sieteDiasAtras = System.currentTimeMillis() - (7L * 24 * 60 * 60 * 1000)
            dao?.limpiarViejos(sieteDiasAtras)
        }
    }
}
