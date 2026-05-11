package com.example.laberinto

import android.graphics.Color
import android.graphics.Paint

class Maze (val displaySize: IntArray)  {
    // tamaño de las casillas
    val TILESIZE = 100

    // dimensiones de las casillas segun el tamaño definido
    var w:Float = 10f*TILESIZE
    var x:Float = ( displaySize[0]/2 ) - ( w/2 )
    var y:Float = ( displaySize[1]/2 ) - ( w/2 )
    var right = x + w
    var bottom = y + w

    // matriz obtenida de una imagen usando python leyento los pixeles
    var mazeMatrix = arrayOf(
        arrayOf(1,2,1,1,1,1,1,1,1,1),
        arrayOf(1,0,0,0,0,0,0,1,0,1),
        arrayOf(1,1,1,1,0,1,1,1,0,1),
        arrayOf(1,0,0,1,0,0,0,1,0,1),
        arrayOf(1,0,1,1,1,1,0,1,0,1),
        arrayOf(1,0,0,0,0,0,0,0,0,1),
        arrayOf(1,0,1,0,1,1,1,1,1,1),
        arrayOf(1,0,1,0,1,0,0,0,1,1),
        arrayOf(1,0,0,0,0,0,1,0,0,1),
        arrayOf(1,1,1,1,1,1,1,1,3,1)
    )

    // pinturas
    var paint:Paint = Paint().apply {
        color = Color.rgb(200,200,200)
        strokeWidth = 50f
        isAntiAlias = true
        style = Paint.Style.STROKE
    }

    var wallPaint:Paint = Paint().apply {
        color = Color.BLACK
        strokeWidth = 10f
        isAntiAlias = true
        style = Paint.Style.FILL
    }

    var startPaint:Paint = Paint().apply {
        color = Color.DKGRAY
        strokeWidth = 10f
        isAntiAlias = true
        style = Paint.Style.FILL
    }

    var endPaint:Paint = Paint().apply {
        color = Color.GREEN
        strokeWidth = 10f
        isAntiAlias = true
        style = Paint.Style.FILL
    }

    // devuelve la posición de la casilla donde aparece la bola
    fun getStartingPosition(): Pair<Int, Int> {
        return findPosition(2)
    }

    // devuelve la posición de la casilla donde tiene que llegar la bola
    fun getEndingPosition(): Pair<Int, Int> {
        return findPosition(3)
    }

    // devuelve la casilla final como un objeto Tile
    fun getEndingTile(): Tile {
        return Tile(
            this.x + (getEndingPosition().first*TILESIZE),
            this.y + (getEndingPosition().second*TILESIZE),
            3,
            TILESIZE
        )
    }

    // buscar la posición de una casilla según id en formato pareja de coordenadas
    fun findPosition(value: Int): Pair<Int, Int> {
        for (y in mazeMatrix.indices)
            for (x in mazeMatrix[y].indices)
                if (mazeMatrix[y][x] == value)
                    return Pair(x, y)
        return Pair(-1,-1)
    }

    // devuelve la matriz del laberinto como matriz de casillas (Tiles)
    fun getTileMazeMatrix():Array<Array<Tile>> {
        val tileMaze = Array(mazeMatrix.size) {
            y -> Array(mazeMatrix.size) {
                x -> Tile(
            this.x + (TILESIZE * x),
            this.y + (TILESIZE * y),
            mazeMatrix[y][x],
            TILESIZE
                )
            }
        }
        return tileMaze
    }


}