package mx.utng.smarthealthmonitor.data.repository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext
import mx.utng.smarthealthmonitor.data.db.LecturaFCDao
import mx.utng.smarthealthmonitor.data.db.LecturaFC
import mx.utng.smarthealthmonitor.data.remote.NeonClient
import mx.utng.smarthealthmonitor.data.remote.NeonRequest

class SyncRepository(
    private val dao: LecturaFCDao
) {

    fun observarHistorial(): Flow<List<LecturaFC>> = dao.obtenerTodas()

    suspend fun insertarLectura(lectura: LecturaFC) {
        val id = dao.insertar(lectura)
        try {
            sincronizarHaciaNeon(lectura)
            dao.marcarSincronizado(id)
        } catch (e: Exception) {
            android.util.Log.w("SYNC", "Pendiente de sync: ${e.message}")
        }
    }

    private suspend fun sincronizarHaciaNeon(lectura: LecturaFC) =
        withContext(Dispatchers.IO) {
            NeonClient.api.executeQuery(
                auth    = NeonClient.AUTH_HEADER,
                request = NeonRequest(
                    query  = """INSERT INTO lecturas_fc (bpm, estado, dispositivo, hora)
                               VALUES ($1, $2, $3, $4) RETURNING *""".trimIndent(),
                    params = listOf(lectura.bpm, lectura.estado, lectura.dispositivo, lectura.hora)
                )
            )
        }

    /** Descarga de Neon y borra los locales primero para mostrar solo datos reales */
    suspend fun sincronizarDesdeNeon(limite: Int = 50) = withContext(Dispatchers.IO) {
        try {
            val response = NeonClient.api.executeQuery(
                auth    = NeonClient.AUTH_HEADER,
                request = NeonRequest(
                    query  = "SELECT id,bpm,estado,dispositivo,hora FROM lecturas_fc ORDER BY id DESC LIMIT \$1",
                    params = listOf(limite)
                )
            )
            android.util.Log.d("SYNC", "📥 rows recibidos: ${response.rows.size}")
            if (response.rows.isNotEmpty()) {
                // Borrar locales primero para que no aparezcan mezclados
                dao.borrarTodas()
                response.rows.forEach { dto ->
                    dao.upsert(LecturaFC(
                        id           = dto.id,
                        bpm          = dto.bpm,
                        estado       = dto.estado,
                        dispositivo  = dto.dispositivo,
                        hora         = dto.hora,
                        sincronizado = true
                    ))
                }
            }
        } catch (e: Exception) {
            android.util.Log.e("SYNC", "❌ Error descargando de Neon: ${e.message}", e)
        }
    }

    suspend fun enviarPendientes() = withContext(Dispatchers.IO) {
        val pendientes = dao.obtenerNoSincronizados()
        pendientes.forEach { lectura ->
            try {
                sincronizarHaciaNeon(lectura)
                dao.marcarSincronizado(lectura.id.toLong())
            } catch (e: Exception) {
                android.util.Log.w("SYNC", "Aún sin internet: ${e.message}")
            }
        }
    }
}
