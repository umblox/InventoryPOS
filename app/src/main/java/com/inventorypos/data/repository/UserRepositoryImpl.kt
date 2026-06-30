package com.inventorypos.data.repository

import com.inventorypos.data.local.dao.UserDao
import com.inventorypos.data.local.entity.UserEntity
import com.inventorypos.data.local.entity.UserRole
import com.inventorypos.domain.model.User
import com.inventorypos.domain.repository.UserRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class UserRepositoryImpl @Inject constructor(
    private val userDao: UserDao
) : UserRepository {

    override fun getAllUsers(): Flow<List<User>> {
        return userDao.getAllActive().map { list ->
            list.map { it.toDomain() }
        }
    }

    override suspend fun getUserById(id: Long): User? {
        return userDao.getById(id)?.toDomain()
    }

    override suspend fun getUserByUsername(username: String): User? {
        return userDao.getByUsername(username)?.toDomain()
    }

    override suspend fun addUser(user: User): Long {
        return userDao.insert(user.toEntity())
    }

    override suspend fun updateUser(user: User) {
        userDao.update(user.toEntity())
    }

    override suspend fun deleteUser(id: Long) {
        userDao.softDelete(id)
    }

    private fun UserEntity.toDomain(): User {
        return User(
            id = this.id,
            username = this.username,
            fullName = this.fullName,
            email = this.email,
            phone = this.phone,
            role = this.role.name,
            isActive = this.isActive,
            lastLogin = this.lastLogin
        )
    }

    private fun User.toEntity(): UserEntity {
        return UserEntity(
            id = this.id,
            username = this.username,
            passwordHash = "", // Password tidak diubah dari sini
            fullName = this.fullName,
            email = this.email,
            phone = this.phone,
            role = UserRole.valueOf(this.role),
            isActive = this.isActive,
            lastLogin = this.lastLogin
        )
    }
}
