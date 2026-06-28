package com.inventorypos.data.local.dao

import androidx.room.*
import com.inventorypos.data.local.entity.UserPermissionEntity

@Dao
interface UserPermissionDao {
    @Query("SELECT * FROM user_permissions WHERE userId = :userId")
    suspend fun getPermissionsByUserId(userId: Long): List<UserPermissionEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPermissions(permissions: List<UserPermissionEntity>)

    @Query("DELETE FROM user_permissions WHERE userId = :userId")
    suspend fun deletePermissionsByUserId(userId: Long)
}
