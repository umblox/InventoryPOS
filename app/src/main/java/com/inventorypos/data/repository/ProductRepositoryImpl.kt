package com.inventorypos.data.repository

import com.inventorypos.data.local.dao.ProductDao
import com.inventorypos.data.local.entity.ProductEntity
import com.inventorypos.data.local.entity.ProductWithCategory
import com.inventorypos.domain.model.Product
import com.inventorypos.domain.repository.ProductRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import java.util.Date
import javax.inject.Inject

class ProductRepositoryImpl @Inject constructor(
    private val productDao: ProductDao
) : ProductRepository {
    
    override fun getAllProducts(): Flow<List<Product>> {
        return productDao.getAllActive().map { list ->
            list.map { it.toDomain() }
        }
    }
    
    override fun searchProducts(query: String): Flow<List<Product>> {
        return productDao.searchProducts(query).map { list ->
            list.map { it.toDomain() }
        }
    }
    
    override suspend fun getProductById(id: Long): Product? {
        return productDao.getByIdWithCategory(id)?.toDomain()
    }
    
    override fun getProductByIdFlow(id: Long): Flow<Product?> {
        return productDao.getByIdFlow(id).map { it?.toDomain() }
    }
    
    override fun getLowStockProducts(): Flow<List<Product>> {
        return productDao.getLowStock().map { list ->
            list.map { it.toDomain() }
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
    
    // MAPPER DARI WADAH GABUNGAN (JOIN) KE DOMAIN MODEL
    private fun ProductWithCategory.toDomain(): Product {
        return Product(
            id = this.product.id,
            name = this.product.name,
            sku = this.product.sku,
            barcode = this.product.barcode,
            categoryId = this.product.categoryId,
            categoryName = this.categoryName, // Nama Kategori berhasil diselamatkan!
            description = this.product.description,
            buyPrice = this.product.buyPrice,
            sellPrice = this.product.sellPrice,
            wholesalePrice = this.product.wholesalePrice,
            stock = this.product.stock,
            minStock = this.product.minStock,
            unit = this.product.unit,
            imageUrl = this.product.imageUrl,
            isActive = this.product.isActive
        )
    }
    
    // MAPPER DARI DOMAIN MODEL KEMBALI KE DATABASE (Hanya Entity Produk)
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
