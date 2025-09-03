package com.example.jjcapitalmanager

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class MyRecyclerAdapter(
    private val dataList: List<String>,
    private val onItemClicked: (Int) -> Unit
) : RecyclerView.Adapter<MyRecyclerAdapter.ViewHolder>() {

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val itemTitle: TextView = view.findViewById(R.id.itemTitle)
        val itemDetails: TextView = view.findViewById(R.id.itemDetails)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.recycler_item, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val data = dataList[position]

        // Split data into title and details (assuming a format like "Title: Details")
        val parts = data.split(", ", limit = 2)
        holder.itemTitle.text = parts[0]  // e.g., "Investment Title"
        holder.itemDetails.text = parts.getOrNull(1) ?: "No details available."

        holder.itemView.setOnClickListener {
            onItemClicked(position + 1)
        }
    }

    override fun getItemCount(): Int {
        return dataList.size
    }
}
