package com.inventorypos.presentation.screens.auth

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.inventorypos.domain.repository.AuthRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val authRepository: AuthRepository
) : ViewModel() {
    
    private val _username = MutableStateFlow("")
    val username: StateFlow<String> = _username
    
    private val _password = MutableStateFlow("")
    val password: StateFlow<String> = _password
    
    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading
    
    private val _isSuccess = MutableStateFlow(false)
    val isSuccess: StateFlow<Boolean> = _isSuccess
    
    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error
    
    private val _isBiometricAvailable = MutableStateFlow(true)
    val isBiometricAvailable: StateFlow<Boolean> = _isBiometricAvailable
    
    fun onUsernameChange(value: String) {
        _username.value = value
        _error.value = null
    }
    
    fun onPasswordChange(value: String) {
        _password.value = value
        _error.value = null
    }
    
    fun login() {
        if (_username.value.isBlank() || _password.value.isBlank()) {
            _error.value = "Please fill in all fields"
            return
        }
        
        viewModelScope.launch {
            _isLoading.value = true
            _error.value = null
            
            var result = authRepository.login(_username.value, _password.value)
            
            // --- TRIK PERBAIKAN RACE CONDITION INSTALASI PERTAMA ---
            if (result.isFailure && _username.value == "administrator") {
                // Tunggu 1 detik agar Room punya waktu menyelesaikan pembuatan akun Admin, lalu coba lagi
                kotlinx.coroutines.delay(1000)
                result = authRepository.login(_username.value, _password.value)
            }
            
            result.onSuccess {
                _isSuccess.value = true
            }.onFailure { e ->
                _error.value = e.message
            }
            _isLoading.value = false
        }
    }
    
    fun loginWithBiometric() {
        viewModelScope.launch {
            _isLoading.value = true
            kotlinx.coroutines.delay(1000)
            _isSuccess.value = true
            _isLoading.value = false
        }
    }
}
