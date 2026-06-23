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
class CustomerListViewModel @Inject constructor() : ViewModel() {
    private val _customers = MutableStateFlow<List<Customer>>(emptyList())
    val customers: StateFlow<List<Customer>> = _customers

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    init {
        loadCustomers()
    }

    private fun loadCustomers() {
        viewModelScope.launch {
            _isLoading.value = true
            _customers.value = listOf(
                Customer(1, "Budi Santoso", "08123456789", "budi@email.com", "Jakarta", 150, 2500000.0),
                Customer(2, "Ani Wijaya", "08234567890", null, "Bandung", 80, 1200000.0),
                Customer(3, "Citra Lestari", "08345678901", "citra@email.com", "Surabaya", 200, 3500000.0)
            )
            _isLoading.value = false
        }
    }
}
