package com.inventorypos.data.local.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import java.util.Date

@Entity(
    tableName = "product_suppliers",
    primaryKeys = ["productId", "supplierId"],
    foreignKeys = [
        ForeignKey(entity = ProductEntity::class, parentColumns = ["id"], childColumns = ["productId"], onDelete = ForeignKey.CASCADE),
        ForeignKey(entity = SupplierEntity::class, parentColumns = ["id"], childColumns = ["supplierId"], onDelete = ForeignKey.CASCADE)
    ],
    indices = [Index("supplierId")]
)
data class ProductSupplierEntity(
    val productId: Long,
    val supplierId: Long,
    val buyPrice: Double,
    val isPrimary: Boolean = false,
    val lastUpdated: Date = Date()
)
