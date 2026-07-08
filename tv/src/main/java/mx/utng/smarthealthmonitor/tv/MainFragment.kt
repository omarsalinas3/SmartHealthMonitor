// tv/.../tv/MainFragment.kt
package mx.utng.smarthealthmonitor.tv

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.leanback.app.BrowseSupportFragment
import androidx.leanback.widget.*
import kotlinx.coroutines.launch
import mx.utng.smarthealthmonitor.tv.db.TvLecturaFC

class MainFragment : BrowseSupportFragment() {

    private val viewModel: TvViewModel by viewModels()
    private lateinit var histAdapter: ArrayObjectAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Configuración del BrowseFragment
        title        = "SmartHealth TV"
        headersState = HEADERS_ENABLED
        isHeadersTransitionOnBackEnabled = true

        // Color de la marca en el sidebar
        brandColor = resources.getColor(R.color.sh_primary, null)

        cargarFilas()
        observarDatos()
    }

    /** Observa el historial de Room y lo refleja en la fila reactivamente */
    private fun observarDatos() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.historial.collect { lecturas ->
                    histAdapter.clear()
                    // Convertir TvLecturaFC → LecturaFC local para FCCardPresenter
                    lecturas.forEach { tvLectura ->
                        histAdapter.add(
                            LecturaFC(
                                id       = tvLectura.id,
                                valorBpm = tvLectura.valorBpm,
                                hora     = tvLectura.hora,
                                esNormal = tvLectura.esNormal
                            )
                        )
                    }
                }
            }
        }
    }

    private fun cargarFilas() {
        val rowsAdapter = ArrayObjectAdapter(ListRowPresenter())

        // ── Fila 1: Estado actual ──────────────────────────────────────
        val estadoAdapter = ArrayObjectAdapter(FCCardPresenter())
        estadoAdapter.add(LecturaFC(id = 0, valorBpm = TvMockData.fcActual,    hora = "Ahora"))
        estadoAdapter.add(LecturaFC(id = 1, valorBpm = TvMockData.pasosActual, hora = "Pasos"))
        rowsAdapter.add(ListRow(HeaderItem("Estado actual"), estadoAdapter))

        // ── Fila 2: Historial de FC (reactivo desde Room) ─────────────
        histAdapter = ArrayObjectAdapter(FCCardPresenter())
        rowsAdapter.add(ListRow(HeaderItem("Historial FC"), histAdapter))

        // ── Fila 3: Alertas recientes ⭐ Reto adicional ───────────────
        val alertasAdapter = ArrayObjectAdapter(FCCardPresenter())
        TvMockData.alertasRecientes.forEach { alertasAdapter.add(it) }
        rowsAdapter.add(ListRow(HeaderItem("Alertas recientes"), alertasAdapter))

        this.adapter = rowsAdapter
    }
}
