package com.inventorypos.presentation.screens.customers

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.inventorypos.domain.model.Customer
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CustomerDeleteViewModel @Inject constructor() : ViewModel() {
    private val _customer = MutableStateFlow<Customer?>(null)
    val customer: StateFlow<Customer?> = _customer

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    private val _isDeleting = MutableStateFlow(false)
    val isDeleting: StateFlow<Boolean> = _isDeleting

    private val _isDeleted = MutableStateFlow(false)
    val isDeleted: StateFlow<Boolean> = _isDeleted

    fun loadCustomer(id: Long) {
        viewModelScope.launch {
            _isLoading.value = true
            kotlinx.coroutines.delay(300)
            _customer.value = Customer(id, "Budi Santoso", "08123456789", null, null, 150, 2500000.0)
            _isLoading.value = false
        }
    }

    fun deleteCustomer(id: Long) {
        viewModelScope.launch {
            _isDeleting.value = true
            kotlinx.coroutines.delay(800)
            _isDeleted.value = true
            _isDeleting.value = false
        }
    }
}
