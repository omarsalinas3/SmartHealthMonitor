// presentation/WearDashboardScreen.kt
package mx.utng.smarthealthmonitor.wear.presentation

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.wear.compose.foundation.lazy.ScalingLazyColumn
import androidx.wear.compose.foundation.lazy.rememberScalingLazyListState
import androidx.wear.compose.material.*
import mx.utng.smarthealthmonitor.wear.presentation.components.WearFCCard

@Composable
fun WearDashboardScreen(
    onAlertClick: () -> Unit = {},
    viewModel: WearDashboardViewModel = viewModel()
) {
    val fc    by viewModel.fc.collectAsState()
    val pasos by viewModel.pasos.collectAsState()           // ⭐ Reto adicional
    val listState = rememberScalingLazyListState()

    Scaffold(
        timeText = {
            // La hora desaparece al hacer scroll
            TimeText(modifier = Modifier.scrollAway(listState))
        },
        positionIndicator = {
            PositionIndicator(scalingLazyListState = listState)
        }
    ) {
        ScalingLazyColumn(
            state    = listState,
            modifier = Modifier.fillMaxSize()
        ) {
            // Item 1: Card de FC
            item {
                WearFCCard(
                    fc       = fc,
                    modifier = Modifier.fillMaxWidth()
                )
            }

            // Item 2: Chip de Alerta (rojo)
            item {
                Chip(
                    label  = { Text("⚠ Alerta") },
                    onClick = onAlertClick,
                    colors  = ChipDefaults.primaryChipColors(
                        backgroundColor = MaterialTheme.colors.error
                    ),
                    modifier = Modifier.fillMaxWidth()
                )
            }

            // ⭐ Reto adicional: CompactChip con conteo de pasos
            item {
                CompactChip(
                    label = {
                        Text(
                            text = if (pasos == 0) "-- pasos"
                                   else "%,d pasos".format(pasos)
                        )
                    },
                    onClick = { },
                    modifier = Modifier.fillMaxWidth()
                )
            }
        }
    }
}
