// wear/.../watchface/SmartHealthRenderer.kt
package mx.utng.smarthealthmonitor.wear.watchface

import android.content.Context
import android.graphics.*
import android.view.SurfaceHolder
import androidx.wear.watchface.*
import androidx.wear.watchface.style.CurrentUserStyleRepository
import mx.utng.smarthealthmonitor.wear.data.WearHealthRepository
import java.time.ZonedDateTime

/**
 * Renderer del WatchFace SmartHealth.
 * Dibuja hora digital + FC en tiempo real.
 * ⭐ Reto adicional: Modo AOD (DrawMode.AMBIENT) muestra solo la hora.
 */
class SmartHealthRenderer(
    private val context: Context,
    surfaceHolder: SurfaceHolder,
    watchState: WatchState,
    complicationSlotsManager: ComplicationSlotsManager,
    currentUserStyleRepository: CurrentUserStyleRepository,
    interactiveDrawModeUpdateDelayMillis: Long
) : Renderer.CanvasRenderer2<Renderer.SharedAssets>(
    surfaceHolder,
    currentUserStyleRepository,
    watchState,
    CanvasType.HARDWARE,
    interactiveDrawModeUpdateDelayMillis
) {
    // ─── Pinturas modo interactivo
    private val paintHora = Paint().apply {
        color      = Color.WHITE
        textSize   = 72f
        isAntiAlias = true
        typeface   = Typeface.DEFAULT_BOLD
    }
    private val paintFC = Paint().apply {
        color      = Color.RED
        textSize   = 30f
        isAntiAlias = true
    }
    private val paintSub = Paint().apply {
        color      = Color.GRAY
        textSize   = 22f
        isAntiAlias = true
    }

    // ⭐ Reto adicional: pinturas modo AOD (antiAlias = false para ahorro de batería)
    private val paintHoraAOD = Paint().apply {
        color       = Color.WHITE
        textSize    = 72f
        isAntiAlias = false
        typeface    = Typeface.DEFAULT_BOLD
    }

    override suspend fun createSharedAssets(): Renderer.SharedAssets =
        object : Renderer.SharedAssets { override fun onDestroy() {} }

    override fun render(
        canvas: Canvas,
        bounds: Rect,
        zonedDateTime: ZonedDateTime,
        sharedAssets: Renderer.SharedAssets
    ) {
        // Fondo negro — ahorra batería en modo AOD
        canvas.drawColor(Color.BLACK)

        val cx = bounds.exactCenterX()
        val cy = bounds.exactCenterY()

        val hora = String.format("%02d:%02d", zonedDateTime.hour, zonedDateTime.minute)

        // ⭐ Reto adicional: modo AOD — solo hora, sin FC, sin antiAlias
        val isAmbient = renderParameters.drawMode == DrawMode.AMBIENT

        if (isAmbient) {
            val tw = paintHoraAOD.measureText(hora)
            canvas.drawText(hora, cx - tw / 2f, cy + 25f, paintHoraAOD)
        } else {
            // Hora digital centrada
            val tw = paintHora.measureText(hora)
            canvas.drawText(hora, cx - tw / 2f, cy - 10f, paintHora)

            // Segundos (pequeño debajo)
            val seg = String.format("%02d", zonedDateTime.second)
            canvas.drawText(seg, cx - 18f, cy + 30f, paintSub)

            // FC desde WearHealthRepository (en memoria, actualizado por sensor)
            val fc = WearHealthRepository.fcFlow.value
            if (fc > 0) {
                val fcStr = "❤ $fc bpm"
                val fcW = paintFC.measureText(fcStr)
                canvas.drawText(fcStr, cx - fcW / 2f, cy + 70f, paintFC)
            }
        }
    }

    override fun renderHighlightLayer(
        canvas: Canvas,
        bounds: Rect,
        zonedDateTime: ZonedDateTime,
        sharedAssets: Renderer.SharedAssets
    ) {
        canvas.drawColor(renderParameters.highlightLayer!!.backgroundTint)
    }
}
