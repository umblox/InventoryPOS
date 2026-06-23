package com.inventorypos.presentation.screens.customers

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CustomerEditViewModel @Inject constructor() : ViewModel() {
    private val _name = MutableStateFlow("")
    val name: StateFlow<String> = _name

    private val _phone = MutableStateFlow("")
    val phone: StateFlow<String> = _phone

    private val _email = MutableStateFlow("")
    val email: StateFlow<String> = _email

    private val _address = MutableStateFlow("")
    val address: StateFlow<String> = _address

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    private val _isSaving = MutableStateFlow(false)
    val isSaving: StateFlow<Boolean> = _isSaving

    private val _isSuccess = MutableStateFlow(false)
    val isSuccess: StateFlow<Boolean> = _isSuccess

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error

    fun loadCustomer(id: Long) {
        viewModelScope.launch {
            _isLoading.value = true
            kotlinx.coroutines.delay(300)
            _name.value = "Budi Santoso"
            _phone.value = "08123456789"
            _email.value = "budi@email.com"
            _address.value = "Jakarta"
            _isLoading.value = false
        }
    }

    fun onNameChange(value: String) { _name.value = value }
    fun onPhoneChange(value: String) { _phone.value = value }
    fun onEmailChange(value: String) { _email.value = value }
    fun onAddressChange(value: String) { _address.value = value }

    fun updateCustomer(id: Long) {
        if (_name.value.isBlank()) { _error.value = "Name is required"; return }
        viewModelScope.launch {
            _isSaving.value = true
            kotlinx.coroutines.delay(800)
            _isSuccess.value = true
            _isSaving.value = false
        }
    }
}
