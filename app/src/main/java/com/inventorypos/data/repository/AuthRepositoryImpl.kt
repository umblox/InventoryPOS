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
            // Memanggil fungsi getByUsername dari DAO asli Anda
            val user = userDao.getByUsername(username)
            
            if (user != null && user.passwordHash == password) {
                // Memanfaatkan fitur keren dari DAO Anda: Catat waktu login terakhir
                userDao.updateLastLogin(user.id, Date())
                
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

    override suspend fun register(fullName: String, username: String, password: String): Result<Unit> {
        return try {
            // Cek apakah username sudah ada menggunakan DAO asli Anda
            val existingUser = userDao.getByUsername(username)
            if (existingUser != null) {
                return Result.failure(Exception("Username sudah digunakan!"))
            }

            // Menyusun data sesuai dengan UserEntity asli Anda
            val newUser = UserEntity(
                fullName = fullName, 
                username = username,
                passwordHash = password,
                role = UserRole.ADMIN, // Jadikan pendaftar pertama sebagai ADMIN
                isActive = true,
                createdAt = Date()
            )
            
            // Memanggil fungsi insert dari DAO asli Anda
            userDao.insert(newUser)
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
