// tv/.../tv/ui/theme/SmartHealthTvTheme.kt
package mx.utng.smarthealthmonitor.tv.ui.theme

import androidx.compose.runtime.Composable
import androidx.tv.material3.MaterialTheme
import androidx.tv.material3.darkColorScheme

private val TvColorScheme = darkColorScheme()

/**
 * Tema Compose for TV del módulo SmartHealth.
 * Usa tv-material3 con colorScheme oscuro por defecto.
 */
@Composable
fun SmartHealthTvTheme(content: @Composable () -> Unit) {
    MaterialTheme(
        colorScheme = TvColorScheme,
        content     = content
    )
}
