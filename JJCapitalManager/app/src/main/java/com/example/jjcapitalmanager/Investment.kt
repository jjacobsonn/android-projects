package com.example.jjcapitalmanager

data class Investment(
    val id: Int,
    val type: String,
    val amount: Double,
    val growthRate: Double,
    val durationYears: Int,
    val date: String
)
