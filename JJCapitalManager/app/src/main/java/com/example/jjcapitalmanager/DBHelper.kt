package com.example.jjcapitalmanager

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.util.Log

class DBHelper(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    companion object {
        private const val DATABASE_NAME = "JJCapitalManager.db"
        private const val DATABASE_VERSION = 4
    }

    override fun onCreate(db: SQLiteDatabase?) {
        val createTable1 = """
            CREATE TABLE investments (
                id INTEGER PRIMARY KEY AUTOINCREMENT,
                investment_type TEXT NOT NULL,
                amount REAL NOT NULL,
                date TEXT NOT NULL
            );
        """.trimIndent()

        val createTable2 = """
            CREATE TABLE investment_details (
                id INTEGER PRIMARY KEY AUTOINCREMENT,
                investment_id INTEGER NOT NULL,
                growth_rate REAL NOT NULL,
                duration_years INTEGER NOT NULL,
                FOREIGN KEY (investment_id) REFERENCES investments(id)
            );
        """.trimIndent()

        db?.execSQL(createTable1)
        db?.execSQL(createTable2)
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        db?.execSQL("DROP TABLE IF EXISTS investments")
        db?.execSQL("DROP TABLE IF EXISTS investment_details")
        onCreate(db)
    }

    // Populates the database with 12 records from the Project 2 + Exercise C
    fun populateDatabase(db: SQLiteDatabase) {
        val insertIntoInvestments = """
            INSERT INTO investments (investment_type, amount, date) VALUES 
            ('Tech Stocks', 5000.00, '2023-01-10'), 
            ('Real Estate', 10000.00, '2023-02-15'), 
            ('Bonds', 2000.00, '2023-03-20'),
            ('Cryptocurrency', 1500.00, '2023-04-01'),
            ('Gold', 1000.00, '2023-05-11'),
            ('Commodities', 3000.00, '2023-06-01'),
            ('Mutual Funds', 4000.00, '2023-07-10'),
            ('Hedge Funds', 12000.00, '2023-08-05'),
            ('Private Equity', 15000.00, '2023-09-15'),
            ('Venture Capital', 25000.00, '2023-10-01'),
            ('Foreign Exchange', 7000.00, '2023-11-20'),
            ('Savings Accounts', 500.00, '2023-12-01');
        """.trimIndent()

        val insertIntoInvestmentDetails = """
            INSERT INTO investment_details (investment_id, growth_rate, duration_years) VALUES
            (1, 7.5, 10),
            (2, 4.2, 5),
            (3, 3.0, 8),
            (4, 20.0, 2),
            (5, 2.5, 1),
            (6, 5.0, 4),
            (7, 6.0, 7),
            (8, 10.0, 10),
            (9, 8.0, 8),
            (10, 15.0, 15),
            (11, 4.0, 5),
            (12, 0.5, 1);
        """.trimIndent()

        db.execSQL(insertIntoInvestments)
        db.execSQL(insertIntoInvestmentDetails)
        Log.i("CS3680", "Database populated with 12 investment records.")
    }

    // Clears the database
    fun clearDatabase(db: SQLiteDatabase) {
        db.execSQL("DELETE FROM investments")
        db.execSQL("DELETE FROM investment_details")
        db.execSQL("DELETE FROM sqlite_sequence WHERE name = 'investments';")
        db.execSQL("DELETE FROM sqlite_sequence WHERE name = 'investment_details';")
        Log.i("CS3680", "Database cleared.")
    }

    // Adds a new investment
    fun addInvestment(type: String, amount: Double, date: String, growth: Double, duration: Int) {
        val db = this.writableDatabase

        // Inserts into investments
        val investmentValues = ContentValues()
        investmentValues.put("investment_type", type)
        investmentValues.put("amount", amount)
        investmentValues.put("date", date)
        val investmentId = db.insert("investments", null, investmentValues)

        // Inserts into investment_details
        val detailValues = ContentValues()
        detailValues.put("investment_id", investmentId)
        detailValues.put("growth_rate", growth)
        detailValues.put("duration_years", duration)
        db.insert("investment_details", null, detailValues)

        Log.i("CS3680", "Added new investment: $type")
    }

    // Edits the investments
    fun editInvestment(id: Int, type: String, amount: Double, date: String, growth: Double, duration: Int) {
        val db = this.writableDatabase

        val investmentValues = ContentValues()
        investmentValues.put("investment_type", type)
        investmentValues.put("amount", amount)
        investmentValues.put("date", date)
        db.update("investments", investmentValues, "id=?", arrayOf(id.toString()))

        val detailValues = ContentValues()
        detailValues.put("growth_rate", growth)
        detailValues.put("duration_years", duration)
        db.update("investment_details", detailValues, "investment_id=?", arrayOf(id.toString()))

        Log.i("CS3680", "Investment $id updated.")
    }

    // Deletes the investments
    fun deleteInvestment(id: Int)
    {
        val db = this.writableDatabase
        db.delete("investment_details", "investment_id=?", arrayOf(id.toString()))
        db.delete("investments", "id=?", arrayOf(id.toString()))
        Log.i("CS3680", "Investment $id deleted.")
    }

    // Queries data for RecyclerView
    fun getAllInvestmentsWithDetails(): List<String> {
        val db = this.readableDatabase
        val query = """
            SELECT investments.id, investments.investment_type, investments.amount, investments.date, 
                   investment_details.growth_rate, investment_details.duration_years
            FROM investments
            JOIN investment_details ON investments.id = investment_details.investment_id
        """.trimIndent()

        val cursor = db.rawQuery(query, null)
        val results = mutableListOf<String>()
        while (cursor.moveToNext()) {
            val id = cursor.getInt(0)
            val investmentType = cursor.getString(1)
            val amount = cursor.getDouble(2)
            val date = cursor.getString(3)
            val growthRate = cursor.getDouble(4)
            val duration = cursor.getInt(5)
            results.add("ID: $id, Type: $investmentType, Amount: $amount, Date: $date, Growth: $growthRate%, Duration: $duration years")
        }
        cursor.close()
        return results
    }

    fun getInvestmentById(id: Int): Investment? {
        val db = this.readableDatabase
        val query = """
            SELECT investments.id, investments.investment_type, investments.amount, investments.date, 
                   investment_details.growth_rate, investment_details.duration_years
            FROM investments
            JOIN investment_details ON investments.id = investment_details.investment_id
            WHERE investments.id = ?
        """.trimIndent()

        val cursor = db.rawQuery(query, arrayOf(id.toString()))
        var investment: Investment? = null

        if (cursor.moveToFirst()) {
            val investmentId = cursor.getInt(0)
            val type = cursor.getString(1)
            val amount = cursor.getDouble(2)
            val date = cursor.getString(3)
            val growth = cursor.getDouble(4)
            val duration = cursor.getInt(5)
            investment = Investment(investmentId, type, amount, growth, duration, date)
        }
        cursor.close()
        return investment
    }
}
