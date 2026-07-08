// tv/.../tv/db/TvLecturaFC.kt
package mx.utng.smarthealthmonitor.tv.db

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * Entidad Room local del módulo TV.
 * Mismos campos que LecturaFC del módulo app.
 */
@Entity(tableName = "tv_lecturas_fc")
data class TvLecturaFC(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val valorBpm: Int,
    val timestamp: Long = System.currentTimeMillis(),
    val hora: String = java.text.SimpleDateFormat(
        "HH:mm", java.util.Locale.getDefault()
    ).format(java.util.Date()),
    val esNormal: Boolean = valorBpm in 60..100
)
