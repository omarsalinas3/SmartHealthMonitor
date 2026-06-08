// SmartHealthApp.kt
package mx.utng.smarthealthmonitor

import android.app.Application
import mx.utng.smarthealthmonitor.data.SmartHealthRepository

class SmartHealthApp : Application() {
    override fun onCreate() {
        super.onCreate()
        // Inicializar Room al arrancar la app
        SmartHealthRepository.init(this)
        // Reto adicional: limpiar lecturas con más de 7 días
        SmartHealthRepository.limpiarHistorialAntiguo()
    }
}
