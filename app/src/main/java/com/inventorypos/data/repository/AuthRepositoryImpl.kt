package com.inventorypos.data.repository

import com.inventorypos.data.local.dao.UserDao
import com.inventorypos.data.local.entity.UserEntity
import com.inventorypos.data.preferences.AuthPreferences
import com.inventorypos.domain.repository.AuthRepository
import kotlinx.coroutines.flow.Flow

class AuthRepositoryImpl(
    private val userDao: UserDao,
    private val authPreferences: AuthPreferences
) : AuthRepository {

    override suspend fun login(username: String, password: String): Result<Unit> {
        return try {
            val user = userDao.getUserByUsername(username)
            if (user != null && user.passwordHash == password) {
                // Login sukses, simpan sesi
                authPreferences.saveSession(user.id, user.username)
                Result.success(Unit)
            } else {
                Result.failure(Exception("Username atau password salah!"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun register(name: String, username: String, password: String): Result<Unit> {
        return try {
            val existingUser = userDao.getUserByUsername(username)
            if (existingUser != null) {
                return Result.failure(Exception("Username sudah digunakan!"))
            }

            val newUser = UserEntity(
                name = name,
                username = username,
                passwordHash = password // Di aplikasi nyata, gunakan hashing (Bcrypt/MD5). Untuk kemudahan sekarang, kita simpan langsung.
            )
            
            userDao.insertUser(newUser)
            Result.success(Unit)
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
