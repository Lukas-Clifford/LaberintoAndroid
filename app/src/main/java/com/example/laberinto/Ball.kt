package com.example.laberinto

import android.graphics.Color
import android.graphics.Paint

class Ball () {

    // dimensiones de la bola
    var cx: Float = 100f
    var cy: Float = 100f
    var radius: Int = 40

    // velocidad de la bola
    var velX:Float = 0f
    var velY:Float= 0f
    var accelerationX: Float = 0f
    var accelerationY: Float = 0f

    val MAXSPEED = 15F
    val BOUNCINESS: Float = 0.2f

    // pintura
    var paint: Paint = Paint().apply {
        color = Color.RED
        strokeWidth = 10f
        isAntiAlias = true
        style = Paint.Style.FILL
    }

    // metódo que se define en MazeActivity para reproducir sonido cuando rebota
    private var onBounce: (() -> Unit)? = null

    fun setOnBounce(listener: () -> Unit) {
        onBounce = listener
    }

    // metódo que actualiza la posición y velocidad de la bola
    fun updatePosition(accelerationX: Float, accelerationY: Float, maze: Maze){

        this.accelerationX = accelerationX
        this.accelerationY = accelerationY


        // X AXIS-----------------------
        // Capar velocidad
        if (velX > MAXSPEED) {
            velX = MAXSPEED
        } else if (velX < -MAXSPEED){
            velX = -MAXSPEED
        } else {
            velX += accelerationX
        }

        // evitar que se vaya de los bordes si sale del laberinto
        if (cx + radius > maze.right ) {
            cx = maze.right-radius
            velX *= -BOUNCINESS // invierte velocidad
            onBounce?.invoke() // sonido de rebote

        } else if(cx - radius < maze.x){
            cx = maze.x + radius
            velX *= -BOUNCINESS // invierte velocidad
            onBounce?.invoke() // sonido de rebote

        }




        // Y AXIS-----------------------
        // Capar velocidad
        if ( velY > MAXSPEED ){
            velY = MAXSPEED

        } else if (velY < -MAXSPEED) {
            velY = -MAXSPEED
        } else {
            velY += accelerationY
        }

        // evitar que se vaya de los bordes si sale del laberinto
        if (cy + radius > maze.bottom) {
            cy = maze.bottom - radius
            velY *= -BOUNCINESS // invierte velocidad
            onBounce?.invoke() // sonido de rebote

        } else if (cy - radius < maze.y) {
            cy = maze.y + radius
            velY *= -BOUNCINESS // invierte velocidad
            onBounce?.invoke() // sonido de rebote

        }


        // Colision con las paredes

        for (row in maze.getTileMazeMatrix()) {
            for (tile in row) {

                // si la casilla es id 1, es una pared
                if (tile.id == 1) {
                    // comprueba si la distancia al borde de la casilla mas cercana es menor que la suma de los radios
                    if (collidesWithTile(tile)) {

                        // centro de la casilla
                        val rectCenterX = tile.x + tile.size / 2
                        val rectCenterY = tile.y + tile.size / 2

                        // diferencia entre el centro de la bola y el centro de la casilla
                        val diffX = cx - rectCenterX
                        val diffY = cy - rectCenterY

                        // valor absoluto
                        val absDiffX = Math.abs(diffX)
                        val absDiffY = Math.abs(diffY)

                        // sobrepaso de la bola sobre la casilla
                        val overlapX = (tile.size / 2 + radius) - absDiffX
                        val overlapY = (tile.size / 2 + radius) - absDiffY

                        // si hay menos sobrepaso en el eje x que en el y, quiere decir que hubo una colision en el eje x
                        if (overlapX < overlapY) {
                            velX *= -BOUNCINESS // invierte la velocidad
                            if (Math.abs(velX) > 1.5) onBounce?.invoke() // reproduce el sonido solo si va lo suficientemente rápido

                            // el lado de la colisión se detecta por el signo de la diferencia en el eje
                            // Colisión por la derecha
                            if ( diffX > 0 )  cx = tile.x + tile.size + radius
                            // Colisión por la izquierda
                            else  cx = tile.x - radius


                        } else {
                            velY *= -BOUNCINESS // invierte la velocidad
                            if (Math.abs(velY) > 1.5) onBounce?.invoke() // reproduce el sonido solo si va lo suficientemente rápido

                            // el lado de la colisión se detecta por el signo de la diferencia en el eje
                            // Colisión por la derecha
                            if ( diffY > 0 )  cy = tile.y + tile.size + radius
                            // Colisión por la izquierda
                            else  cy = tile.y - radius

                        }

                    }
                }

            }

        }

        // suma la velocidad a los centros para mover la bola
        this.cy += velY
        this.cx += velX

    }

    // comprueba si la bola toca una casilla
    fun collidesWithTile(tile: Tile): Boolean {
        val closestSides = getClosestSides(tile) // lado mas cercano de la bola a la casilla

        val dx = cx - closestSides.first // coord x
        val dy = cy - closestSides.second // coord y

        val distance = dx * dx + dy * dy // pitágoras

        return distance <= (radius * radius)
    }

    fun isInsideTile(tile: Tile): Boolean {
        val closestSides = getClosestSides(tile)

        val dx = cx - closestSides.first // coord x
        val dy = cy - closestSides.second // coord y

        val distance = dx * dx + dy * dy // pitágoras

        return distance < (radius * radius) / 2
    }


    // comprueba lado mas cercano. Si el centro es mayor que el lado derecho, el lado derecho es el mas cercano
    fun getClosestSides(tile: Tile): Pair<Float, Float> {
        return Pair (
            Math.max( tile.x, Math.min( tile.x + tile.size, cx ) ),
            Math.max( tile.y, Math.min( tile.y + tile.size, cy ) )
        )
    }



}