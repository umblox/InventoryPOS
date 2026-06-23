package com.inventorypos.utils

object ValidationUtils {
    fun isValidEmail(email: String): Boolean {
        return email.matches(Regex("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$"))
    }
    
    fun isValidPhone(phone: String): Boolean {
        return phone.matches(Regex("^[0-9]{10,15}$"))
    }
    
    fun isValidSKU(sku: String): Boolean {
        return sku.isNotBlank() && sku.length <= 50
    }
    
    fun isValidPrice(price: String): Boolean {
        val value = price.toDoubleOrNull()
        return value != null && value >= 0
    }
    
    fun isValidStock(stock: String): Boolean {
        val value = stock.toIntOrNull()
        return value != null && value >= 0
    }
}
