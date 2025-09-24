package com.example.jjadventuremap

import android.os.Bundle
import android.widget.FrameLayout
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main) // Use activity_main.xml as the layout

        // Dynamically add the GameView to the game container
        val gameContainer = findViewById<FrameLayout>(R.id.game_container)
        gameContainer.addView(GameView(this)) // Add the GameView
    }
}
