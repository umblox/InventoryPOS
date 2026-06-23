package com.inventorypos.utils

import java.text.NumberFormat
import java.util.*

object CurrencyFormatter {
    private val formatter = NumberFormat.getCurrencyInstance(Locale("id", "ID"))
    
    init {
        formatter.maximumFractionDigits = 0
    }
    
    fun format(amount: Double): String {
        return formatter.format(amount).replace("Rp", "Rp ").trim()
    }
    
    fun formatWithoutSymbol(amount: Double): String {
        return String.format("%,.0f", amount)
    }
    
    fun parse(formatted: String): Double? {
        return try {
            formatted.replace("[^0-9.]".toRegex(), "").toDouble()
        } catch (e: Exception) {
            null
        }
    }
}
