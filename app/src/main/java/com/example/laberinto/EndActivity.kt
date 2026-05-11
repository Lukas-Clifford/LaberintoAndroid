package com.example.laberinto

import android.media.AudioAttributes
import android.media.SoundPool
import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class EndActivity : BaseActivity()  {
    private lateinit var soundPool: SoundPool // motor de aurdio
    private var winId = 0 // id del audio de victoria

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_end)

        val winLabel = findViewById<TextView>(R.id.winLabel)

        // configuracion del motor de audio
        val attrs = AudioAttributes.Builder()
            .setUsage(AudioAttributes.USAGE_GAME)
            .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
            .build()

        soundPool = SoundPool.Builder()
            .setAudioAttributes(attrs)
            .build()

        // cargar audio
        winId = soundPool.load(this, R.raw.victory, 1)

        // cuando cargue que se reproduzca
        soundPool.setOnLoadCompleteListener { _, sampleId, status ->
            if (status == 0) {
                soundPool.play(winId, 1f, 1f, 1, 0, 1f)
            }
        }

        // animación del texto de victoria
        winLabel.post {
            winLabel.translationY = winLabel.rootView.height.toFloat()
            winLabel.animate()
                .translationY(0f)
                .setDuration(400)
                .start()
        }

    }

}