package com.inventorypos.data.local.dao

import androidx.room.*
import com.inventorypos.data.local.entity.ProductEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface ProductDao {
    @Query("SELECT * FROM products WHERE isActive = 1 ORDER BY name ASC")
    fun getAllActive(): Flow<List<ProductEntity>>
    
    @Query("SELECT * FROM products WHERE isActive = 1 AND name LIKE '%' || :query || '%' OR sku LIKE '%' || :query || '%' ORDER BY name ASC")
    fun searchProducts(query: String): Flow<List<ProductEntity>>
    
    @Query("SELECT * FROM products WHERE id = :id")
    suspend fun getById(id: Long): ProductEntity?
    
    @Query("SELECT * FROM products WHERE id = :id")
    fun getByIdFlow(id: Long): Flow<ProductEntity?>
    
    @Query("SELECT * FROM products WHERE categoryId = :categoryId AND isActive = 1")
    fun getByCategory(categoryId: Long): Flow<List<ProductEntity>>
    
    @Query("SELECT * FROM products WHERE stock <= minStock AND isActive = 1")
    fun getLowStock(): Flow<List<ProductEntity>>
    
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(product: ProductEntity): Long
    
    @Update
    suspend fun update(product: ProductEntity)
    
    @Query("UPDATE products SET isActive = 0, updatedAt = :date WHERE id = :id")
    suspend fun softDelete(id: Long, date: java.util.Date)
    
    @Query("DELETE FROM products WHERE id = :id")
    suspend fun hardDelete(id: Long)
    
    @Query("SELECT COUNT(*) FROM products WHERE isActive = 1")
    fun getProductCount(): Flow<Int>
    
    @Query("UPDATE products SET stock = stock + :amount, updatedAt = :date WHERE id = :productId")
    suspend fun adjustStock(productId: Long, amount: Int, date: java.util.Date)
}

