// tv/.../tv/ui/TvPlaybackScreen.kt
package mx.utng.smarthealthmonitor.tv.ui

import android.net.Uri
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.media3.common.MediaItem
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.ui.PlayerView
import androidx.navigation.NavController
import androidx.tv.material3.*

/**
 * Pantalla de reproducción con ExoPlayer dentro de un Composable.
 * Usa AndroidView { PlayerView } — patrón oficial para TV.
 * El video de demo es un stream de Big Buck Bunny.
 */
@Composable
fun TvPlaybackScreen(navController: NavController) {
    val context = LocalContext.current

    // Inicializar ExoPlayer con video de demo
    val exoPlayer = remember {
        ExoPlayer.Builder(context).build().apply {
            val mediaItem = MediaItem.fromUri(
                Uri.parse("https://commondatastorage.googleapis.com/gtv-videos-bucket/sample/BigBuckBunny.mp4")
            )
            setMediaItem(mediaItem)
            prepare()
            playWhenReady = true
        }
    }

    // Liberar ExoPlayer al salir de la pantalla
    DisposableEffect(Unit) {
        onDispose { exoPlayer.release() }
    }

    Box(
        modifier          = Modifier.fillMaxSize().background(Color.Black),
        contentAlignment  = Alignment.Center
    ) {
        // AndroidView — puente entre View y Composable
        AndroidView(
            factory  = { ctx -> PlayerView(ctx).apply { player = exoPlayer } },
            modifier = Modifier.fillMaxSize()
        )

        // Botón Volver (esquina superior izquierda)
        Button(
            onClick  = {
                exoPlayer.stop()
                navController.popBackStack()
            },
            modifier = Modifier
                .align(Alignment.TopStart)
                .padding(24.dp)
        ) {
            Text("← Volver", fontSize = 14.sp)
        }
    }
}
