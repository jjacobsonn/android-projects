package com.jj.orionoversight

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Define a local variable for the splash screen duration
        val splashDuration = 3.5f // Unique float variable for splash screen duration in seconds

        // Print a message to LogCat using string interpolation
        Log.i("CS3680", "OrionOversight initialized. Splash screen duration: $splashDuration seconds.")
    }
}