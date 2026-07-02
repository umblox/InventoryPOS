package com.inventorypos.data.local.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "purchase_order_items",
    foreignKeys = [
        ForeignKey(
            entity = PurchaseOrderEntity::class,
            parentColumns = ["id"],
            childColumns = ["poId"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [Index("poId"), Index("productId")]
)
data class PurchaseOrderItemEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0L,
    val poId: Long,
    val productId: Long,
    val productName: String,
    val buyPrice: Double, // Harga kesepakatan saat PO dibuat
    val quantityOrdered: Int, // Jumlah yang dipesan ke pabrik
    val quantityReceived: Int = 0 // Jumlah real yang datang ke toko (awalnya 0)
)

