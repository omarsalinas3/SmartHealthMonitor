// tv/.../tv/TvRepository.kt
package mx.utng.smarthealthmonitor.tv

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
import mx.utng.smarthealthmonitor.tv.db.TvDatabase
import mx.utng.smarthealthmonitor.tv.db.TvLecturaFC

/**
 * Repositorio singleton del módulo TV.
 * Patrón idéntico a SmartHealthRepository del módulo app,
 * pero independiente — sin depender de :app.
 * Ejercicio 03 — S11.
 */
object TvRepository {

    private val scope = CoroutineScope(Dispatchers.IO + SupervisorJob())

    // FC actual (actualizado desde el emulador / wearable)
    private val _fcFlow = MutableStateFlow(0)
    val fcFlow: StateFlow<Int> = _fcFlow.asStateFlow()

    private var dao: mx.utng.smarthealthmonitor.tv.db.TvLecturaFCDao? = null

    fun init(context: Context) {
        dao = TvDatabase.getDatabase(context).tvLecturaDao()
        // Insertar datos mock al iniciar (simula historial real)
        scope.launch {
            TvMockData.historialFC.forEach { lectura ->
                dao?.insertar(
                    TvLecturaFC(
                        valorBpm  = lectura.valorBpm,
                        hora      = lectura.hora,
                        esNormal  = lectura.esNormal
                    )
                )
            }
        }
    }

    fun actualizarFC(bpm: Int) {
        _fcFlow.value = bpm
        scope.launch {
            dao?.insertar(TvLecturaFC(valorBpm = bpm))
        }
    }

    /** Flow del historial desde Room — actualización reactiva */
    fun obtenerHistorial(): Flow<List<TvLecturaFC>> =
        dao?.obtenerUltimas() ?: emptyFlow()
}
