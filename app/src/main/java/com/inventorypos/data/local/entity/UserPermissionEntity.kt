package com.inventorypos.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "user_permissions")
data class UserPermissionEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val userId: Long,
    val permissionName: String,
    val isGranted: Boolean
)
