package com.inventorypos.presentation.screens.users

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.inventorypos.data.local.dao.UserDao
import com.inventorypos.data.local.entity.UserEntity
import com.inventorypos.data.local.entity.UserRole
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class UserEditViewModel @Inject constructor(
    private val userDao: UserDao
) : ViewModel() {
    
    private val _fullName = MutableStateFlow("")
    val fullName: StateFlow<String> = _fullName

    private val _username = MutableStateFlow("")
    val username: StateFlow<String> = _username

    private val _role = MutableStateFlow("")
    val role: StateFlow<String> = _role

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    private val _isSaving = MutableStateFlow(false)
    val isSaving: StateFlow<Boolean> = _isSaving

    private val _isSuccess = MutableStateFlow(false)
    val isSuccess: StateFlow<Boolean> = _isSuccess

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error

    // Menyimpan data asli agar field lain (seperti password) tidak hilang saat disimpan ulang
    private var currentUser: UserEntity? = null

    // 1. Memuat User dari Database asli
    fun loadUser(id: Long) {
        viewModelScope.launch {
            _isLoading.value = true
            _error.value = null
            try {
                val user = userDao.getById(id)
                if (user != null) {
                    currentUser = user
                    _fullName.value = user.fullName
                    _username.value = user.username
                    _role.value = user.role.name
                } else {
                    _error.value = "Data karyawan tidak ditemukan"
                }
            } catch (e: Exception) {
                _error.value = e.message ?: "Gagal memuat data"
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun onFullNameChange(value: String) { _fullName.value = value; _error.value = null }
    fun onUsernameChange(value: String) { _username.value = value; _error.value = null }
    fun onRoleChange(value: String) { _role.value = value; _error.value = null }

    // 2. Mengupdate profil lengkap User ke Database
    fun updateUser(id: Long) {
        if (_fullName.value.isBlank()) { _error.value = "Full name is required"; return }
        if (_username.value.isBlank()) { _error.value = "Username is required"; return }
        
        viewModelScope.launch {
            _isSaving.value = true
            _error.value = null
            
            try {
                if (currentUser != null) {
                    // Konversi string kembali ke Enum UserRole
                    val roleEnum = try {
                        UserRole.valueOf(_role.value)
                    } catch (e: Exception) {
                        UserRole.EMPLOYEE // Default jika terjadi kesalahan
                    }
                    
                    val updatedUser = currentUser!!.copy(
                        fullName = _fullName.value,
                        username = _username.value,
                        role = roleEnum
                    )
                    
                    userDao.update(updatedUser)
                    _isSuccess.value = true
                } else {
                    _error.value = "Sistem gagal mengidentifikasi user asli"
                }
            } catch (e: Exception) {
                _error.value = e.message ?: "Gagal memperbarui user"
            } finally {
                _isSaving.value = false
            }
        }
    }

    // 3. Fungsi khusus (Sesuai Permintaan) untuk mengubah jabatan/Role saja dengan cepat
    fun updateUserRole(userId: Long, newRole: UserRole) {
        viewModelScope.launch {
            _isLoading.value = true
            _error.value = null
            try {
                val user = userDao.getById(userId)
                if (user != null) {
                    val updated = user.copy(role = newRole)
                    userDao.update(updated)
                    
                    // Jika user yang diubah jabatannya adalah user yang sedang dibuka formnya:
                    if (currentUser?.id == userId) {
                        currentUser = updated
                        _role.value = newRole.name
                    }
                } else {
                    _error.value = "Karyawan tidak ditemukan di sistem!"
                }
            } catch (e: Exception) {
                _error.value = e.message ?: "Gagal mengubah hak akses karyawan"
            } finally {
                _isLoading.value = false
            }
        }
    }
}
