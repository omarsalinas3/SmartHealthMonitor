// tv/.../tv/TvData.kt
package mx.utng.smarthealthmonitor.tv

/**
 * Modelo de datos local del módulo TV.
 * Replica la estructura de LecturaFC del módulo app
 * sin depender de Room ni de :app (módulos application
 * no pueden depender entre sí).
 */
data class LecturaFC(
    val id: Int,
    val valorBpm: Int,
    val hora: String,
    val esNormal: Boolean = valorBpm in 60..100
)

/** Mock data — en Ej.03 vendrán de Room local */
object TvMockData {

    val historialFC = listOf(
        LecturaFC(1, 78,  "11:00"),
        LecturaFC(2, 82,  "10:30"),
        LecturaFC(3, 76,  "10:00"),
        LecturaFC(4, 110, "09:30", esNormal = false), // ⚠ FC alta
        LecturaFC(5, 71,  "09:00"),
        LecturaFC(6, 95,  "08:30", esNormal = false), // ⚠ FC alta
        LecturaFC(7, 74,  "08:00")
    )

    // ⭐ Reto adicional — alertas recientes
    val alertasRecientes = listOf(
        LecturaFC(101, 118, "11:45", esNormal = false),
        LecturaFC(102, 105, "10:15", esNormal = false),
        LecturaFC(103, 112, "08:50", esNormal = false)
    )

    val fcActual   = 88
    val pasosActual = 4250
}
