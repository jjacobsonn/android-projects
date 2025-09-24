package com.example.jjadventuremap

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Canvas
import android.graphics.Rect
import android.util.Log
import android.view.MotionEvent
import android.view.View

class GameView(context: Context) : View(context) {

    private val tileMap = listOf(
        "MMM~~........~~~~",
        "MMff~~~~.....~~~~",
        "Mff....~~.....~~~",
        "ff....~~~~~......",
        "f......~~~~......",
        "...~~~........~~~",
        "MMM~~........~~~~",
        "MMff~~~~.....~~~~",
        "Mff....~~.....~~~",
        "ff....~~~~~......",
        "f......~~~~......",
        "...~~~........~~~"
    )

    private val tileSize = 120 // Size of each tile in pixels
    private val screenWidth = 8 // Number of tiles visible horizontally
    private val screenHeight = 6 // Number of tiles visible vertically
    private val mapWidth: Int
    private val mapHeight: Int

    private var playerX = 3 // Player's initial X position
    private var playerY = 3 // Player's initial Y position
    private var offsetX = 0 // Horizontal offset for scrolling
    private var offsetY = 0 // Vertical offset for scrolling

    private lateinit var forest: Bitmap
    private lateinit var mountain: Bitmap
    private lateinit var water: Bitmap
    private lateinit var plain: Bitmap
    private lateinit var treasure: Bitmap
    private lateinit var out: Bitmap
    private lateinit var person: Bitmap

    init {
        require(tileMap.all { it.length == tileMap[0].length }) {
            "All rows in tileMap must have the same length."
        }
        mapWidth = tileMap[0].length
        mapHeight = tileMap.size
        loadBitmaps()
    }

    private fun loadBitmaps() {
        forest = BitmapFactory.decodeResource(resources, R.drawable.forest)
        mountain = BitmapFactory.decodeResource(resources, R.drawable.mountain)
        water = BitmapFactory.decodeResource(resources, R.drawable.water)
        plain = BitmapFactory.decodeResource(resources, R.drawable.plain)
        treasure = BitmapFactory.decodeResource(resources, R.drawable.treasure)
        out = BitmapFactory.decodeResource(resources, R.drawable.out)
        person = BitmapFactory.decodeResource(resources, R.drawable.person)
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        drawMap(canvas)
        drawPlayer(canvas)
    }

    private fun drawMap(canvas: Canvas) {
        for (row in 0 until screenHeight) {
            for (col in 0 until screenWidth) {
                val mapRow = row + offsetY
                val mapCol = col + offsetX

                if (mapRow in 0 until mapHeight && mapCol in 0 until mapWidth) {
                    val tile = tileMap[mapRow][mapCol]
                    val destRect = Rect(
                        col * tileSize,
                        row * tileSize,
                        (col + 1) * tileSize,
                        (row + 1) * tileSize
                    )

                    val bitmap = when (tile) {
                        'M' -> mountain
                        'f' -> forest
                        '~' -> water
                        '.' -> plain
                        't' -> treasure
                        else -> out
                    }
                    canvas.drawBitmap(bitmap, null, destRect, null)
                }
            }
        }
    }

    private fun drawPlayer(canvas: Canvas) {
        val screenPlayerX = playerX - offsetX
        val screenPlayerY = playerY - offsetY
        val destRect = Rect(
            screenPlayerX * tileSize,
            screenPlayerY * tileSize,
            (screenPlayerX + 1) * tileSize,
            (screenPlayerY + 1) * tileSize
        )
        canvas.drawBitmap(person, null, destRect, null)
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        if (event.action == MotionEvent.ACTION_UP) {
            val x = (event.x / tileSize).toInt() + offsetX
            val y = (event.y / tileSize).toInt() + offsetY

            // Prevent movement if outside map bounds
            if (x in 0 until mapWidth && y in 0 until mapHeight) {
                movePlayer(x, y)
            } else {
                Log.w("GameView", "Invalid tap at: ($x, $y) - outside map boundaries")
            }
        }
        return true
    }

    private fun movePlayer(x: Int, y: Int) {
        if (x in 0 until mapWidth && y in 0 until mapHeight) {
            playerX = x
            playerY = y
            adjustOffset()
            invalidate() // Redraw the map
            Log.d("GameView", "Player moved to: ($playerX, $playerY) with offset ($offsetX, $offsetY)")
        } else {
            Log.w("GameView", "Invalid movement: ($x, $y)")
        }
    }

    private fun adjustOffset() {
        // Adjust offsetX if the player is near the horizontal edges of the visible screen
        if (playerX < offsetX + 1 && offsetX > 0) {
            offsetX--
        } else if (playerX > offsetX + screenWidth - 2 && offsetX + screenWidth < mapWidth) {
            offsetX++
        }

        // Adjust offsetY if the player is near the vertical edges of the visible screen
        if (playerY < offsetY + 1 && offsetY > 0) {
            offsetY--
        } else if (playerY > offsetY + screenHeight - 2 && offsetY + screenHeight < mapHeight) {
            offsetY++
        }
    }
}