package com.example.jjcapitalmanager

import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.*
import android.content.DialogInterface
import androidx.appcompat.app.AlertDialog
import android.view.LayoutInflater
import android.widget.EditText

class MainActivity : AppCompatActivity() {

    lateinit var dbHelper: DBHelper
    lateinit var recyclerView: RecyclerView
    lateinit var adapter: MyRecyclerAdapter
    var selectedId: Int? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        dbHelper = DBHelper(this)
        recyclerView = findViewById(R.id.recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this)

        // Initializes all the buttons on the frame
        val clearButton: Button = findViewById(R.id.clearButton)
        val populateButton: Button = findViewById(R.id.populateButton)
        val addButton: Button = findViewById(R.id.addButton)
        val printButton: Button = findViewById(R.id.printButton)

        clearButton.setOnClickListener {
            lifecycleScope.launch(Dispatchers.IO) {
                dbHelper.clearDatabase(dbHelper.writableDatabase)
                Log.i("CS3680", "Database cleared!")
                updateRecyclerView()
            }
        }

        populateButton.setOnClickListener {
            lifecycleScope.launch(Dispatchers.IO) {
                dbHelper.populateDatabase(dbHelper.writableDatabase)
                Log.i("CS3680", "Database populated with 12 investment records.")
                updateRecyclerView()
            }
        }

        addButton.setOnClickListener {
            showAddEditDialog(null)
        }

        printButton.setOnClickListener {
            lifecycleScope.launch(Dispatchers.IO) {
                val joinedResults = dbHelper.getAllInvestmentsWithDetails()
                Log.i("CS3680", "Displaying all investments:")
                for (result in joinedResults) {
                    Log.i("CS3680", result)
                }
            }
        }

        lifecycleScope.launch {
            updateRecyclerView()
        }
    }

    // Updates RecyclerView with current data
    private suspend fun updateRecyclerView() {
        val results = withContext(Dispatchers.IO) {
            dbHelper.getAllInvestmentsWithDetails()
        }
        withContext(Dispatchers.Main) {
            adapter = MyRecyclerAdapter(results) { id -> showDetailView(id) }
            recyclerView.adapter = adapter
        }
    }

    // Shows detail view for an investment with the edit and delete buttons/options
    private fun showDetailView(id: Int) {
        val investment = dbHelper.getInvestmentById(id)
        val dialogView = LayoutInflater.from(this).inflate(R.layout.dialog_add_edit, null)

        val inputType = dialogView.findViewById<EditText>(R.id.inputType)
        val inputAmount = dialogView.findViewById<EditText>(R.id.inputAmount)
        val inputDate = dialogView.findViewById<EditText>(R.id.inputDate)
        val inputGrowth = dialogView.findViewById<EditText>(R.id.inputGrowth)
        val inputDuration = dialogView.findViewById<EditText>(R.id.inputDuration)

        // Prepopulate the fields with data
        inputType.setText(investment?.type)
        inputAmount.setText(investment?.amount.toString())
        inputDate.setText(investment?.date)
        inputGrowth.setText(investment?.growthRate.toString())
        inputDuration.setText(investment?.durationYears.toString())

        val dialog = AlertDialog.Builder(this)
            .setView(dialogView)
            .setPositiveButton("Edit", null)
            .setNegativeButton("Delete", null)
            .setNeutralButton("Cancel", null)
            .create()

        dialog.setOnShowListener {
            val positiveButton = dialog.getButton(DialogInterface.BUTTON_POSITIVE)
            val deleteButton = dialog.getButton(DialogInterface.BUTTON_NEGATIVE)

            positiveButton.setOnClickListener {
                // Edits the item
                val type = inputType.text.toString()
                val amount = inputAmount.text.toString().toDoubleOrNull() ?: 0.0
                val date = inputDate.text.toString()
                val growth = inputGrowth.text.toString().toDoubleOrNull() ?: 0.0
                val duration = inputDuration.text.toString().toIntOrNull() ?: 0

                lifecycleScope.launch(Dispatchers.IO) {
                    dbHelper.editInvestment(id, type, amount, date, growth, duration)
                    updateRecyclerView()
                }
                dialog.dismiss()
            }

            deleteButton.setOnClickListener {
                // Delete the item
                lifecycleScope.launch(Dispatchers.IO) {
                    dbHelper.deleteInvestment(id)
                    updateRecyclerView()
                }
                dialog.dismiss()
            }
        }

        dialog.show()
    }

    // Shows dialog to add or edit investment
    private fun showAddEditDialog(id: Int?) {
        val dialogView = LayoutInflater.from(this).inflate(R.layout.dialog_add_edit, null)
        val inputType = dialogView.findViewById<EditText>(R.id.inputType)
        val inputAmount = dialogView.findViewById<EditText>(R.id.inputAmount)
        val inputDate = dialogView.findViewById<EditText>(R.id.inputDate)
        val inputGrowth = dialogView.findViewById<EditText>(R.id.inputGrowth)
        val inputDuration = dialogView.findViewById<EditText>(R.id.inputDuration)

        val isEdit = id != null

        if (isEdit) {
            val investment = dbHelper.getInvestmentById(id!!)
            selectedId = id
            inputType.setText(investment?.type)
            inputAmount.setText(investment?.amount.toString())
            inputDate.setText(investment?.date)
            inputGrowth.setText(investment?.growthRate.toString())
            inputDuration.setText(investment?.durationYears.toString())
        }

        val dialog = AlertDialog.Builder(this)
            .setView(dialogView)
            .setPositiveButton(if (isEdit) "Update" else "Add", null)
            .setNegativeButton("Cancel", null)
            .create()

        dialog.setOnShowListener {
            val positiveButton = dialog.getButton(DialogInterface.BUTTON_POSITIVE)
            positiveButton.setOnClickListener {
                val type = inputType.text.toString()
                val amount = inputAmount.text.toString().toDoubleOrNull() ?: 0.0
                val date = inputDate.text.toString()
                val growth = inputGrowth.text.toString().toDoubleOrNull() ?: 0.0
                val duration = inputDuration.text.toString().toIntOrNull() ?: 0

                lifecycleScope.launch(Dispatchers.IO) {
                    if (isEdit && selectedId != null) {
                        dbHelper.editInvestment(selectedId!!, type, amount, date, growth, duration)
                    } else {
                        dbHelper.addInvestment(type, amount, date, growth, duration)
                    }
                    updateRecyclerView()
                }
                dialog.dismiss()
            }
        }

        dialog.show()
    }

}