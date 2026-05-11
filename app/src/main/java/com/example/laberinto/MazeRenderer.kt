package com.example.laberinto

import android.content.Context
import android.content.Intent
import android.graphics.Canvas
import android.graphics.Color
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.media.AudioAttributes
import android.media.SoundPool
import android.util.Log
import android.view.View
import android.widget.Toast

class MazeRenderer(context: Context) : View(context), SensorEventListener {

    var isActive = false // determinar si la vista está lista
    lateinit var displaySize: IntArray // tamaño de la pantalla
    lateinit var ball: Ball // bola que se mueve por el laberinto
    lateinit var maze: Maze // laberinto por el que se meve la bola

    private var bounceId = 0 // id del sonido de rebote
    private lateinit var soundPool: SoundPool // motor de audio


    // Métódo que funciona simbólicamente como un constructor, pues cuando termina de expandirse a su tamaño
    // es cuando se configura el resto de elementos
    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)

        displaySize = intArrayOf(width, height)

        // instanciar el laberinto
        maze = Maze(displaySize)

        // instanciar el laberinto y colocarlo en la casilla de inicio
        ball = Ball()
        ball.cx = ( maze.getStartingPosition().first * (maze.TILESIZE) ) + maze.x + maze.TILESIZE/2
        ball.cy = ( maze.getStartingPosition().second * (maze.TILESIZE) ) + maze.y + maze.TILESIZE/2

        try {
            // configuracion de atributos del motor de audio
            val attrs = AudioAttributes.Builder()
                .setUsage(AudioAttributes.USAGE_GAME)
                .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                .build()

            // construccion del motor de audio
            soundPool = SoundPool.Builder()
                .setAudioAttributes(attrs)
                .build()

            // cargar el audio "bounce.wav"
            bounceId = soundPool.load(context, R.raw.bounce, 1)

            // cuando se termine de cargar, asigna el métódo onBounce de la bola
            soundPool.setOnLoadCompleteListener { _, _, status ->
                if (status == 0) {
                    // 0 significa que se cargó bien

                    ball.setOnBounce {
                        soundPool.play(bounceId, 1f, 1f, 1, 0, 1f)
                    }

                } else {
                    // error al cargar el archivo de audio
                    Log.e("MazeRenderer", "Error al cargar el audio de rebote. Status: $status")
                    Toast.makeText(context, "Aviso: Audio de rebote no cargado", Toast.LENGTH_SHORT).show()
                }
            }
        } catch (e: Exception) {
            // el archivo R.raw.bounce no existe, es corrupto o falló el SoundPool
            Log.e("MazeRenderer", "Excepción al intentar inicializar el audio: ${e.message}")
            Toast.makeText(context, "Error de audio", Toast.LENGTH_SHORT).show()
        }

        isActive = true // define la vista como lista para usarse

    }


    // metódo que se llama para dibujar. Se encarga de dibujar el laberinto y la bola
    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        canvas.drawColor(Color.LTGRAY)

        if (isActive) {


            canvas.drawRect(maze.x,maze.y,maze.right,maze.bottom,maze.paint)

            // el laberinto es una matriz de casillas. Cada casilla tiene un id por el que se diferencian para dibujar con diferentes colores
            maze.getTileMazeMatrix().forEach {
                row -> row.forEach {
                    tile ->
                    if (tile.id == 1) canvas.drawRect(tile.x, tile.y, tile.x+tile.size, tile.y+tile.size, maze.wallPaint)
                    if (tile.id == 2) canvas.drawRect(tile.x, tile.y, tile.x+tile.size, tile.y+tile.size, maze.startPaint)
                    if (tile.id == 3) canvas.drawRect(tile.x, tile.y, tile.x+tile.size, tile.y+tile.size, maze.endPaint)
                }
            }


            // se dibuja la bola
            canvas.drawCircle(
                ball.cx, ball.cy, ball.radius.toFloat(), ball.paint
            )

        }

    }


    // metódo necesario para el sensor del acelerómetro
    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {}

    // cuando cambia el valor del sensor se actualiza la posicion de la bola.
    override fun onSensorChanged(event: SensorEvent?) {
        if (event != null && isActive) {
            // actualizar la posición de la bola
            ball.updatePosition(
                -event.values[0]/10,
                event.values[1]/10,
                maze
            )

            // detectar si la bola está dentro de la casilla final
            if (ball.isInsideTile(maze.getEndingTile())) {
                onEnd?.invoke()
            }
        }
        invalidate() // redibujar la view

    }

    // metódo para definir desde MazeActivity cuando se termina el laberinto
    private var onEnd: (() -> Unit)? = null
    fun setOnEnd(listener: () -> Unit) { onEnd = listener }


}