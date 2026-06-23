package com.inventorypos.presentation.screens.auth

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RegisterViewModel @Inject constructor() : ViewModel() {
    
    private val _fullName = MutableStateFlow("")
    val fullName: StateFlow<String> = _fullName
    
    private val _username = MutableStateFlow("")
    val username: StateFlow<String> = _username
    
    private val _email = MutableStateFlow("")
    val email: StateFlow<String> = _email
    
    private val _password = MutableStateFlow("")
    val password: StateFlow<String> = _password
    
    private val _confirmPassword = MutableStateFlow("")
    val confirmPassword: StateFlow<String> = _confirmPassword
    
    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading
    
    private val _isSuccess = MutableStateFlow(false)
    val isSuccess: StateFlow<Boolean> = _isSuccess
    
    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error
    
    fun onFullNameChange(value: String) { _fullName.value = value; _error.value = null }
    fun onUsernameChange(value: String) { _username.value = value; _error.value = null }
    fun onEmailChange(value: String) { _email.value = value; _error.value = null }
    fun onPasswordChange(value: String) { _password.value = value; _error.value = null }
    fun onConfirmPasswordChange(value: String) { _confirmPassword.value = value; _error.value = null }
    
    fun register() {
        when {
            _fullName.value.isBlank() -> { _error.value = "Full name is required"; return }
            _username.value.isBlank() -> { _error.value = "Username is required"; return }
            _email.value.isBlank() -> { _error.value = "Email is required"; return }
            _password.value.length < 6 -> { _error.value = "Password must be at least 6 characters"; return }
            _password.value != _confirmPassword.value -> { _error.value = "Passwords do not match"; return }
        }
        
        viewModelScope.launch {
            _isLoading.value = true
            _error.value = null
            
            try {
                kotlinx.coroutines.delay(1500)
                _isSuccess.value = true
            } catch (e: Exception) {
                _error.value = e.message ?: "Registration failed"
            } finally {
                _isLoading.value = false
            }
        }
    }
}
