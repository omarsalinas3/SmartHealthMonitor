package mx.utng.smarthealthmonitor.wear
 
import android.content.Context
import android.util.Log
import com.google.android.gms.wearable.Wearable
import kotlinx.coroutines.tasks.await
 
class WearDataSender(private val context: Context) {
    
    private val messageClient = Wearable.getMessageClient(context)
    private val nodeClient = Wearable.getNodeClient(context)
 
    suspend fun enviarFC(bpm: Int) {
        enviarMensaje("/smarthealthmonitor/fc", bpm.toString())
    }

    suspend fun enviarPasos(pasos: Int) {
        enviarMensaje("/smarthealthmonitor/pasos", pasos.toString())
    }
 
    private suspend fun enviarMensaje(path: String, payload: String) {
        try {
            val nodes = nodeClient.connectedNodes.await()
            for (node in nodes) {
                messageClient.sendMessage(node.id, path, payload.toByteArray()).await()
                Log.d("WearDataSender", "Mensaje enviado a ${node.displayName}: $payload en $path")
            }
        } catch (e: Exception) {
            Log.e("WearDataSender", "Error al enviar mensaje: ${e.message}")
        }
    }
}
