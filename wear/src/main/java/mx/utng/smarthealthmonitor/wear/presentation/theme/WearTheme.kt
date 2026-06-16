// wear/.../presentation/theme/WearTheme.kt
package mx.utng.smarthealthmonitor.wear.presentation.theme

import androidx.compose.runtime.Composable
import androidx.wear.compose.material.MaterialTheme

/**
 * Tema Wear OS para SmartHealth Monitor.
 * Wear Material Theme — versión circular de MD3.
 */
@Composable
fun SmartHealthWearTheme(
    content: @Composable () -> Unit
) {
    MaterialTheme(
        content = content
    )
}
