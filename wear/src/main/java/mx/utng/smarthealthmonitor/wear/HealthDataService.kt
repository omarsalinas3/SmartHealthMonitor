package mx.utng.smarthealthmonitor.wear
 
import android.content.Context
import androidx.health.services.client.HealthServices
import androidx.health.services.client.PassiveListenerService
import androidx.health.services.client.data.DataPointContainer
import androidx.health.services.client.data.DataType
import androidx.health.services.client.data.PassiveListenerConfig
import androidx.health.services.client.data.SampleDataPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import kotlinx.coroutines.guava.await
import kotlinx.coroutines.launch
 
class HealthDataService : PassiveListenerService() {
 
    private val scope = CoroutineScope(Dispatchers.IO + SupervisorJob())
    private lateinit var wearDataSender: WearDataSender
 
    override fun onCreate() {
        super.onCreate()
        wearDataSender = WearDataSender(this)  // S6: MessageClient
    }
 
    override fun onNewDataPointsReceived(dataPoints: DataPointContainer) {
        val fcDataPoints = dataPoints.getData(DataType.HEART_RATE_BPM)
        fcDataPoints.forEach { dataPoint ->
            if (dataPoint is SampleDataPoint<Double>) {
                val bpm = dataPoint.value.toInt()
                scope.launch { wearDataSender.enviarFC(bpm) }
            }
        }

        // Reto adicional: Enviar conteo de pasos
        val pasosDataPoints = dataPoints.getData(DataType.STEPS_DAILY)
        pasosDataPoints.forEach { dataPoint ->
            if (dataPoint is SampleDataPoint<Long>) {
                val pasos = dataPoint.value.toInt()
                scope.launch { wearDataSender.enviarPasos(pasos) }
            }
        }
    }
 
    override fun onDestroy() {
        super.onDestroy()
        scope.cancel()
    }
 
    companion object {
        suspend fun registrar(context: Context) {
            val hsClient = HealthServices.getClient(context)
            val passiveClient = hsClient.passiveMonitoringClient
 
            val config = PassiveListenerConfig.builder()
                .setDataTypes(setOf(DataType.HEART_RATE_BPM, DataType.STEPS_DAILY))
                .setShouldUserActivityInfoBeRequested(true)
                .build()
 
            passiveClient.setPassiveListenerServiceAsync(
                HealthDataService::class.java,
                config
            ).await()
        }
    }
}
