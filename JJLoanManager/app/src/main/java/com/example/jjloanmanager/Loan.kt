package com.example.jjloanmanager

data class Loan(
    val uniqueID: Int,
    val loanName: String,
    val principalAmount: Double,
    val interestRate: Float,
    val termInYears: Int
)