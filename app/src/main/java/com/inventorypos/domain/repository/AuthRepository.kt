package com.inventorypos.domain.repository

import kotlinx.coroutines.flow.Flow

interface AuthRepository {
    suspend fun login(username: String, password: String): Result<Unit>
    suspend fun resetPassword(username: String, newPassword: String): Result<Unit>
    suspend fun logout()
    fun isUserLoggedIn(): Flow<Boolean>
}
