package com.example.jjloantracker

import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import androidx.activity.ComponentActivity

class MainActivity : ComponentActivity() {
    private val loanManager = LoanManager()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // References for application UI elements
        val loanIDInput = findViewById<EditText>(R.id.idInput)
        val loanNameInput = findViewById<EditText>(R.id.loanNameInput)
        val principalInput = findViewById<EditText>(R.id.principalInput)
        val interestRateInput = findViewById<EditText>(R.id.interestRateInput)
        val termInput = findViewById<EditText>(R.id.termInput)
        val addButton = findViewById<Button>(R.id.addButton)
        val deleteButton = findViewById<Button>(R.id.deleteButton)
        val clearButton = findViewById<Button>(R.id.clearButton)
        val populateButton = findViewById<Button>(R.id.populateButton)
        val printButton = findViewById<Button>(R.id.printButton)

        // Add Loan Button
        addButton.setOnClickListener {
            val id = loanIDInput.text.toString().toIntOrNull()
            val loanName = loanNameInput.text.toString()
            val principal = principalInput.text.toString().toDoubleOrNull()
            val interestRate = interestRateInput.text.toString().toFloatOrNull()
            val term = termInput.text.toString().toIntOrNull()

            if (id != null && loanName.isNotEmpty() && principal != null && interestRate != null && term != null) {
                val loan = Loan(id, loanName, principal, interestRate, term)
                loanManager.addLoan(loan)
                Log.i("LoanManager", "Loan Added: $loanName, $id")
            } else {
                Log.e("LoanManager", "Please fill in all fields with valid values")
            }
        }

        // Delete Loan Button
        deleteButton.setOnClickListener {
            val id = loanIDInput.text.toString().toIntOrNull()
            if (id != null) {
                loanManager.deleteLoanByID(id)
            } else {
                Log.e("LoanManager", "Please enter a valid Loan ID")
            }
        }

        // Clear All the Loans Button
        clearButton.setOnClickListener {
            loanManager.clearAllLoans()
        }

        // Populates 12 Example Loans Button
        populateButton.setOnClickListener {
            Log.i("LoanManager", "===== Populating Loan Data =====")
            loanManager.initializeList(listOf(
                Loan(1, "Car Loan", 20000.0, 3.5f, 5),
                Loan(2, "Student Loan", 15000.0, 5.0f, 10),
                Loan(3, "Home Loan", 250000.0, 4.0f, 30),
                Loan(4, "Business Loan", 50000.0, 6.5f, 7),
                Loan(5, "Personal Loan", 10000.0, 4.0f, 3),
                Loan(6, "Vacation Loan", 5000.0, 2.5f, 1),
                Loan(7, "Wedding Loan", 15000.0, 4.5f, 5),
                Loan(8, "Medical Loan", 8000.0, 3.0f, 2),
                Loan(9, "Consolidation Loan", 40000.0, 6.0f, 8),
                Loan(10, "Equipment Loan", 120000.0, 5.5f, 10),
                Loan(11, "Mortgage Loan", 300000.0, 3.8f, 30),
                Loan(12, "Auto Loan", 25000.0, 4.2f, 6)
            ))
            Log.i("LoanManager", "Loans populated with 12 items")
        }

        // Prints All of the Loans Button
        printButton.setOnClickListener {
            loanManager.printAllLoans()
        }
    }
}