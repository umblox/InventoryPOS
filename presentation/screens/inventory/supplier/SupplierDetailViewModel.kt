package com.inventorypos.presentation.screens.inventory.supplier

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SupplierDetailViewModel @Inject constructor() : ViewModel() {
    private val _supplier = MutableStateFlow<Supplier?>(null)
    val supplier: StateFlow<Supplier?> = _supplier

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    fun loadSupplier(id: Long) {
        viewModelScope.launch {
            _isLoading.value = true
            kotlinx.coroutines.delay(300)
            _supplier.value = Supplier(id, "PT. Indah Jaya", "08123456789", "indah@jaya.com", "Jakarta")
            _isLoading.value = false
        }
    }
}
