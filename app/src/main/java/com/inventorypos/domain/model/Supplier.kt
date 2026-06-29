package com.inventorypos.domain.model

data class Supplier(
    val id: Long = 0,
    val name: String,
    val phone: String? = null,
    val email: String? = null,
    val address: String? = null,
    val isActive: Boolean = true
)

