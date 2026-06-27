package com.inventorypos.domain.repository

import kotlinx.coroutines.flow.Flow

interface AuthRepository {
    suspend fun login(username: String, password: String): Result<Unit>
    // Disesuaikan dengan entitas asli: menggunakan fullName
    suspend fun register(fullName: String, username: String, password: String): Result<Unit>
    suspend fun logout()
    fun isUserLoggedIn(): Flow<Boolean>
}
