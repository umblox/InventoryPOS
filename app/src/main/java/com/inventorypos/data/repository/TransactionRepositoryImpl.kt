package com.inventorypos.data.repository

import com.inventorypos.data.local.dao.ProductDao
import com.inventorypos.data.local.dao.StockDao
import com.inventorypos.data.local.dao.TransactionDao
import com.inventorypos.data.local.entity.PaymentMethod
import com.inventorypos.data.local.entity.PaymentStatus
import com.inventorypos.data.local.entity.StockLogEntity
import com.inventorypos.data.local.entity.StockLogType
import com.inventorypos.data.local.entity.TransactionEntity
import com.inventorypos.data.local.entity.TransactionItemEntity
import com.inventorypos.data.local.dao.TransactionWithItems // Gabungan
import com.inventorypos.domain.model.Transaction
import com.inventorypos.domain.model.TransactionItem
import com.inventorypos.domain.repository.TransactionRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import java.util.Date
import javax.inject.Inject

class TransactionRepositoryImpl @Inject constructor(
    private val transactionDao: TransactionDao,
    private val productDao: ProductDao,
    private val stockDao: StockDao
) : TransactionRepository {

    override fun getAllTransactions(): Flow<List<Transaction>> {
        return transactionDao.getAll().map { list ->
            list.map { it.toDomain() }
        }
    }

    override fun getTodayTransactions(): Flow<List<Transaction>> {
        return transactionDao.getTodayTransactions().map { list ->
            list.map { it.toDomain() }
        }
    }

    override suspend fun getTransactionById(id: Long): Transaction? {
        return transactionDao.getById(id)?.toDomain()
    }

    override fun getTodaySales(): Flow<Double?> {
        return transactionDao.getTodaySales()
    }

    override fun getTodayTransactionCount(): Flow<Int> {
        return transactionDao.getTodayTransactionCount()
    }

    override suspend fun createTransaction(transaction: Transaction): Long {
        val transactionEntity = transaction.toEntity()
        val itemEntities = transaction.items.map { it.toEntity() }

        val transactionId = transactionDao.insertTransactionWithItems(
            transaction = transactionEntity,
            items = itemEntities
        )

        transaction.items.forEach { item ->
            val currentStock = productDao.getById(item.productId)?.stock ?: 0
            val newStock = currentStock - item.quantity

            productDao.adjustStock(
                productId = item.productId,
                amount = -item.quantity,
                date = Date()
            )

            stockDao.insertLog(
                StockLogEntity(
                    productId = item.productId,
                    type = StockLogType.OUT,
                    quantity = item.quantity,
                    previousStock = currentStock,
                    newStock = newStock,
                    reference = transaction.transactionNumber,
                    notes = "Transaction: ${transaction.transactionNumber}",
                    userId = transaction.userId,
                    createdAt = Date()
                )
            )
        }

        return transactionId
    }

    override suspend fun updateTransaction(transaction: Transaction) {
        transactionDao.updateTransaction(transaction.toEntity())
    }

    private fun Transaction.toEntity(): TransactionEntity {
        return TransactionEntity(
            id = this.id,
            transactionNumber = this.transactionNumber,
            userId = this.userId,
            customerId = this.customerId,
            totalAmount = this.totalAmount,
            discountAmount = this.discountAmount,
            taxAmount = this.taxAmount,
            finalAmount = this.finalAmount,
            paymentMethod = PaymentMethod.valueOf(this.paymentMethod),
            paymentStatus = PaymentStatus.valueOf(this.paymentStatus),
            notes = this.notes,
            createdAt = this.createdAt
        )
    }

    private fun TransactionItem.toEntity(): TransactionItemEntity {
        return TransactionItemEntity(
            id = this.id,
            transactionId = this.transactionId,
            productId = this.productId,
            productName = this.productName,
            quantity = this.quantity,
            unitPrice = this.unitPrice,
            discount = this.discount,
            totalPrice = this.totalPrice
        )
    }

    private fun TransactionWithItems.toDomain(): Transaction {
        return Transaction(
            id = this.transaction.id,
            transactionNumber = this.transaction.transactionNumber,
            userId = this.transaction.userId,
            customerId = this.transaction.customerId,
            customerName = null,
            totalAmount = this.transaction.totalAmount,
            discountAmount = this.transaction.discountAmount,
            taxAmount = this.transaction.taxAmount,
            finalAmount = this.transaction.finalAmount,
            paymentMethod = this.transaction.paymentMethod.name,
            paymentStatus = this.transaction.paymentStatus.name,
            notes = this.transaction.notes,
            items = this.items.map { it.toDomain() },
            createdAt = this.transaction.createdAt
        )
    }

    private fun TransactionItemEntity.toDomain(): TransactionItem {
        return TransactionItem(
            id = this.id,
            transactionId = this.transactionId,
            productId = this.productId,
            productName = this.productName,
            quantity = this.quantity,
            unitPrice = this.unitPrice,
            discount = this.discount,
            totalPrice = this.totalPrice
        )
    }
}
