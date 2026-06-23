package com.inventorypos.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.Date

@Entity(tableName = "customers")
data class CustomerEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val name: String,
    val phone: String? = null,
    val email: String? = null,
    val address: String? = null,
    val loyaltyPoints: Int = 0,
    val totalSpent: Double = 0.0,
    val notes: String? = null,
    val isActive: Boolean = true,
    val createdAt: Date = Date()
)
