package com.inventorypos.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.Date

@Entity(tableName = "transactions")
data class TransactionEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0L,
    val transactionNumber: String,
    val userId: Long,
    val customerId: Long? = null,
    val totalAmount: Double,
    val discountAmount: Double = 0.0,
    val taxAmount: Double = 0.0,
    val finalAmount: Double,
    val paymentMethod: PaymentMethod = PaymentMethod.CASH,
    val paymentStatus: PaymentStatus = PaymentStatus.PAID,
    val notes: String? = null,
    val syncStatus: SyncStatus = SyncStatus.PENDING,
    val createdAt: Date = Date()
)

enum class PaymentMethod {
    CASH, CARD, QRIS, EWALLET, TRANSFER, SPLIT
}

enum class PaymentStatus {
    PAID, PENDING, PARTIAL, REFUNDED, CANCELLED
}

enum class SyncStatus {
    SYNCED, PENDING, FAILED
}

