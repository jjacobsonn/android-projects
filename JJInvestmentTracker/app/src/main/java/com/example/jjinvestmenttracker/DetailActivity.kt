package com.example.jjinvestmenttracker.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import android.widget.Button
import android.widget.TextView
import com.example.jjinvestmenttracker.Investment
import com.example.jjinvestmenttracker.R

class DetailActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?)
    {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail)

        // Retrieves the investment object from the intent
        val investment = intent.getParcelableExtra<Investment>("INVESTMENT")

        // Does a null check before proceeding
        if (investment != null) {
            findViewById<TextView>(R.id.investmentName).text = investment.investmentName
            findViewById<TextView>(R.id.amountInvested).text = "\$${investment.amountInvested}"
            findViewById<TextView>(R.id.returnRate).text = "${investment.returnRate}%"
            findViewById<TextView>(R.id.totalGrowth).text = "\$${investment.totalGrowth}"
        } else
        {
            // Handles case where data = null
            findViewById<TextView>(R.id.investmentName).text = "Investment data is missing"
        }

        // Handles back button click
        val backButton = findViewById<Button>(R.id.backButton)
        backButton.setOnClickListener {
            finish()
        }
    }
}