package com.inventorypos.domain.repository

import com.inventorypos.domain.model.Transaction
import kotlinx.coroutines.flow.Flow

interface TransactionRepository {
    fun getAllTransactions(): Flow<List<Transaction>>
    fun getTodayTransactions(): Flow<List<Transaction>>
    suspend fun getTransactionById(id: Long): Transaction?
    suspend fun createTransaction(transaction: Transaction): Long
    suspend fun updateTransaction(transaction: Transaction)
    fun getTodaySales(): Flow<Double?>
    fun getTodayTransactionCount(): Flow<Int>
}
