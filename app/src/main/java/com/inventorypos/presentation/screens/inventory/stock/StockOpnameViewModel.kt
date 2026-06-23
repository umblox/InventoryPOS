package com.inventorypos.presentation.screens.inventory.stock

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class StockOpnameViewModel @Inject constructor() : ViewModel() {
    private val _productSearch = MutableStateFlow("")
    val productSearch: StateFlow<String> = _productSearch

    private val _systemStock = MutableStateFlow("0")
    val systemStock: StateFlow<String> = _systemStock

    private val _physicalStock = MutableStateFlow("")
    val physicalStock: StateFlow<String> = _physicalStock

    private val _notes = MutableStateFlow("")
    val notes: StateFlow<String> = _notes

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    private val _isSuccess = MutableStateFlow(false)
    val isSuccess: StateFlow<Boolean> = _isSuccess

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error

    fun onProductSearchChange(value: String) {
        _productSearch.value = value
        _error.value = null
        // Simulate loading system stock
        if (value.length > 2) {
            _systemStock.value = "25"
        }
    }

    fun onPhysicalStockChange(value: String) { _physicalStock.value = value; _error.value = null }
    fun onNotesChange(value: String) { _notes.value = value }

    fun confirmOpname() {
        if (_productSearch.value.isBlank()) { _error.value = "Product is required"; return }
        if (_physicalStock.value.isBlank() || _physicalStock.value.toIntOrNull() == null) {
            _error.value = "Valid physical stock is required"; return
        }
        viewModelScope.launch {
            _isLoading.value = true
            kotlinx.coroutines.delay(800)
            _isSuccess.value = true
            _isLoading.value = false
        }
    }
}
