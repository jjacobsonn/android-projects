package com.example.jjinvestmenttracker.ui

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import com.example.jjinvestmenttracker.Investment
import com.example.jjinvestmenttracker.R

class AddInvestmentActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_investment)

        val investmentNameInput = findViewById<EditText>(R.id.investmentNameInput)
        val amountInvestedInput = findViewById<EditText>(R.id.amountInvestedInput)
        val returnRateInput = findViewById<EditText>(R.id.returnRateInput)
        val totalGrowthInput = findViewById<EditText>(R.id.totalGrowthInput)

        val addButton = findViewById<Button>(R.id.addInvestmentButton)
        addButton.setOnClickListener {

            val name = investmentNameInput.text.toString()
            val amountInvested = amountInvestedInput.text.toString().toDoubleOrNull()
            val returnRate = returnRateInput.text.toString().toFloatOrNull()
            val totalGrowth = totalGrowthInput.text.toString().toDoubleOrNull()

            if (name.isNotEmpty() && amountInvested != null && returnRate != null && totalGrowth != null)
            {

                val newInvestment = Investment(
                    investmentId = (1..10000).random(),
                    investmentName = name,
                    amountInvested = amountInvested,
                    returnRate = returnRate,
                    totalGrowth = totalGrowth
                )

                val resultIntent = Intent()
                resultIntent.putExtra("NEW_INVESTMENT", newInvestment)
                setResult(Activity.RESULT_OK, resultIntent)
                finish()
            } else {

                // Shows an error or validation if inputs are not correct
                investmentNameInput.error = "Please enter valid data"
            }
        }
    }
}