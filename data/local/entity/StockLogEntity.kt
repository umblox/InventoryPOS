package com.inventorypos.data.local.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import java.util.Date

@Entity(
    tableName = "stock_logs",
    foreignKeys = [
        ForeignKey(
            entity = ProductEntity::class,
            parentColumns = ["id"],
            childColumns = ["productId"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [Index(value = ["productId"]), Index(value = ["createdAt"])]
)
data class StockLogEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val productId: Long,
    val type: StockLogType,
    val quantity: Int,
    val previousStock: Int,
    val newStock: Int,
    val reference: String? = null,
    val notes: String? = null,
    val userId: Long,
    val createdAt: Date = Date()
)

enum class StockLogType {
    IN, OUT, TRANSFER, ADJUSTMENT, OPNAME
}
