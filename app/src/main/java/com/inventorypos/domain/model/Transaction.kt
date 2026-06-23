package com.inventorypos.domain.model

data class Transaction(
    val id: Long = 0,
    val transactionNumber: String,
    val userId: Long,
    val customerId: Long? = null,
    val customerName: String? = null,
    val totalAmount: Double,
    val discountAmount: Double = 0.0,
    val taxAmount: Double = 0.0,
    val finalAmount: Double,
    val paymentMethod: String,
    val paymentStatus: String,
    val notes: String? = null,
    val items: List<TransactionItem> = emptyList(),
    val createdAt: java.util.Date = java.util.Date()
)

data class TransactionItem(
    val id: Long = 0,
    val transactionId: Long,
    val productId: Long,
    val productName: String,
    val quantity: Int,
    val unitPrice: Double,
    val discount: Double = 0.0,
    val totalPrice: Double
)
