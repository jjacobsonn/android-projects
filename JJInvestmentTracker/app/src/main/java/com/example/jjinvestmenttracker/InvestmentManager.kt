package com.example.jjinvestmenttracker

class InvestmentManager {
    private val investmentList = mutableListOf<Investment>()

    // Initializes list with 'dummy investments'
    fun initializeList(investments: List<Investment>)
    {
        investmentList.clear()
        investmentList.addAll(investments)
    }

    // Get the investment by index
    fun getInvestmentByIndex(index: Int): Investment?
    {
        return investmentList.getOrNull(index)
    }

    // Find investment by ID
    fun getInvestmentById(id: Int): Investment?
    {
        return investmentList.find { it.investmentId == id }
    }

    // Adds a new investment
    fun addInvestment(investment: Investment)
    {
        investmentList.add(investment)
    }

    // Delete a investment by ID
    fun deleteInvestmentById(id: Int): Boolean
    {
        return investmentList.removeIf { it.investmentId == id }
    }

    // Get all investments as a MutableList
    fun getAllInvestments(): MutableList<Investment>
    {
        return investmentList
    }
}
