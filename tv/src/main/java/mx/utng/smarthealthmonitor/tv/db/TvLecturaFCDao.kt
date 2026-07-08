// tv/.../tv/db/TvLecturaFCDao.kt
package mx.utng.smarthealthmonitor.tv.db

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface TvLecturaFCDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertar(lectura: TvLecturaFC)

    @Query("SELECT * FROM tv_lecturas_fc ORDER BY timestamp DESC LIMIT 20")
    fun obtenerUltimas(): Flow<List<TvLecturaFC>>
}
