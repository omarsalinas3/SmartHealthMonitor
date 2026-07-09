// tv/.../tv/TvUiState.kt
package mx.utng.smarthealthmonitor.tv

/**
 * Modelo de presentación para una lectura de FC en la UI de TV.
 * Mapea TvLecturaFC (Room) a campos legibles por el Composable.
 */
data class TvLecturaDisplay(
    val id     : Int,
    val bpm    : Int,
    val estado : String,   // "Normal" | "⚠ Alta"
    val hora   : String
)

/** Estado UI del módulo TV */
data class TvUiState(
    val lecturas : List<TvLecturaDisplay> = emptyList(),
    val fcActual : Int                   = 0
)
