package com.inventorypos.presentation.screens.users

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.inventorypos.data.local.dao.UserDao
import com.inventorypos.data.local.dao.UserPermissionDao
import com.inventorypos.data.local.entity.UserEntity
import com.inventorypos.data.local.entity.UserPermissionEntity
import com.inventorypos.data.local.entity.UserRole
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class UserAddViewModel @Inject constructor(
    private val userDao: UserDao,
    private val userPermissionDao: UserPermissionDao // Injeksi DAO Permission
) : ViewModel() {
    private val _fullName = MutableStateFlow("")
    val fullName: StateFlow<String> = _fullName

    private val _username = MutableStateFlow("")
    val username: StateFlow<String> = _username

    private val _password = MutableStateFlow("")
    val password: StateFlow<String> = _password

    private val _role = MutableStateFlow("CASHIER")
    val role: StateFlow<String> = _role

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    private val _isSuccess = MutableStateFlow(false)
    val isSuccess: StateFlow<Boolean> = _isSuccess

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error

    fun onFullNameChange(value: String) { _fullName.value = value; _error.value = null }
    fun onUsernameChange(value: String) { _username.value = value; _error.value = null }
    fun onPasswordChange(value: String) { _password.value = value; _error.value = null }
    fun onRoleChange(value: String) { _role.value = value; _error.value = null }

    fun saveUser() {
        if (_fullName.value.isBlank()) { _error.value = "Full name is required"; return }
        if (_username.value.isBlank()) { _error.value = "Username is required"; return }
        if (_password.value.length < 6) { _error.value = "Password min 6 characters"; return }
        
        viewModelScope.launch {
            _isLoading.value = true
            _error.value = null
            try {
                // 1. Cek apakah username sudah ada
                val existing = userDao.getByUsername(_username.value)
                if (existing != null) {
                    _error.value = "Username sudah digunakan!"
                    return@launch
                }

                val roleEnum = try { UserRole.valueOf(_role.value) } catch (e: Exception) { UserRole.CASHIER }

                // 2. Simpan User Baru
                val newUser = UserEntity(
                    fullName = _fullName.value,
                    username = _username.value,
                    passwordHash = _password.value,
                    role = roleEnum,
                    isActive = true
                )
                userDao.insert(newUser)
                
                // 3. SEEDER PERMISSION OTOMATIS: 
                // Kita tarik ulang ID user yang baru saja dibuat
                val savedUser = userDao.getByUsername(_username.value)
                if (savedUser != null) {
                    val defaultPerms = listOf("View Dashboard", "Manage POS", "Manage Products", "Manage Stock", "View Reports", "Manage Users", "Manage Settings")
                    
                    val permissionEntities = defaultPerms.map { permName ->
                        // Aturan Default: Kasir hanya boleh POS dan Dashboard. Admin boleh yang lain.
                        val isGrantedByDefault = when (roleEnum) {
                            UserRole.ADMIN -> true
                            UserRole.CASHIER -> permName == "Manage POS" || permName == "View Dashboard"
                            UserRole.WAREHOUSE -> permName == "Manage Stock" || permName == "Manage Products" || permName == "View Dashboard"
                            else -> false
                        }
                        
                        UserPermissionEntity(
                            userId = savedUser.id,
                            permissionName = permName,
                            isGranted = isGrantedByDefault
                        )
                    }
                    // Simpan izin ke database
                    userPermissionDao.insertPermissions(permissionEntities)
                }
                
                _isSuccess.value = true
            } catch (e: Exception) {
                _error.value = e.message ?: "Failed to save user"
            } finally {
                _isLoading.value = false
            }
        }
    }
}
