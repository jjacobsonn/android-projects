package com.example.jjinvestmenttracker

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.jjinvestmenttracker.ui.AddInvestmentActivity
import com.example.jjinvestmenttracker.ui.InvestmentAdapter

class MainActivity : AppCompatActivity() {

    private lateinit var investmentManager: InvestmentManager
    private lateinit var recyclerView: RecyclerView

    companion object {

        // Requests investment code
        const val ADD_INVESTMENT_REQUEST_CODE = 100
    }

    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Initialize to the investment manager + add dummy data
        investmentManager = InvestmentManager()
        investmentManager.initializeList(generateDummyData()) // Initialize with 12+ items
        recyclerView = findViewById(R.id.recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this)

        // Create the adapter and sets it to the RecyclerView
        val adapter = InvestmentAdapter(
            investments = investmentManager.getAllInvestments(),
            onItemClick = { investment ->
                val intent = Intent(this, com.example.jjinvestmenttracker.ui.DetailActivity::class.java)
                intent.putExtra("INVESTMENT", investment)
                startActivity(intent)
            },
            onDeleteClick = { investment ->
                // Handle the delete action
                investmentManager.deleteInvestmentById(investment.investmentId)
                recyclerView.adapter?.notifyDataSetChanged()
            }
        )
        recyclerView.adapter = adapter

        val addButton = findViewById<Button>(R.id.addButton)
        addButton.setOnClickListener {
            val intent = Intent(this, AddInvestmentActivity::class.java)
            startActivityForResult(intent, ADD_INVESTMENT_REQUEST_CODE)
        }
    }

    // Handles the result from AddInvestmentActivity
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == ADD_INVESTMENT_REQUEST_CODE && resultCode == Activity.RESULT_OK) {
            val newInvestment = data?.getParcelableExtra<Investment>("NEW_INVESTMENT")
            if (newInvestment != null)
            {
                // Adds the new investment
                investmentManager.addInvestment(newInvestment)

                // Notifies the adapter of a data change
                recyclerView.adapter?.notifyDataSetChanged()
            }
        }
    }

    // Dummy data generator to generate the 12 investments
    private fun generateDummyData(): MutableList<Investment> {
        return mutableListOf(
            Investment(1, "Tech Stocks", 5000.00, 7.5f, 300.00),
            Investment(2, "Real Estate", 10000.00, 4.2f, 500.00),
            Investment(3, "Bonds", 2000.00, 3.0f, 100.00),
            Investment(4, "Cryptocurrency", 1500.00, 20.0f, 800.00),
            Investment(5, "Gold", 1000.00, 2.5f, 50.00),
            Investment(6, "Commodities", 3000.00, 5.0f, 200.00),
            Investment(7, "Mutual Funds", 4000.00, 6.0f, 250.00),
            Investment(8, "Hedge Funds", 12000.00, 10.0f, 1200.00),
            Investment(9, "Private Equity", 15000.00, 8.0f, 800.00),
            Investment(10, "Venture Capital", 25000.00, 15.0f, 2000.00),
            Investment(11, "Foreign Exchange", 7000.00, 4.0f, 350.00),
            Investment(12, "Savings Accounts", 500.00, 0.5f, 10.00)
        )
    }
}