package com.inventorypos.data.local.dao

import androidx.room.*
import com.inventorypos.data.local.entity.ProductEntity
import com.inventorypos.data.local.entity.ProductWithCategory
import kotlinx.coroutines.flow.Flow

@Dao
interface ProductDao {
    
    // MENGGUNAKAN LEFT JOIN: Ambil semua data produk dan nama kategorinya
    @Query("""
        SELECT products.*, categories.name AS categoryName 
        FROM products 
        LEFT JOIN categories ON products.categoryId = categories.id 
        WHERE products.isActive = 1 
        ORDER BY products.name ASC
    """)
    fun getAllActive(): Flow<List<ProductWithCategory>>
    
    @Query("""
        SELECT products.*, categories.name AS categoryName 
        FROM products 
        LEFT JOIN categories ON products.categoryId = categories.id 
        WHERE products.isActive = 1 AND (products.name LIKE '%' || :query || '%' OR products.sku LIKE '%' || :query || '%') 
        ORDER BY products.name ASC
    """)
    fun searchProducts(query: String): Flow<List<ProductWithCategory>>
    
    @Query("""
        SELECT products.*, categories.name AS categoryName 
        FROM products 
        LEFT JOIN categories ON products.categoryId = categories.id 
        WHERE products.id = :id
    """)
    suspend fun getByIdWithCategory(id: Long): ProductWithCategory?
    
    @Query("""
        SELECT products.*, categories.name AS categoryName 
        FROM products 
        LEFT JOIN categories ON products.categoryId = categories.id 
        WHERE products.id = :id
    """)
    fun getByIdFlow(id: Long): Flow<ProductWithCategory?>
    
    @Query("""
        SELECT products.*, categories.name AS categoryName 
        FROM products 
        LEFT JOIN categories ON products.categoryId = categories.id 
        WHERE products.stock <= products.minStock AND products.isActive = 1
    """)
    fun getLowStock(): Flow<List<ProductWithCategory>>

    // --- OPERASI CRUD STANDAR (TETAP MENGGUNAKAN ENTITY ASLI) ---
    
    @Query("SELECT * FROM products WHERE id = :id")
    suspend fun getById(id: Long): ProductEntity?

    @Query("SELECT * FROM products WHERE categoryId = :categoryId AND isActive = 1")
    fun getByCategory(categoryId: Long): Flow<List<ProductEntity>>
    
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
