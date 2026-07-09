// tv/.../tv/ui/TvCatalogScreen.kt
package mx.utng.smarthealthmonitor.tv.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.tv.material3.MaterialTheme
import androidx.tv.material3.Surface
import androidx.tv.material3.Text
import mx.utng.smarthealthmonitor.tv.TvMockData
import mx.utng.smarthealthmonitor.tv.TvViewModel
import mx.utng.smarthealthmonitor.tv.TvViewModelFactory
import mx.utng.smarthealthmonitor.tv.db.TvLecturaFC

/**
 * Pantalla catalogo del modulo TV.
 * Muestra filas de cards de FC navegables con D-pad.
 * Al presionar OK sobre una card -> onCardClick(lectura.id)
 */
@Composable
fun TvCatalogScreen(
    onCardClick: (Int) -> Unit,
    viewModel: TvViewModel = viewModel(
        factory = TvViewModelFactory(LocalContext.current)
    )
) {
    val historial by viewModel.historial.collectAsStateWithLifecycle()
    val fc        by viewModel.fc.collectAsStateWithLifecycle()

    // Pre-computar listas fuera del scope de LazyRow
    val historialItems = historial.ifEmpty {
        TvMockData.historialFC.map {
            TvLecturaFC(valorBpm = it.valorBpm, hora = it.hora, esNormal = it.esNormal)
        }
    }
    val alertasItems = TvMockData.alertasRecientes.map {
        TvLecturaFC(valorBpm = it.valorBpm, hora = it.hora, esNormal = it.esNormal)
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(start = 48.dp, top = 32.dp)
    ) {
        Text(
            text  = "SmartHealth TV",
            style = MaterialTheme.typography.headlineLarge,
            color = Color.White
        )
        Text(
            text     = "FC actual: $fc bpm",
            style    = MaterialTheme.typography.titleMedium,
            color    = Color(0xFF64B5F6),
            modifier = Modifier.padding(top = 4.dp, bottom = 24.dp)
        )

        // Fila: Historial FC
        Text("Historial FC", color = Color.LightGray,
             style = MaterialTheme.typography.labelLarge)
        Spacer(Modifier.height(8.dp))
        LazyRow(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
            items(historialItems) { lectura ->
                FcCardItem(lectura = lectura, onClick = { onCardClick(lectura.id) })
            }
        }

        Spacer(Modifier.height(32.dp))

        // Fila: Alertas recientes
        Text("Alertas recientes", color = Color.LightGray,
             style = MaterialTheme.typography.labelLarge)
        Spacer(Modifier.height(8.dp))
        LazyRow(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
            items(alertasItems) { lectura ->
                FcCardItem(lectura = lectura, onClick = { onCardClick(lectura.id) })
            }
        }
    }
}

/** Card individual de FC — Surface con background manual para evitar ClickableSurfaceDefaults */
@Composable
fun FcCardItem(
    lectura: TvLecturaFC,
    onClick: () -> Unit
) {
    val bgColor = if (lectura.esNormal) Color(0xFF1B4F8A) else Color(0xFFB3261E)

    Surface(
        onClick  = onClick,
        modifier = Modifier.size(width = 160.dp, height = 100.dp)
    ) {
        Box(
            modifier         = Modifier
                .fillMaxSize()
                .background(bgColor)
                .padding(12.dp),
            contentAlignment = Alignment.Center
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text(
                    text  = "${lectura.valorBpm} bpm",
                    color = Color.White,
                    style = MaterialTheme.typography.titleLarge
                )
                Text(
                    text  = lectura.hora,
                    color = Color.LightGray,
                    style = MaterialTheme.typography.bodySmall
                )
            }
        }
    }
}
