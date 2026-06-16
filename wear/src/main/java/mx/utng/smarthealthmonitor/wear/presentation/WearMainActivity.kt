// wear/.../presentation/WearMainActivity.kt
package mx.utng.smarthealthmonitor.wear.presentation

import android.Manifest
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.launch
import mx.utng.smarthealthmonitor.wear.WearDataSender
import mx.utng.smarthealthmonitor.wear.data.WearHealthRepository
import mx.utng.smarthealthmonitor.wear.presentation.theme.SmartHealthWearTheme

/**
 * Actividad principal del módulo Wear OS.
 * Ejercicio 01 S9: usa SmartHealthWearTheme y WearDashboardScreen.
 */
class WearMainActivity : ComponentActivity(), SensorEventListener {

    private lateinit var sensorManager: SensorManager
    private var heartRateSensor: Sensor? = null
    private var stepCountSensor: Sensor? = null
    private lateinit var wearDataSender: WearDataSender

    private val requestPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions()
    ) { permissions ->
        if (permissions[Manifest.permission.BODY_SENSORS] == true) {
            iniciarSensores()
        } else {
            Log.w("WearMain", "Permiso BODY_SENSORS denegado")
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        wearDataSender = WearDataSender(this)
        sensorManager = getSystemService(SENSOR_SERVICE) as SensorManager
        heartRateSensor = sensorManager.getDefaultSensor(Sensor.TYPE_HEART_RATE)
        stepCountSensor = sensorManager.getDefaultSensor(Sensor.TYPE_STEP_COUNTER)

        requestPermissionLauncher.launch(
            arrayOf(
                Manifest.permission.BODY_SENSORS,
                Manifest.permission.ACTIVITY_RECOGNITION
            )
        )

        setContent {
            SmartHealthWearTheme {
                // Ejercicio 02 S9: WearDashboardScreen con FC en tiempo real
                WearDashboardScreen()
            }
        }
    }

    private fun iniciarSensores() {
        heartRateSensor?.let { sensor ->
            sensorManager.registerListener(this, sensor, SensorManager.SENSOR_DELAY_NORMAL)
            Log.d("WearMain", "Sensor FC registrado")
        }
        stepCountSensor?.let { sensor ->
            sensorManager.registerListener(this, sensor, SensorManager.SENSOR_DELAY_NORMAL)
            Log.d("WearMain", "Sensor de pasos registrado")
        }
    }

    override fun onSensorChanged(event: SensorEvent) {
        when (event.sensor.type) {
            Sensor.TYPE_HEART_RATE -> {
                val bpm = event.values[0].toInt()
                if (bpm > 0) {
                    // Actualizar UI del reloj en tiempo real
                    WearHealthRepository.actualizarFC(bpm)
                    Log.d("WearMain", "FC: $bpm bpm")
                    lifecycleScope.launch {
                        try { wearDataSender.enviarFC(bpm) }
                        catch (e: Exception) { Log.e("WearMain", "Error FC: ${e.message}") }
                    }
                }
            }
            Sensor.TYPE_STEP_COUNTER -> {
                val pasos = event.values[0].toInt()
                // Actualizar UI del reloj en tiempo real
                WearHealthRepository.actualizarPasos(pasos)
                lifecycleScope.launch {
                    try { wearDataSender.enviarPasos(pasos) }
                    catch (e: Exception) { Log.e("WearMain", "Error pasos: ${e.message}") }
                }
            }
        }
    }

    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {}

    override fun onDestroy() {
        super.onDestroy()
        sensorManager.unregisterListener(this)
    }
}
