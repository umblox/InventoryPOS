package com.inventorypos.data.local.dao

import androidx.room.*
import com.inventorypos.data.local.entity.CustomerEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface CustomerDao {
    @Query("SELECT * FROM customers WHERE isActive = 1 ORDER BY name ASC")
    fun getAllActive(): Flow<List<CustomerEntity>>
    
    @Query("SELECT * FROM customers WHERE name LIKE '%' || :query || '%' OR phone LIKE '%' || :query || '%'")
    fun search(query: String): Flow<List<CustomerEntity>>
    
    @Query("SELECT * FROM customers WHERE id = :id")
    suspend fun getById(id: Long): CustomerEntity?
    
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(customer: CustomerEntity): Long
    
    @Update
    suspend fun update(customer: CustomerEntity)
    
    @Query("UPDATE customers SET isActive = 0 WHERE id = :id")
    suspend fun softDelete(id: Long)
    
    @Query("UPDATE customers SET loyaltyPoints = loyaltyPoints + :points WHERE id = :id")
    suspend fun addPoints(id: Long, points: Int)
}
