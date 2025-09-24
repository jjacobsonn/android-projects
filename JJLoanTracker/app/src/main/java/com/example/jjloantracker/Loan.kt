package com.example.jjloantracker

data class Loan(
    val uniqueID: Int,
    val loanName: String,
    val principal: Double,
    val interestRate: Float,
    val termInYears: Int
)