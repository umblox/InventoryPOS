package com.inventorypos.presentation.screens.inventory.stock

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class StockAdjustmentViewModel @Inject constructor() : ViewModel() {
    private val _productSearch = MutableStateFlow("")
    val productSearch: StateFlow<String> = _productSearch

    private val _currentStock = MutableStateFlow("0")
    val currentStock: StateFlow<String> = _currentStock

    private val _newStock = MutableStateFlow("")
    val newStock: StateFlow<String> = _newStock

    private val _reason = MutableStateFlow("")
    val reason: StateFlow<String> = _reason

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
        if (value.length > 2) _currentStock.value = "25"
    }

    fun onNewStockChange(value: String) { _newStock.value = value; _error.value = null }
    fun onReasonChange(value: String) { _reason.value = value; _error.value = null }
    fun onNotesChange(value: String) { _notes.value = value }

    fun confirmAdjustment() {
        if (_productSearch.value.isBlank()) { _error.value = "Product is required"; return }
        if (_newStock.value.isBlank() || _newStock.value.toIntOrNull() == null) {
            _error.value = "Valid new stock is required"; return
        }
        if (_reason.value.isBlank()) { _error.value = "Reason is required"; return }
        viewModelScope.launch {
            _isLoading.value = true
            kotlinx.coroutines.delay(800)
            _isSuccess.value = true
            _isLoading.value = false
        }
    }
}
