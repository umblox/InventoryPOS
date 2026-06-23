package com.inventorypos.domain.repository

import com.inventorypos.domain.model.User
import kotlinx.coroutines.flow.Flow

interface UserRepository {
    fun getAllUsers(): Flow<List<User>>
    suspend fun getUserById(id: Long): User?
    suspend fun getUserByUsername(username: String): User?
    suspend fun addUser(user: User): Long
    suspend fun updateUser(user: User)
    suspend fun deleteUser(id: Long)
}
