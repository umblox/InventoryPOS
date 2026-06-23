package com.inventorypos.data.local.dao

import androidx.room.*
import com.inventorypos.data.local.entity.CategoryEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface CategoryDao {
    @Query("SELECT * FROM categories WHERE isActive = 1 ORDER BY sortOrder ASC, name ASC")
    fun getAllActive(): Flow<List<CategoryEntity>>
    
    @Query("SELECT * FROM categories WHERE id = :id")
    suspend fun getById(id: Long): CategoryEntity?
    
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(category: CategoryEntity): Long
    
    @Update
    suspend fun update(category: CategoryEntity)
    
    @Query("UPDATE categories SET isActive = 0 WHERE id = :id")
    suspend fun softDelete(id: Long)
    
    @Query("DELETE FROM categories WHERE id = :id")
    suspend fun hardDelete(id: Long)
    
    @Query("SELECT COUNT(*) FROM categories WHERE isActive = 1")
    fun getCount(): Flow<Int>
}
