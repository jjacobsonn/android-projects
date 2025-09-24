package com.example.jjloanmanager

class LoanManager {

    // Stores a list of all the loans
    private val loanList = mutableListOf<Loan>()

    fun initializeList(loans: List<Loan>) {

        // Creates a new fresh empty list
        loanList.clear()

        // Add all new loans to current list
        loanList.addAll(loans)
    }

    fun getLoanByIndex(index: Int): Loan? {

        // Gets the loan(s) by index
        return loanList.getOrNull(index)
    }

    fun getLoanByID(id: Int): Loan? {

        // Finds loan(s) but unique ID
        return loanList.find { it.uniqueID == id }
    }

    fun addLoan(loan: Loan) {

        // Add new loan to list
        loanList.add(loan)
    }

    fun deleteLoanByID(id: Int): Boolean {

        // Find the loan by its unique idea
        return loanList.removeIf { it.uniqueID == id }
    }

    fun printAllLoans() {

        // Display the details of all loans
        loanList.forEach { println(it) }
    }

    fun calculateMonthlyPayment(loan: Loan): Double {

        // Converts the annual interest rate to monthly
        val monthlyRate = (loan.interestRate / 100 / 12).toDouble()

        // Converts years to total months
        val numPayments = loan.termInYears * 12

        // Standard formula to calculate the monthly payment
        return loan.principalAmount * monthlyRate / (1 - Math.pow(1 + monthlyRate, -numPayments.toDouble()))
    }

    fun calculateTotalInterest(loan: Loan): Double {
        // Gets monthly payment
        val monthlyPayment = calculateMonthlyPayment(loan)

        // Assigns total amount paid over loan term
        val totalPayment = monthlyPayment * loan.termInYears * 12
        return totalPayment - loan.principalAmount
    }

    fun printDetailedLoanInfo(loan: Loan) {

        // Calculates monthly payments for loan
        val monthlyPayment = calculateMonthlyPayment(loan)

        // Calculates the total interest for loan info in a readable format
        val totalInterest = calculateTotalInterest(loan)

        // Prints detailed information in lo
        println("""
            Loan Details for ${loan.loanName} (ID: ${loan.uniqueID}):
            - Principal Amount: \$${String.format("%.2f", loan.principalAmount)}
            - Interest Rate: ${String.format("%.2f", loan.interestRate)}%
            - Term: ${loan.termInYears} years
            - Monthly Payment: \$${String.format("%.2f", monthlyPayment)}
            - Total Interest Paid: \$${String.format("%.2f", totalInterest)}
        """.trimIndent())
    }

    fun generateAmortizationSchedule(loan: Loan) {

        // Calculates the monthly loan payment
        val monthlyPayment = calculateMonthlyPayment(loan)

        // Begin with full loan amount
        var remainingBalance = loan.principalAmount

        // Calculates monthly interest rate
        val monthlyRate = loan.interestRate / 100 / 12

        println("\nAmortization Schedule for ${loan.loanName} (Loan ID: ${loan.uniqueID}):")
        println("Month | Principal Paid | Interest Paid | Remaining Balance")

        for (month in 1..loan.termInYears * 12) {
            // Calculates interest rate for the current month
            val interestPayment = remainingBalance * monthlyRate

            // The rest goes toward the principal
            val principalPayment = monthlyPayment - interestPayment

            // Subtract principal payment from balance
            remainingBalance -= principalPayment

            // Show details for the current month
            println("$month | ${String.format("%.2f", principalPayment)} | ${String.format("%.2f", interestPayment)} | ${String.format("%.2f", remainingBalance)}")
            if (remainingBalance <= 0) break
        }
    }

    fun sortLoansBy(criteria: String) {
        when (criteria) {

            // Sorts loan principle amount
            "principal" -> loanList.sortBy { it.principalAmount }

            // Sorts loan by interest rate
            "interest" -> loanList.sortBy { it.interestRate }

            // Sorts loan by term length
            "term" -> loanList.sortBy { it.termInYears }

            // Handles invalid input
            else -> println("Invalid sorting criteria")
        }
        println("\nLoans sorted by $criteria:")
        printAllLoans()
    }
}