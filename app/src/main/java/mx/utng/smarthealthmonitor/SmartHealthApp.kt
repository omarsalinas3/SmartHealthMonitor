// SmartHealthApp.kt
package mx.utng.smarthealthmonitor

import android.app.Application
import mx.utng.smarthealthmonitor.data.SmartHealthRepository
import mx.utng.smarthealthmonitor.mqtt.MqttAppService

class SmartHealthApp : Application() {
    lateinit var mqttService: MqttAppService

    override fun onCreate() {
        super.onCreate()
        // Inicializar Room al arrancar la app
        SmartHealthRepository.init(this)
        
        // Programar sync periódico con Neon
        mx.utng.smarthealthmonitor.data.sync.NeonSyncWorker.schedule(this)

        mqttService = MqttAppService(
            context = this,
            onFcReceived = SmartHealthRepository::actualizarFC
        )
        mqttService.connect()
        
        // Trigger initial sync on startup
        SmartHealthRepository.triggerSync()
    }
}
