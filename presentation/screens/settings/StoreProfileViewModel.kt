package com.inventorypos.presentation.screens.settings

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class StoreProfileViewModel @Inject constructor() : ViewModel() {
    private val _storeName = MutableStateFlow("")
    val storeName: StateFlow<String> = _storeName

    private val _address = MutableStateFlow("")
    val address: StateFlow<String> = _address

    private val _phone = MutableStateFlow("")
    val phone: StateFlow<String> = _phone

    private val _taxRate = MutableStateFlow("10")
    val taxRate: StateFlow<String> = _taxRate

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    private val _isSuccess = MutableStateFlow(false)
    val isSuccess: StateFlow<Boolean> = _isSuccess

    fun onStoreNameChange(value: String) { _storeName.value = value }
    fun onAddressChange(value: String) { _address.value = value }
    fun onPhoneChange(value: String) { _phone.value = value }
    fun onTaxRateChange(value: String) { _taxRate.value = value }

    fun saveProfile() {
        viewModelScope.launch {
            _isLoading.value = true
            kotlinx.coroutines.delay(800)
            _isSuccess.value = true
            _isLoading.value = false
        }
    }
}
