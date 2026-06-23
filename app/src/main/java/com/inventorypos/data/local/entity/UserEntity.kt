package com.inventorypos.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.Date

@Entity(tableName = "users")
data class UserEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val username: String,
    val passwordHash: String,
    val fullName: String,
    val email: String? = null,
    val phone: String? = null,
    val role: UserRole = UserRole.CASHIER,
    val pin: String? = null,
    val isActive: Boolean = true,
    val lastLogin: Date? = null,
    val createdAt: Date = Date()
)

enum class UserRole {
    SUPER_ADMIN,
    ADMIN,
    CASHIER,
    WAREHOUSE,
    ACCOUNTANT,
    EMPLOYEE
}
