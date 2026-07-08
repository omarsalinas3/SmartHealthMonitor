// tv/.../tv/TvApplication.kt
package mx.utng.smarthealthmonitor.tv

import android.app.Application

/**
 * Application del módulo TV.
 * Inicializa TvRepository (Room DB) al arrancar.
 * Paso 3 — Ejercicio 03.
 */
class TvApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        // Inicializar TvRepository con contexto de la Application
        TvRepository.init(this)
    }
}
