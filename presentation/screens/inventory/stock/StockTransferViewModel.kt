package com.inventorypos.presentation.screens.inventory.stock

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class StockTransferViewModel @Inject constructor() : ViewModel() {
    private val _productSearch = MutableStateFlow("")
    val productSearch: StateFlow<String> = _productSearch

    private val _quantity = MutableStateFlow("")
    val quantity: StateFlow<String> = _quantity

    private val _fromLocation = MutableStateFlow("")
    val fromLocation: StateFlow<String> = _fromLocation

    private val _toLocation = MutableStateFlow("")
    val toLocation: StateFlow<String> = _toLocation

    private val _notes = MutableStateFlow("")
    val notes: StateFlow<String> = _notes

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    private val _isSuccess = MutableStateFlow(false)
    val isSuccess: StateFlow<Boolean> = _isSuccess

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error

    fun onProductSearchChange(value: String) { _productSearch.value = value; _error.value = null }
    fun onQuantityChange(value: String) { _quantity.value = value; _error.value = null }
    fun onFromLocationChange(value: String) { _fromLocation.value = value; _error.value = null }
    fun onToLocationChange(value: String) { _toLocation.value = value; _error.value = null }
    fun onNotesChange(value: String) { _notes.value = value }

    fun confirmTransfer() {
        if (_productSearch.value.isBlank()) { _error.value = "Product is required"; return }
        if (_quantity.value.isBlank() || _quantity.value.toIntOrNull() == null || _quantity.value.toInt() <= 0) {
            _error.value = "Valid quantity is required"; return
        }
        if (_fromLocation.value.isBlank()) { _error.value = "From location is required"; return }
        if (_toLocation.value.isBlank()) { _error.value = "To location is required"; return }
        if (_fromLocation.value == _toLocation.value) { _error.value = "Locations must be different"; return }
        viewModelScope.launch {
            _isLoading.value = true
            kotlinx.coroutines.delay(800)
            _isSuccess.value = true
            _isLoading.value = false
        }
    }
}
