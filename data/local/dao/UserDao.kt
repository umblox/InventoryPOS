package com.inventorypos.data.local.dao

import androidx.room.*
import com.inventorypos.data.local.entity.UserEntity
import com.inventorypos.data.local.entity.UserRole
import kotlinx.coroutines.flow.Flow

@Dao
interface UserDao {
    @Query("SELECT * FROM users WHERE isActive = 1 ORDER BY fullName ASC")
    fun getAllActive(): Flow<List<UserEntity>>
    
    @Query("SELECT * FROM users WHERE id = :id")
    suspend fun getById(id: Long): UserEntity?
    
    @Query("SELECT * FROM users WHERE username = :username AND isActive = 1 LIMIT 1")
    suspend fun getByUsername(username: String): UserEntity?
    
    @Query("SELECT * FROM users WHERE role = :role AND isActive = 1")
    fun getByRole(role: UserRole): Flow<List<UserEntity>>
    
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(user: UserEntity): Long
    
    @Update
    suspend fun update(user: UserEntity)
    
    @Query("UPDATE users SET isActive = 0 WHERE id = :id")
    suspend fun softDelete(id: Long)
    
    @Query("UPDATE users SET lastLogin = :date WHERE id = :id")
    suspend fun updateLastLogin(id: Long, date: java.util.Date)
}
