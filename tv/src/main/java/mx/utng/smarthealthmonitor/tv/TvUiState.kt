package mx.utng.smarthealthmonitor.tv
import mx.utng.smarthealthmonitor.tv.data.remote.LecturaFcDto
 
/** Modelo de presentación para una lectura en TV */
data class TvLecturaDisplay(
    val id          : Int,
    val bpm         : Int,
    val estado      : String,
    val dispositivo : String,
    val hora        : String,
    val fecha       : String = ""
)
 
/** Estado UI del módulo TV */
data class TvUiState(
    val lecturas     : List<TvLecturaDisplay> = emptyList(),
    val estadisticas : List<TvLecturaDisplay> = emptyList(),
    val alertas      : List<TvLecturaDisplay> = emptyList(),
    val isLoading    : Boolean                = false,
    val error        : String?                = null,
    val fcActual     : Int                    = 0
)
 
fun LecturaFcDto.toTvLecturaDisplay() = TvLecturaDisplay(
    id          = id,
    bpm         = bpm,
    estado      = estado,
    dispositivo = dispositivo,
    hora        = hora,
    fecha       = fecha ?: ""
)
