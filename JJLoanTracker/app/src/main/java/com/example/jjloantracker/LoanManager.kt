package com.example.jjloantracker

import android.util.Log

class LoanManager {
    private val loanList = mutableListOf<Loan>()

    // Initializes the  list of the loans without having to clear the existing loans
    fun initializeList(loans: List<Loan>) {

        // Logs each loan that gets added during the initialization phase
        loans.forEach { loan ->
            loanList.add(loan)
            Log.i("LoanManager", "Populated Loan: ${loan.loanName}, ID: ${loan.uniqueID}, Principal: ${loan.principal}, Rate: ${loan.interestRate}%, Term: ${loan.termInYears} years")
        }
    }

    // Retrieves the loan by the unique ID
    fun getLoanByID(id: Int): Loan? {
        val loan = loanList.find { it.uniqueID == id }
        loan?.let {
            Log.i("LoanManager", "Retrieved Loan: ${it.loanName}, ID: ${it.uniqueID}, Principal: ${it.principal}, Rate: ${it.interestRate}%, Term: ${it.termInYears} years")
        } ?: Log.e("LoanManager", "No Loan found with ID: $id")
        return loan
    }

    // Adds a new loan
    fun addLoan(loan: Loan) {
        loanList.add(loan)
        Log.i("LoanManager", "Loan Added: ${loan.loanName}, ID: ${loan.uniqueID}, Principal: ${loan.principal}, Rate: ${loan.interestRate}%, Term: ${loan.termInYears} years")
    }

    // Prints all of the loans in the list that have a header
    fun printAllLoans() {
        if (loanList.isEmpty()) {
            Log.i("LoanManager", "No loans to print.")
        } else {
            Log.i("LoanManager", "===== LIST OF CURRENT LOANS =====")
            loanList.forEach { loan ->
                Log.i("LoanManager", "Loan: ${loan.loanName}, ID: ${loan.uniqueID}, Principal: ${loan.principal}, Rate: ${loan.interestRate}%, Term: ${loan.termInYears} years")
            }
        }
    }

    // Clears all of the loans
    fun clearAllLoans() {
        loanList.clear()
        Log.i("LoanManager", "All loans cleared")
    }

    // Delete a specific loan by its unique ID
    fun deleteLoanByID(id: Int): Boolean {
        val removed = loanList.removeIf { it.uniqueID == id }
        if (removed) {
            Log.i("LoanManager", "Loan Deleted with ID: $id")
        } else {
            Log.e("LoanManager", "Failed to Delete Loan with ID: $id")
        }
        return removed
    }

    // Checks if there are any loans or if list if empty
    fun hasLoans(): Boolean {
        return loanList.isNotEmpty()
    }
}