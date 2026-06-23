package com.inventorypos.domain.model

data class Customer(
    val id: Long = 0,
    val name: String,
    val phone: String? = null,
    val email: String? = null,
    val address: String? = null,
    val loyaltyPoints: Int = 0,
    val totalSpent: Double = 0.0,
    val notes: String? = null,
    val isActive: Boolean = true
)
