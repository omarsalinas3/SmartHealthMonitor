// ui/components/TarjetaDato.kt
package mx.utng.smarthealthmonitor.ui.components
 
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.DirectionsWalk
import mx.utng.smarthealthmonitor.ui.theme.SmartHealthMonitorTheme
 
@Composable
fun TarjetaDato(
    valor: String,                        // "78" o "4,250"
    unidad: String,                       // "bpm" o "pasos"
    label: String,                        // "Frecuencia cardíaca"
    colorValor: Color,                    // MaterialTheme.colorScheme.error
    modifier: Modifier = Modifier,        // ← siempre en Composables reutilizables
    icono: ImageVector? = null,           // Reto adicional 1: parámetro opcional
    estadoText: String? = null,           // Reto adicional 2: texto del estado
    isNormal: Boolean = true              // Reto adicional 2: indica si es normal
) {
    ElevatedCard(
        modifier = modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier.padding(horizontal = 20.dp, vertical = 16.dp),
            horizontalAlignment = Alignment.Start
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = label,
                    style = MaterialTheme.typography.labelMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                // Reto adicional 2: Chip de estado
                if (estadoText != null) {
                    val containerColor = if (isNormal) Color(0xFF4CAF50).copy(alpha = 0.15f) else MaterialTheme.colorScheme.errorContainer
                    val labelColor = if (isNormal) Color(0xFF2E7D32) else MaterialTheme.colorScheme.onErrorContainer
                    
                    AssistChip(
                        onClick = {},
                        label = { Text(estadoText, fontWeight = FontWeight.Bold) },
                        colors = AssistChipDefaults.assistChipColors(
                            containerColor = containerColor,
                            labelColor = labelColor
                        ),
                        border = null
                    )
                }
            }
            Spacer(Modifier.height(4.dp))
            Row(
                verticalAlignment = Alignment.Bottom,
                horizontalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                // Mostrar ícono si no es null
                if (icono != null) {
                    Icon(
                        imageVector = icono,
                        contentDescription = null,
                        tint = colorValor,
                        modifier = Modifier.size(28.dp).padding(bottom = 2.dp)
                    )
                }
                
                Text(
                    text = valor,
                    style = MaterialTheme.typography.headlineMedium,
                    fontWeight = FontWeight.Bold,
                    color = colorValor
                )
                Text(
                    text = unidad,
                    style = MaterialTheme.typography.titleSmall,
                    color = colorValor,
                    modifier = Modifier.padding(bottom = 4.dp)
                )
            }
        }
    }
}
 
// Preview con datos de prueba y reto adicional
@Preview(showBackground = true, name = "TarjetaDato - FC")
@Composable
private fun TarjetaDatoPreview() {
    SmartHealthMonitorTheme {
        Column(Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
            TarjetaDato(
                valor = "78", unidad = "bpm",
                label = "Frecuencia cardíaca",
                colorValor = MaterialTheme.colorScheme.error,
                icono = Icons.Default.Favorite, // Icono para FC
                estadoText = "Normal",
                isNormal = true
            )
            TarjetaDato(
                valor = "4,250", unidad = "pasos",
                label = "Pasos del día",
                colorValor = MaterialTheme.colorScheme.primary,
                icono = Icons.Default.DirectionsWalk // Icono para Pasos
            )
        }
    }
}
