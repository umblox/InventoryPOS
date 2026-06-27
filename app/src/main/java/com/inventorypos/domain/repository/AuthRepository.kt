interface AuthRepository {
    suspend fun login(username: String, password: String): Result<Unit>
    suspend fun register(fullName: String, username: String, password: String): Result<Unit>
    suspend fun resetPassword(username: String, newPassword: String): Result<Unit> // Tambahkan ini
    suspend fun logout()
    fun isUserLoggedIn(): Flow<Boolean>
}
