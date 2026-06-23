package com.inventorypos.presentation.screens.inventory.supplier

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SupplierListViewModel @Inject constructor() : ViewModel() {
    private val _suppliers = MutableStateFlow<List<Supplier>>(emptyList())
    val suppliers: StateFlow<List<Supplier>> = _suppliers

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    init {
        loadSuppliers()
    }

    private fun loadSuppliers() {
        viewModelScope.launch {
            _isLoading.value = true
            _suppliers.value = listOf(
                Supplier(1, "PT. Indah Jaya", "08123456789", "indah@jaya.com", "Jakarta"),
                Supplier(2, "CV. Makmur Sejahtera", "08234567890", null, "Bandung")
            )
            _isLoading.value = false
        }
    }
}
