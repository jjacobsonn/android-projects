package com.example.jjinvestmenttracker.ui

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.jjinvestmenttracker.Investment
import com.example.jjinvestmenttracker.R

class InvestmentAdapter(
    private val investments: MutableList<Investment>,
    private val onItemClick: (Investment) -> Unit,
    private val onDeleteClick: (Investment) -> Unit
) : RecyclerView.Adapter<InvestmentAdapter.InvestmentViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): InvestmentViewHolder
    {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_investment, parent, false)
        return InvestmentViewHolder(view)
    }

    override fun onBindViewHolder(holder: InvestmentViewHolder, position: Int) {
        val investment = investments[position]
        holder.bind(investment)

        holder.itemView.setOnClickListener {
            onItemClick(investment)
        }

        holder.itemView.findViewById<Button>(R.id.deleteButton).setOnClickListener {
            onDeleteClick(investment)
        }
    }

    override fun getItemCount(): Int {
        return investments.size
    }

    class InvestmentViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)
    {
        fun bind(investment: Investment) {
            val nameView = itemView.findViewById<TextView>(R.id.investmentName)
            val amountView = itemView.findViewById<TextView>(R.id.amountInvested)
            val returnRateView = itemView.findViewById<TextView>(R.id.returnRate)
            val totalGrowthView = itemView.findViewById<TextView>(R.id.totalGrowth)

            nameView.text = investment.investmentName
            amountView.text = "$${investment.amountInvested}"
            returnRateView.text = "${investment.returnRate}%"
            totalGrowthView.text = "$${investment.totalGrowth}"
        }
    }
}