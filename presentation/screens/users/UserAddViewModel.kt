package com.inventorypos.presentation.screens.users

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class UserAddViewModel @Inject constructor() : ViewModel() {
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
            kotlinx.coroutines.delay(800)
            _isSuccess.value = true
            _isLoading.value = false
        }
    }
}
