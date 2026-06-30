package com.inventorypos.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.Date

@Entity(tableName = "suppliers")
data class SupplierEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0L,
    val name: String,
    val phone: String? = null,
    val email: String? = null,
    val address: String? = null,
    val isActive: Boolean = true,
    val createdAt: Date = Date(),
    val updatedAt: Date = Date()
)
