package com.inventorypos.presentation.screens.profile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.inventorypos.data.local.dao.UserDao
import com.inventorypos.data.preferences.AuthPreferences
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ChangePasswordViewModel @Inject constructor(
    private val userDao: UserDao,
    private val authPreferences: AuthPreferences
) : ViewModel() {

    private val _currentPassword = MutableStateFlow("")
    val currentPassword: StateFlow<String> = _currentPassword

    private val _newPassword = MutableStateFlow("")
    val newPassword: StateFlow<String> = _newPassword

    private val _confirmPassword = MutableStateFlow("")
    val confirmPassword: StateFlow<String> = _confirmPassword

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    private val _isSuccess = MutableStateFlow(false)
    val isSuccess: StateFlow<Boolean> = _isSuccess

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error

    fun onCurrentPasswordChange(value: String) { _currentPassword.value = value; _error.value = null }
    fun onNewPasswordChange(value: String) { _newPassword.value = value; _error.value = null }
    fun onConfirmPasswordChange(value: String) { _confirmPassword.value = value; _error.value = null }

    fun changePassword() {
        // Validasi dasar
        if (_currentPassword.value.isBlank() || _newPassword.value.isBlank() || _confirmPassword.value.isBlank()) {
            _error.value = "Semua kolom wajib diisi"
            return
        }
        if (_newPassword.value != _confirmPassword.value) {
            _error.value = "Password baru dan konfirmasi tidak cocok!"
            return
        }
        if (_newPassword.value.length < 6) {
            _error.value = "Password baru minimal 6 karakter!"
            return
        }

        // Simpan ke database
        viewModelScope.launch {
            _isLoading.value = true
            _error.value = null

            try {
                // Tarik ID user yang sedang login dari DataStore (AuthPreferences)
                val userId = authPreferences.loggedInUserId.first() 
                
                if (userId != -1L) {
                    val user = userDao.getById(userId)
                    
                    if (user != null) {
                        // Cek apakah password lama yang dimasukkan sudah benar
                        if (user.passwordHash == _currentPassword.value) {
                            // Update password di database
                            val updatedUser = user.copy(passwordHash = _newPassword.value)
                            userDao.update(updatedUser)
                            
                            _isSuccess.value = true
                        } else {
                            _error.value = "Password saat ini (lama) salah!"
                        }
                    } else {
                        _error.value = "Data user tidak ditemukan di database."
                    }
                } else {
                    _error.value = "Sesi login tidak valid, silakan login ulang."
                }
            } catch (e: Exception) {
                _error.value = e.message ?: "Gagal mengubah password"
            } finally {
                _isLoading.value = false
            }
        }
    }
}
