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
class StockAdjustmentViewModel @Inject constructor(
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

    private val _currentStock = MutableStateFlow("")
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

    fun onSearchQueryChange(value: String) { _searchQuery.value = value; _error.value = null }
    
    fun selectProduct(product: Product) { 
        _selectedProduct.value = product
        _currentStock.value = product.stock.toString() // Otomatis set stok saat ini
        _searchQuery.value = "" 
    }
    
    fun clearSelection() { 
        _selectedProduct.value = null
        _currentStock.value = ""
        _newStock.value = ""
        _reason.value = ""
        _notes.value = "" 
    }

    fun onNewStockChange(value: String) { _newStock.value = value; _error.value = null }
    fun onReasonChange(value: String) { _reason.value = value; _error.value = null }
    fun onNotesChange(value: String) { _notes.value = value }

    fun confirmAdjustment() {
        val product = _selectedProduct.value
        if (product == null) { _error.value = "Please select a product"; return }
        
        val newQty = _newStock.value.toIntOrNull()
        if (newQty == null || newQty < 0) { _error.value = "Valid new stock quantity is required"; return }
        
        if (newQty == product.stock) { _error.value = "New stock is exactly the same as current stock"; return }
        if (_reason.value.isBlank()) { _error.value = "Reason is required"; return }

        viewModelScope.launch {
            _isLoading.value = true
            try {
                // Update stok menjadi nilai mutlak yang baru (New Stock)
                val updatedProduct = product.copy(stock = newQty)
                productRepository.updateProduct(updatedProduct)
                
                // TODO: Log ke StockLogEntity (nanti)
                
                _isSuccess.value = true
            } catch (e: Exception) {
                _error.value = e.message ?: "Failed to adjust stock"
            } finally {
                _isLoading.value = false
            }
        }
    }
}
