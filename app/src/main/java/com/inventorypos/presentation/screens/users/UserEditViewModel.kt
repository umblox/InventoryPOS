package com.inventorypos.presentation.screens.users

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class UserEditViewModel @Inject constructor() : ViewModel() {
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

    fun loadUser(id: Long) {
        viewModelScope.launch {
            _isLoading.value = true
            kotlinx.coroutines.delay(300)
            _fullName.value = "Kasir Satu"
            _username.value = "kasir1"
            _role.value = "CASHIER"
            _isLoading.value = false
        }
    }

    fun onFullNameChange(value: String) { _fullName.value = value }
    fun onUsernameChange(value: String) { _username.value = value }
    fun onRoleChange(value: String) { _role.value = value }

    fun updateUser(id: Long) {
        if (_fullName.value.isBlank()) { _error.value = "Full name is required"; return }
        if (_username.value.isBlank()) { _error.value = "Username is required"; return }
        viewModelScope.launch {
            _isSaving.value = true
            kotlinx.coroutines.delay(800)
            _isSuccess.value = true
            _isSaving.value = false
        }
    }
}
