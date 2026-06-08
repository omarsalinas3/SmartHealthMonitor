package mx.utng.smarthealthmonitor.wear

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
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.lifecycleScope
import androidx.wear.compose.material.Text
import kotlinx.coroutines.launch

class WearMainActivity : ComponentActivity(), SensorEventListener {

    private lateinit var sensorManager: SensorManager
    private var heartRateSensor: Sensor? = null
    private var stepCountSensor: Sensor? = null
    private lateinit var wearDataSender: WearDataSender

    // Estado observable para la UI del reloj
    private val _bpmState = mutableStateOf(0)
    private val _pasosState = mutableStateOf(0)

    private val requestPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions()
    ) { permissions ->
        if (permissions[Manifest.permission.BODY_SENSORS] == true) {
            iniciarSensor()
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

        // Pedir permisos al iniciar
        requestPermissionLauncher.launch(
            arrayOf(
                Manifest.permission.BODY_SENSORS,
                Manifest.permission.ACTIVITY_RECOGNITION
            )
        )

        setContent {
            val bpm by _bpmState
            val pasos by _pasosState
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.Black),
                contentAlignment = Alignment.Center
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(
                        text = "❤️",
                        fontSize = 28.sp
                    )
                    Text(
                        text = if (bpm > 0) "$bpm bpm" else "Leyendo FC...",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = if (bpm > 100) Color.Red else Color.White
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "🚶",
                        fontSize = 20.sp
                    )
                    Text(
                        text = if (pasos > 0) "$pasos pasos" else "Contando...",
                        fontSize = 14.sp,
                        color = Color(0xFF64B5F6)
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "SmartHealth Monitor",
                        fontSize = 10.sp,
                        color = Color.Gray
                    )
                }
            }
        }
    }

    private fun iniciarSensor() {
        heartRateSensor?.let { sensor ->
            sensorManager.registerListener(
                this, sensor, SensorManager.SENSOR_DELAY_NORMAL
            )
            Log.d("WearMain", "Sensor FC registrado")
        } ?: Log.w("WearMain", "Emulador sin sensor FC")

        // Reto adicional: registrar sensor de pasos
        stepCountSensor?.let { sensor ->
            sensorManager.registerListener(
                this, sensor, SensorManager.SENSOR_DELAY_NORMAL
            )
            Log.d("WearMain", "Sensor de pasos registrado")
        } ?: Log.w("WearMain", "Emulador sin sensor de pasos")
    }

    override fun onSensorChanged(event: SensorEvent) {
        when (event.sensor.type) {
            Sensor.TYPE_HEART_RATE -> {
                val bpm = event.values[0].toInt()
                if (bpm > 0) {
                    _bpmState.value = bpm
                    Log.d("WearMain", "FC: $bpm bpm")
                    lifecycleScope.launch {
                        try { wearDataSender.enviarFC(bpm) }
                        catch (e: Exception) { Log.e("WearMain", "Error FC: ${e.message}") }
                    }
                }
            }
            // Reto adicional: pasos diarios
            Sensor.TYPE_STEP_COUNTER -> {
                val pasos = event.values[0].toInt()
                _pasosState.value = pasos
                Log.d("WearMain", "Pasos: $pasos")
                lifecycleScope.launch {
                    try { wearDataSender.enviarPasos(pasos) }
                    catch (e: Exception) { Log.e("WearMain", "Error pasos: ${e.message}") }
                }
            }
        }
    }

    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {
        Log.d("WearMain", "Precisión del sensor cambiada: $accuracy")
    }

    override fun onDestroy() {
        super.onDestroy()
        sensorManager.unregisterListener(this)
    }
}
