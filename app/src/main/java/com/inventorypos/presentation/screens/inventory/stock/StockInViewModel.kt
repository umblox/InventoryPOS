package com.inventorypos.presentation.screens.inventory.stock

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.inventorypos.domain.repository.ProductRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class StockInViewModel @Inject constructor(
    private val productRepository: ProductRepository // Injeksi Repository
) : ViewModel() {
    private val _productSearch = MutableStateFlow("")
    val productSearch: StateFlow<String> = _productSearch

    private val _quantity = MutableStateFlow("")
    val quantity: StateFlow<String> = _quantity

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
    fun onNotesChange(value: String) { _notes.value = value }

    fun confirmStockIn() {
        if (_productSearch.value.isBlank()) { _error.value = "Product is required"; return }
        val qty = _quantity.value.toIntOrNull()
        if (qty == null || qty <= 0) {
            _error.value = "Valid quantity is required"; return
        }

        viewModelScope.launch {
            _isLoading.value = true
            try {
                // LOGIKA NYATA: Ambil produk berdasarkan nama/sku, lalu tambah stok
                val product = productRepository.getProductByName(_productSearch.value)
                if (product != null) {
                    productRepository.updateStock(product.id, product.stock + qty)
                    _isSuccess.value = true
                } else {
                    _error.value = "Product not found"
                }
            } catch (e: Exception) {
                _error.value = "Failed to update stock"
            } finally {
                _isLoading.value = false
            }
        }
    }
}
