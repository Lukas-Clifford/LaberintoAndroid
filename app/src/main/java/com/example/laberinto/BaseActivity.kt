package com.example.laberinto

import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity

open class BaseActivity : AppCompatActivity(), SensorEventListener {

    private lateinit var sensorManager: SensorManager
    private var lightSensor: Sensor? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        sensorManager = getSystemService(Context.SENSOR_SERVICE) as SensorManager
        lightSensor = sensorManager.getDefaultSensor(Sensor.TYPE_LIGHT)
    }

    override fun onSensorChanged(event: SensorEvent?) {
        if (event?.sensor?.type == Sensor.TYPE_LIGHT) {
            val lightValue = event.values[0]

            val calculatedBrightness = (lightValue / 1000f).coerceIn(0.1f, 1.0f)

            val layoutParams = window.attributes
            layoutParams.screenBrightness = calculatedBrightness
            window.attributes = layoutParams
        }
    }

    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {
        // ignorado
    }
}