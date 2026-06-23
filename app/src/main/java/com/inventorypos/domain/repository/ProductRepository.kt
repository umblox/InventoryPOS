package com.inventorypos.domain.repository

import com.inventorypos.domain.model.Product
import kotlinx.coroutines.flow.Flow

interface ProductRepository {
    fun getAllProducts(): Flow<List<Product>>
    fun searchProducts(query: String): Flow<List<Product>>
    suspend fun getProductById(id: Long): Product?
    fun getProductByIdFlow(id: Long): Flow<Product?>
    fun getLowStockProducts(): Flow<List<Product>>
    suspend fun addProduct(product: Product): Long
    suspend fun updateProduct(product: Product)
    suspend fun deleteProduct(id: Long)
    fun getProductCount(): Flow<Int>
}
