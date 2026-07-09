// tv/.../tv/ui/TvDetailScreen.kt
package mx.utng.smarthealthmonitor.tv.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.tv.material3.*
import mx.utng.smarthealthmonitor.tv.TvMockData
import mx.utng.smarthealthmonitor.tv.TvViewModel

/**
 * Pantalla de detalle de una lectura de FC.
 * Muestra datos completos y botones de acción navegables con D-pad.
 * El botón 'Reproducir' navega a TvPlaybackScreen.
 */
@Composable
fun TvDetailScreen(
    lecturaId: Int,
    navController: NavController,
    viewModel: TvViewModel = viewModel()
) {
    val historial by viewModel.historial.collectAsStateWithLifecycle()

    // Buscar en Room o fallback a mock
    val lectura = historial.find { it.id == lecturaId }
        ?: TvMockData.historialFC.getOrNull(lecturaId % TvMockData.historialFC.size)

    val bgColor = if (lectura?.esNormal == true) Color(0xFF1B4F8A) else Color(0xFFB3261E)
    val estado  = if (lectura?.esNormal == true) "✅ Normal" else "⚠ Fuera de rango"

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF0D1117))
            .padding(64.dp),
        verticalArrangement   = Arrangement.Center,
        horizontalAlignment   = Alignment.Start
    ) {
        Text("Detalle de lectura #$lecturaId",
             color = Color.Gray, fontSize = 14.sp)

        Spacer(Modifier.height(16.dp))

        Text(
            text     = "${lectura?.valorBpm ?: "--"} bpm",
            color    = Color.White,
            fontSize = 64.sp
        )

        Text(
            text     = "Hora: ${lectura?.hora ?: "--"}",
            color    = Color.LightGray,
            fontSize = 20.sp,
            modifier = Modifier.padding(top = 8.dp)
        )

        Text(
            text     = estado,
            color    = if (lectura?.esNormal == true) Color(0xFF64B5F6) else Color(0xFFEF9A9A),
            fontSize = 18.sp,
            modifier = Modifier.padding(top = 4.dp, bottom = 32.dp)
        )

        // ── Botones de acción navegables con D-pad ────────────────────
        Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
            Button(onClick = { navController.navigate("playback") }) {
                Text("▶ Reproducir")
            }
            OutlinedButton(onClick = { navController.popBackStack() }) {
                Text("← Volver")
            }
        }
    }
}
