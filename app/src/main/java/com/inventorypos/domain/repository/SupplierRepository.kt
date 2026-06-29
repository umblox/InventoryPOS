package com.inventorypos.domain.repository

import com.inventorypos.domain.model.Supplier
import kotlinx.coroutines.flow.Flow

interface SupplierRepository {
    fun getAllSuppliers(): Flow<List<Supplier>>
    suspend fun getSupplierById(id: Long): Supplier?
    suspend fun addSupplier(supplier: Supplier): Long
    suspend fun updateSupplier(supplier: Supplier)
    suspend fun deleteSupplier(id: Long)
}
