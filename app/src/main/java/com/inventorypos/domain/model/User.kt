package com.inventorypos.domain.model

data class User(
    val id: Long = 0,
    val username: String,
    val fullName: String,
    val email: String? = null,
    val phone: String? = null,
    val role: String,
    val isActive: Boolean = true,
    val lastLogin: java.util.Date? = null
)
