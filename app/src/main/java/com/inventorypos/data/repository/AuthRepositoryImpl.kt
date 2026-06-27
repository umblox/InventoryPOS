package com.inventorypos.data.repository

import com.inventorypos.data.local.dao.UserDao
import com.inventorypos.data.local.entity.UserEntity
import com.inventorypos.data.local.entity.UserRole
import com.inventorypos.data.preferences.AuthPreferences
import com.inventorypos.domain.repository.AuthRepository
import kotlinx.coroutines.flow.Flow
import java.util.Date

class AuthRepositoryImpl(
    private val userDao: UserDao,
    private val authPreferences: AuthPreferences
) : AuthRepository {

    override suspend fun login(username: String, password: String): Result<Unit> {
        return try {
            val user = userDao.getByUsername(username)
            
            if (user != null && user.passwordHash == password) {
                userDao.updateLastLogin(user.id, Date())
                authPreferences.saveSession(user.id, user.username)
                Result.success(Unit)
            } else {
                Result.failure(Exception("Username atau password salah!"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun register(fullName: String, username: String, password: String): Result<Unit> {
        return try {
            val existingUser = userDao.getByUsername(username)
            if (existingUser != null) {
                return Result.failure(Exception("Username sudah digunakan!"))
            }

            val newUser = UserEntity(
                fullName = fullName,
                username = username,
                passwordHash = password,
                role = UserRole.ADMIN,
                isActive = true,
                createdAt = Date()
            )
            
            userDao.insert(newUser)
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun resetPassword(username: String, newPassword: String): Result<Unit> {
        return try {
            val user = userDao.getByUsername(username)
            if (user != null) {
                val updatedUser = user.copy(passwordHash = newPassword)
                userDao.update(updatedUser)
                Result.success(Unit)
            } else {
                Result.failure(Exception("User tidak ditemukan"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun logout() {
        authPreferences.clearSession()
    }

    override fun isUserLoggedIn(): Flow<Boolean> {
        return authPreferences.isLoggedIn
    }
}
