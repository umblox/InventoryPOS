package com.inventorypos.data.local.dao

import androidx.room.*
import com.inventorypos.data.local.entity.TransactionEntity
import com.inventorypos.data.local.entity.TransactionItemEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface TransactionDao {
    @Transaction
    @Query("SELECT * FROM transactions ORDER BY createdAt DESC")
    fun getAll(): Flow<List<TransactionWithItems>>
    
    @Transaction
    @Query("SELECT * FROM transactions WHERE id = :id")
    suspend fun getById(id: Long): TransactionWithItems?
    
    @Transaction
    @Query("SELECT * FROM transactions WHERE DATE(createdAt) = DATE('now') ORDER BY createdAt DESC")
    fun getTodayTransactions(): Flow<List<TransactionWithItems>>
    
    @Query("SELECT SUM(finalAmount) FROM transactions WHERE DATE(createdAt) = DATE('now') AND paymentStatus = 'PAID'")
    fun getTodaySales(): Flow<Double?>
    
    @Insert
    suspend fun insertTransaction(transaction: TransactionEntity): Long
    
    @Insert
    suspend fun insertTransactionItems(items: List<TransactionItemEntity>)
    
    @Update
    suspend fun updateTransaction(transaction: TransactionEntity)
    
    @Transaction
    suspend fun insertTransactionWithItems(
        transaction: TransactionEntity,
        items: List<TransactionItemEntity>
    ): Long {
        val transactionId = insertTransaction(transaction)
        val itemsWithId = items.map { it.copy(transactionId = transactionId) }
        insertTransactionItems(itemsWithId)
        return transactionId
    }
    
    @Query("SELECT COUNT(*) FROM transactions WHERE DATE(createdAt) = DATE('now')")
    fun getTodayTransactionCount(): Flow<Int>
}

data class TransactionWithItems(
    @Embedded val transaction: TransactionEntity,
    @Relation(
        parentColumn = "id",
        entityColumn = "transactionId"
    )
    val items: List<TransactionItemEntity>
)
