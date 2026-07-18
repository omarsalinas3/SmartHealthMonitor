package mx.utng.smarthealthmonitor.data.models
 
data class LecturaFC(
    val id: Int,
    val bpm: Int,
    val hora: String,
    val estado: String = if (bpm in 60..100) "Normal" else if (bpm < 60) "FC Baja" else "FC Alta"
)
 
// Datos de prueba para desarrollo (mock data)
object MockData {
    val historialFC = listOf(
        LecturaFC(1, 78, "11:00"),
        LecturaFC(2, 82, "10:30"),
        LecturaFC(3, 76, "10:00"),
        LecturaFC(4, 105, "09:30", "FC Alta"),  // fuera de rango
        LecturaFC(5, 71, "09:00"),
        LecturaFC(6, 80, "08:30"),
        LecturaFC(7, 74, "08:00")
    )
    var fcActual = 78
    var pasosActual = 4250
}
