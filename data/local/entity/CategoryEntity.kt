package com.inventorypos.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.Date

@Entity(tableName = "categories")
data class CategoryEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val name: String,
    val description: String? = null,
    val color: String? = null,
    val icon: String? = null,
    val parentId: Long? = null,
    val sortOrder: Int = 0,
    val isActive: Boolean = true,
    val createdAt: Date = Date()
)
