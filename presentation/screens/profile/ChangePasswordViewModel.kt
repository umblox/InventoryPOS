package com.inventorypos.presentation.screens.profile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ChangePasswordViewModel @Inject constructor() : ViewModel() {
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
        when {
            _currentPassword.value.isBlank() -> { _error.value = "Current password is required"; return }
            _newPassword.value.length < 6 -> { _error.value = "New password min 6 characters"; return }
            _newPassword.value != _confirmPassword.value -> { _error.value = "Passwords do not match"; return }
        }
        viewModelScope.launch {
            _isLoading.value = true
            kotlinx.coroutines.delay(1000)
            _isSuccess.value = true
            _isLoading.value = false
        }
    }
}
