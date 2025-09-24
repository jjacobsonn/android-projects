package com.example.jjunitconverter

import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.Spinner
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {

    private lateinit var etValue: EditText
    private lateinit var spFrom: Spinner
    private lateinit var spTo: Spinner
    private lateinit var btnConvert: Button
    private lateinit var tvResult: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        etValue = findViewById(R.id.et_value)
        spFrom = findViewById(R.id.sp_from)
        spTo = findViewById(R.id.sp_to)
        btnConvert = findViewById(R.id.btn_convert)
        tvResult = findViewById(R.id.tv_result)

        val countries = arrayOf(
            "United States (USD)", "European Union (EUR)", "Brazil (BRL)", "Algeria (DZD)",
            "United Kingdom (GBP)", "Canada (CAD)", "Australia (AUD)", "Japan (JPY)",
            "Switzerland (CHF)", "China (CNY)"
        )
        spFrom.adapter = ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, countries)
        spTo.adapter = ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, countries)

        btnConvert.setOnClickListener {
            performConversion()
        }
    }

    private fun performConversion() {
        val value = etValue.text.toString().toDoubleOrNull()
        if (value != null) {
            val fromCurrency = getCurrencyCode(spFrom.selectedItem.toString())
            val toCurrency = getCurrencyCode(spTo.selectedItem.toString())

            // Currency Conversion Logic
            val result = when {
                fromCurrency == "USD" && toCurrency == "EUR" -> value * 0.85
                fromCurrency == "EUR" && toCurrency == "USD" -> value * 1.18
                fromCurrency == "USD" && toCurrency == "BRL" -> value * 5.23
                fromCurrency == "BRL" && toCurrency == "USD" -> value * 0.19
                fromCurrency == "USD" && toCurrency == "DZD" -> value * 136.55
                fromCurrency == "DZD" && toCurrency == "USD" -> value * 0.0073
                fromCurrency == "EUR" && toCurrency == "BRL" -> value * 6.14
                fromCurrency == "BRL" && toCurrency == "EUR" -> value * 0.16
                fromCurrency == "EUR" && toCurrency == "DZD" -> value * 160.50
                fromCurrency == "DZD" && toCurrency == "EUR" -> value * 0.0062
                fromCurrency == "BRL" && toCurrency == "DZD" -> value * 26.18
                fromCurrency == "DZD" && toCurrency == "BRL" -> value * 0.038
                fromCurrency == "GBP" && toCurrency == "USD" -> value * 1.38
                fromCurrency == "USD" && toCurrency == "GBP" -> value * 0.72
                fromCurrency == "CAD" && toCurrency == "USD" -> value * 0.79
                fromCurrency == "USD" && toCurrency == "CAD" -> value * 1.26
                fromCurrency == "AUD" && toCurrency == "USD" -> value * 0.73
                fromCurrency == "USD" && toCurrency == "AUD" -> value * 1.37
                fromCurrency == "JPY" && toCurrency == "USD" -> value * 0.0091
                fromCurrency == "USD" && toCurrency == "JPY" -> value * 110.51
                fromCurrency == "CHF" && toCurrency == "USD" -> value * 1.09
                fromCurrency == "USD" && toCurrency == "CHF" -> value * 0.92
                fromCurrency == "CNY" && toCurrency == "USD" -> value * 0.15
                fromCurrency == "USD" && toCurrency == "CNY" -> value * 6.47
                fromCurrency == toCurrency -> value
                else -> {
                    tvResult.text = "Conversion not supported"
                    return
                }
            }
            tvResult.text = result.toString()
        } else {
            etValue.error = "Please enter a valid number"
        }
    }

    private fun getCurrencyCode(country: String): String {
        return when (country) {
            "United States (USD)" -> "USD"
            "European Union (EUR)" -> "EUR"
            "Brazil (BRL)" -> "BRL"
            "Algeria (DZD)" -> "DZD"
            "United Kingdom (GBP)" -> "GBP"
            "Canada (CAD)" -> "CAD"
            "Australia (AUD)" -> "AUD"
            "Japan (JPY)" -> "JPY"
            "Switzerland (CHF)" -> "CHF"
            "China (CNY)" -> "CNY"
            else -> ""
        }
    }
}