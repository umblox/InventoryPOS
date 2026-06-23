package com.inventorypos.data.local.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import java.util.Date

@Entity(
    tableName = "products",
    foreignKeys = [
        ForeignKey(
            entity = CategoryEntity::class,
            parentColumns = ["id"],
            childColumns = ["categoryId"],
            onDelete = ForeignKey.SET_NULL
        )
    ],
    indices = [Index(value = ["categoryId"])]
)
data class ProductEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val name: String,
    val sku: String,
    val barcode: String? = null,
    val categoryId: Long? = null,
    val description: String? = null,
    val buyPrice: Double,
    val sellPrice: Double,
    val wholesalePrice: Double? = null,
    val stock: Int = 0,
    val minStock: Int = 5,
    val unit: String = "pcs",
    val imageUrl: String? = null,
    val isActive: Boolean = true,
    val createdAt: Date = Date(),
    val updatedAt: Date = Date()
)
