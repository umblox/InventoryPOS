package com.inventorypos.domain.usecase.transaction

import com.inventorypos.domain.model.Transaction
import com.inventorypos.domain.repository.TransactionRepository
import javax.inject.Inject

class CreateTransactionUseCase @Inject constructor(
    private val repository: TransactionRepository
) {
    suspend operator fun invoke(transaction: Transaction): Long {
        return repository.createTransaction(transaction)
    }
}
