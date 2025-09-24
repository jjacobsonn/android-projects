package com.example.jjloanmanager

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import kotlinx.coroutines.*

class MainActivity : AppCompatActivity() {

    // Triple-quoted string with representation of loan data
    val loanData = """
        1;Car Loan;20000.00;3.5;5
        2;Student Loan;15000.00;5.0;10
        3;Home Loan;250000.00;4.0;30
        4;Personal Loan;5000.00;6.5;3
        5;Business Loan;50000.00;4.5;10
        6;Medical Loan;3000.00;7.0;2
        7;Vacation Loan;1000.00;8.5;1
        8;Equipment Loan;8000.00;7.0;4
        9;Debt Consolidation Loan;12000.00;6.0;5
        10;Wedding Loan;7000.00;9.0;2
        11;Home Improvement Loan;15000.00;5.5;7
        12;Vacation Home Loan;100000.00;3.9;20
    """.trimIndent()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Runs loan management logic in a background thread (coroutine)
        CoroutineScope(Dispatchers.IO).launch {
            processLoans()
        }
    }

    private suspend fun processLoans() = withContext(Dispatchers.Default) {
        val loanList = loanData.lines().map { line ->
            val data = line.split(";")
            Loan(
                uniqueID = data[0].toInt(),
                loanName = data[1],
                principalAmount = data[2].toDouble(),
                interestRate = data[3].toFloat(),
                termInYears = data[4].toInt()
            )
        }

        val loanManager = LoanManager()
        loanManager.initializeList(loanList)

        // Move log printing to the main thread
        withContext(Dispatchers.Main) {
            println("All Loans:")
            loanManager.printAllLoans()
        }

        delay(100)

        withContext(Dispatchers.Main) {
            println("\nLoan at Index 1:")
            val loanAtIndex = loanManager.getLoanByIndex(1)
            println(loanAtIndex)
        }

        delay(100)

        withContext(Dispatchers.Main) {
            println("\nLoan with ID 5:")
            val loanByID = loanManager.getLoanByID(5)
            println(loanByID)
        }

        delay(100)

        withContext(Dispatchers.Main) {
            println("\nDetailed Loan Info for Loan ID 1:")
            loanManager.printDetailedLoanInfo(loanManager.getLoanByID(1)!!)
        }

        delay(100)

        withContext(Dispatchers.Main) {
            println("\nLoans sorted by interest rate:")
            loanManager.sortLoansBy("interest")
        }

        delay(100)

        withContext(Dispatchers.Main) {
            println("\nAmortization Schedule for Car Loan (Loan ID 1):")
            loanManager.generateAmortizationSchedule(loanManager.getLoanByID(1)!!)
        }

        delay(100)

        val newLoan = Loan(13, "Travel Loan", 2000.00, 5.5f, 2)
        loanManager.addLoan(newLoan)

        withContext(Dispatchers.Main) {
            println("\nAfter Adding a New Loan:")
            loanManager.printAllLoans()
        }

        delay(100)

        loanManager.deleteLoanByID(2)

        withContext(Dispatchers.Main) {
            println("\nAfter Deleting Loan with ID 2:")
            loanManager.printAllLoans()
        }
    }
}