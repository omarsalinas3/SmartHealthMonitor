// tv/.../MainFragment.kt
package mx.utng.smarthealthmonitor.tv

import android.os.Bundle
import android.view.View
import androidx.core.content.ContextCompat
import androidx.leanback.app.BrowseSupportFragment
import androidx.leanback.widget.*

/**
 * MainFragment — pantalla principal del módulo Android TV.
 * Usa BrowseSupportFragment (Leanback) para mostrar el menú lateral
 * con filas de contenido navegables con el D-pad.
 * Ejercicio 01 — S11.
 */
class MainFragment : BrowseSupportFragment() {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupUI()
        loadRows()
    }

    private fun setupUI() {
        title          = "SmartHealth TV"
        headersState   = HEADERS_ENABLED
        isHeadersTransitionOnBackEnabled = true

        // Color del encabezado lateral (brand color)
        brandColor = ContextCompat.getColor(
            requireContext(), android.R.color.holo_blue_dark
        )
        searchAffordanceColor = ContextCompat.getColor(
            requireContext(), android.R.color.holo_red_light
        )
    }

    private fun loadRows() {
        val rowsAdapter = ArrayObjectAdapter(ListRowPresenter())

        // Fila 1 — Monitoreo en Tiempo Real
        val monitoreoHeader = HeaderItem(0, "Monitoreo")
        val monitoreoAdapter = ArrayObjectAdapter(CardPresenter())
        monitoreoAdapter.add(TvCard("❤ Frecuencia Cardíaca", "Datos en tiempo real del wearable"))
        monitoreoAdapter.add(TvCard("👟 Pasos del Día", "Conteo de pasos desde el reloj"))
        monitoreoAdapter.add(TvCard("🩸 SpO2", "Saturación de oxígeno"))
        rowsAdapter.add(ListRow(monitoreoHeader, monitoreoAdapter))

        // Fila 2 — Historial
        val historialHeader = HeaderItem(1, "Historial FC")
        val historialAdapter = ArrayObjectAdapter(CardPresenter())
        historialAdapter.add(TvCard("📊 Historial Hoy", "Lecturas del día actual"))
        historialAdapter.add(TvCard("📅 Historial Semanal", "Últimos 7 días"))
        rowsAdapter.add(ListRow(historialHeader, historialAdapter))

        // Fila 3 — Alertas
        val alertasHeader = HeaderItem(2, "Alertas")
        val alertasAdapter = ArrayObjectAdapter(CardPresenter())
        alertasAdapter.add(TvCard("⚠ Alertas Activas", "Notificaciones de FC alta"))
        alertasAdapter.add(TvCard("⚙ Configuración", "Umbrales y contactos"))
        rowsAdapter.add(ListRow(alertasHeader, alertasAdapter))

        adapter = rowsAdapter
    }
}

/** Modelo de datos para las tarjetas del TV */
data class TvCard(val titulo: String, val descripcion: String)
