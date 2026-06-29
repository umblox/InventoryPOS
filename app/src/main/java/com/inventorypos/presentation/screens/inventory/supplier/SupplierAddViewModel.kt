package com.inventorypos.presentation.screens.inventory.supplier

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.inventorypos.domain.model.Supplier
import com.inventorypos.domain.repository.SupplierRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SupplierAddViewModel @Inject constructor(
    private val supplierRepository: SupplierRepository // Injeksi Repository
) : ViewModel() {
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

    private val _isSuccess = MutableStateFlow(false)
    val isSuccess: StateFlow<Boolean> = _isSuccess

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error

    fun onNameChange(value: String) { _name.value = value; _error.value = null }
    fun onPhoneChange(value: String) { _phone.value = value }
    fun onEmailChange(value: String) { _email.value = value }
    fun onAddressChange(value: String) { _address.value = value }

    fun saveSupplier() {
        if (_name.value.isBlank()) { _error.value = "Name is required"; return }
        
        viewModelScope.launch {
            _isLoading.value = true
            try {
                val newSupplier = Supplier(
                    name = _name.value,
                    phone = _phone.value.ifBlank { null },
                    email = _email.value.ifBlank { null },
                    address = _address.value.ifBlank { null },
                    isActive = true
                )
                supplierRepository.addSupplier(newSupplier)
                _isSuccess.value = true
            } catch (e: Exception) {
                _error.value = e.message ?: "Failed to add supplier"
            } finally {
                _isLoading.value = false
            }
        }
    }
}
