package com.inventorypos.presentation.screens.inventory.stock

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.inventorypos.domain.model.Product
import com.inventorypos.domain.repository.ProductRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@OptIn(FlowPreview::class)
@HiltViewModel
class StockTransferViewModel @Inject constructor(
    private val productRepository: ProductRepository
) : ViewModel() {

    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery

    val searchResults: StateFlow<List<Product>> = _searchQuery
        .debounce(300)
        .flatMapLatest { query ->
            if (query.length > 2) {
                productRepository.getAllProducts().map { list ->
                    list.filter { it.name.contains(query, ignoreCase = true) || it.sku.contains(query, ignoreCase = true) }
                }
            } else flowOf(emptyList())
        }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    private val _selectedProduct = MutableStateFlow<Product?>(null)
    val selectedProduct: StateFlow<Product?> = _selectedProduct

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

    fun onSearchQueryChange(value: String) { _searchQuery.value = value; _error.value = null }
    fun selectProduct(product: Product) { _selectedProduct.value = product; _searchQuery.value = "" }
    fun clearSelection() { 
        _selectedProduct.value = null
        _quantity.value = ""
        _fromLocation.value = ""
        _toLocation.value = ""
        _notes.value = "" 
    }

    fun onQuantityChange(value: String) { _quantity.value = value; _error.value = null }
    fun onFromLocationChange(value: String) { _fromLocation.value = value; _error.value = null }
    fun onToLocationChange(value: String) { _toLocation.value = value; _error.value = null }
    fun onNotesChange(value: String) { _notes.value = value }

    fun confirmTransfer() {
        val product = _selectedProduct.value
        if (product == null) { _error.value = "Please select a product"; return }

        val qty = _quantity.value.toIntOrNull()
        if (qty == null || qty <= 0) { _error.value = "Valid quantity is required"; return }
        if (product.stock < qty) { _error.value = "Insufficient stock to transfer"; return }

        if (_fromLocation.value.isBlank()) { _error.value = "From location is required"; return }
        if (_toLocation.value.isBlank()) { _error.value = "To location is required"; return }
        if (_fromLocation.value == _toLocation.value) { _error.value = "Locations must be different"; return }

        viewModelScope.launch {
            _isLoading.value = true
            try {
                // Total stok aplikasi tetap sama (hanya pindah rak/gudang). 
                // TODO: Saat Multi-Branch diaktifkan, kita akan mengurangi stok di Location A dan menambah di Location B.
                
                _isSuccess.value = true
            } catch (e: Exception) {
                _error.value = e.message ?: "Failed to transfer stock"
            } finally {
                _isLoading.value = false
            }
        }
    }
}
