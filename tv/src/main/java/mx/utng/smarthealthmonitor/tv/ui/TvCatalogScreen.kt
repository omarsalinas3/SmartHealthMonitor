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
import mx.utng.smarthealthmonitor.tv.TvLecturaDisplay
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
    val uiState by viewModel.state.collectAsStateWithLifecycle()

    val historialItems = uiState.lecturas


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
            text     = "FC actual: ${uiState.fcActual} bpm",
            style    = MaterialTheme.typography.titleMedium,
            color    = Color(0xFF64B5F6),
            modifier = Modifier.padding(top = 4.dp, bottom = 24.dp)
        )
        
        // Fila 1: Estado Actual (3 dispositivos)
        Text("Estado Actual (Estadísticas)", color = Color.LightGray,
             style = MaterialTheme.typography.labelLarge)
        Spacer(Modifier.height(8.dp))
        LazyRow(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
            items(uiState.estadisticas) { stat ->
                FcCardItem(lectura = stat, onClick = { onCardClick(stat.id) })
            }
        }
        
        Spacer(Modifier.height(32.dp))

        // Fila 2: Historial Completo
        Text("Historial Completo (Últimas 50)", color = Color.LightGray,
             style = MaterialTheme.typography.labelLarge)
        Spacer(Modifier.height(8.dp))
        LazyRow(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
            items(historialItems) { lectura ->
                FcCardItem(lectura = lectura, onClick = { onCardClick(lectura.id) })
            }
        }

        Spacer(Modifier.height(32.dp))

        // Fila 3: Alertas recientes
        Text("Alertas recientes (Últimas 24h)", color = Color.LightGray,
             style = MaterialTheme.typography.labelLarge)
        Spacer(Modifier.height(8.dp))
        LazyRow(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
            items(uiState.alertas) { alerta ->
                FcCardItem(lectura = alerta, onClick = { onCardClick(alerta.id) })
            }
        }
    }
}

/** Card individual de FC — Surface con background manual para evitar ClickableSurfaceDefaults */
@Composable
fun FcCardItem(
    lectura: TvLecturaDisplay,
    onClick: () -> Unit
) {
    val bgColor = if (lectura.estado == "Normal" || lectura.estado == "Promedio") Color(0xFF1B4F8A) else Color(0xFFB3261E)

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
                    text  = "${lectura.bpm} bpm",
                    color = Color.White,
                    style = MaterialTheme.typography.titleLarge
                )
                Text(
                    text  = "${lectura.dispositivo} - ${lectura.hora}",
                    color = Color.LightGray,
                    style = MaterialTheme.typography.bodySmall
                )
            }
        }
    }
}
