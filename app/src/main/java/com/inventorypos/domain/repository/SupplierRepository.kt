package com.inventorypos.domain.repository

import com.inventorypos.domain.model.Supplier
import com.inventorypos.domain.model.SupplierOffer
import kotlinx.coroutines.flow.Flow

interface SupplierRepository {
    fun getAllSuppliers(): Flow<List<Supplier>>
    suspend fun getSupplierById(id: Long): Supplier?
    suspend fun addSupplier(supplier: Supplier): Long
    suspend fun updateSupplier(supplier: Supplier)
    suspend fun deleteSupplier(id: Long)
    
    // ➕ TAMBAHAN: Product-Supplier relation
    suspend fun getSuppliersForProduct(productId: Long): List<SupplierOffer>
    suspend fun addSupplierToProduct(productId: Long, supplierId: Long, buyPrice: Double, isPrimary: Boolean)
    suspend fun removeSupplierFromProduct(productId: Long, supplierId: Long)
    suspend fun setPrimarySupplier(productId: Long, supplierId: Long)
    suspend fun updateSupplierPriceForProduct(productId: Long, supplierId: Long, newPrice: Double)
    
}
