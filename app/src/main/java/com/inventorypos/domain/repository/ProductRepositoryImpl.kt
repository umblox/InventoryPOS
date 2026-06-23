package com.inventorypos.domain.repository

import com.inventorypos.data.local.dao.ProductDao
import com.inventorypos.data.local.entity.ProductEntity
import com.inventorypos.domain.model.Product
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import java.util.Date
import javax.inject.Inject

class ProductRepositoryImpl @Inject constructor(
    private val productDao: ProductDao
) : ProductRepository {
    
    override fun getAllProducts(): Flow<List<Product>> {
        return productDao.getAllActive().map { entities ->
            entities.map { it.toDomain() }
        }
    }
    
    override fun searchProducts(query: String): Flow<List<Product>> {
        return productDao.searchProducts(query).map { entities ->
            entities.map { it.toDomain() }
        }
    }
    
    override suspend fun getProductById(id: Long): Product? {
        return productDao.getById(id)?.toDomain()
    }
    
    override fun getProductByIdFlow(id: Long): Flow<Product?> {
        return productDao.getByIdFlow(id).map { it?.toDomain() }
    }
    
    override fun getLowStockProducts(): Flow<List<Product>> {
        return productDao.getLowStock().map { entities ->
            entities.map { it.toDomain() }
        }
    }
    
    override suspend fun addProduct(product: Product): Long {
        return productDao.insert(product.toEntity())
    }
    
    override suspend fun updateProduct(product: Product) {
        productDao.update(product.toEntity().copy(updatedAt = Date()))
    }
    
    override suspend fun deleteProduct(id: Long) {
        productDao.softDelete(id, Date())
    }
    
    override fun getProductCount(): Flow<Int> {
        return productDao.getProductCount()
    }
    
    private fun ProductEntity.toDomain(): Product {
        return Product(
            id = id,
            name = name,
            sku = sku,
            barcode = barcode,
            categoryId = categoryId,
            description = description,
            buyPrice = buyPrice,
            sellPrice = sellPrice,
            wholesalePrice = wholesalePrice,
            stock = stock,
            minStock = minStock,
            unit = unit,
            imageUrl = imageUrl,
            isActive = isActive
        )
    }
    
    private fun Product.toEntity(): ProductEntity {
        return ProductEntity(
            id = id,
            name = name,
            sku = sku,
            barcode = barcode,
            categoryId = categoryId,
            description = description,
            buyPrice = buyPrice,
            sellPrice = sellPrice,
            wholesalePrice = wholesalePrice,
            stock = stock,
            minStock = minStock,
            unit = unit,
            imageUrl = imageUrl,
            isActive = isActive
        )
    }
}
