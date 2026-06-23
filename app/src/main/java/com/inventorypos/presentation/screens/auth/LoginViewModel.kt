package com.inventorypos.presentation.screens.auth

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    // private val authRepository: AuthRepository
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
            
            try {
                // TODO: Call authRepository.login(username, password)
                // Simulasi delay
                kotlinx.coroutines.delay(1500)
                
                if (_username.value == "admin" && _password.value == "admin") {
                    _isSuccess.value = true
                } else {
                    _error.value = "Invalid username or password"
                }
            } catch (e: Exception) {
                _error.value = e.message ?: "Login failed"
            } finally {
                _isLoading.value = false
            }
        }
    }
    
    fun loginWithBiometric() {
        // TODO: Implement biometric authentication
        viewModelScope.launch {
            _isLoading.value = true
            kotlinx.coroutines.delay(1000)
            _isSuccess.value = true
            _isLoading.value = false
        }
    }
}
