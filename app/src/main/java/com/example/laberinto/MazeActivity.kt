package com.example.laberinto

import android.content.Intent
import android.hardware.Sensor
import android.hardware.SensorManager
import android.media.AudioAttributes
import android.media.SoundPool
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity

class MazeActivity : BaseActivity()  {


    private var ended = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        // Maze Renderer es una vista personalizada y se establece como contenido
        val mazeRenderer = MazeRenderer(this)
        setContentView(mazeRenderer)

        // ajustes del acelerómetro
        val sensorManager = getSystemService(SENSOR_SERVICE) as SensorManager
        val accelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER)

        if (accelerometer == null) {
            // el dispositivo no tiene acelerómetro
            Toast.makeText(this, "Error: Acelerómetro no disponible en este dispositivo", Toast.LENGTH_LONG).show()
            finish()

        } else {
            sensorManager.registerListener(mazeRenderer, accelerometer, SensorManager.SENSOR_DELAY_GAME)
        }

        // cuando se ejecuta "onEnd" dentro de mazeRenderer, se lanza una nueva actividad
        mazeRenderer.setOnEnd {

            // Hay que evitar que se lance más de una vez
            if (ended) return@setOnEnd
            ended = true

            runOnUiThread {
                startActivity(Intent(this, EndActivity::class.java))
                finish() // tras lanzar la nueva actividad, termina la actual
            }
        }




    }
}