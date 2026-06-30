package com.inventorypos.presentation.screens.inventory.stock

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.inventorypos.data.local.entity.StockLogEntity
import com.inventorypos.data.local.entity.StockLogType
import com.inventorypos.domain.model.Product
import com.inventorypos.domain.repository.ProductRepository
import com.inventorypos.domain.repository.StockRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import java.util.Date
import javax.inject.Inject

@OptIn(FlowPreview::class)
@HiltViewModel
class StockInViewModel @Inject constructor(
    private val productRepository: ProductRepository,
    private val stockRepository: StockRepository
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
    fun clearSelection() { _selectedProduct.value = null; _quantity.value = ""; _notes.value = "" }
    
    fun onQuantityChange(value: String) { _quantity.value = value; _error.value = null }
    fun onNotesChange(value: String) { _notes.value = value }

    fun confirmStockIn() {
        val product = _selectedProduct.value
        if (product == null) { _error.value = "Please select a product"; return }
        
        val qty = _quantity.value.toIntOrNull()
        if (qty == null || qty <= 0) { _error.value = "Valid quantity is required"; return }

        viewModelScope.launch {
            _isLoading.value = true
            try {
                // 1. Update stok produk
                val updatedProduct = product.copy(stock = product.stock + qty)
                productRepository.updateProduct(updatedProduct)
                
                // 2. Insert log stok masuk
                stockRepository.insertLog(
                    StockLogEntity(
                        productId = product.id,
                        type = StockLogType.IN,
                        quantity = qty,
                        previousStock = product.stock,
                        newStock = product.stock + qty,
                        reference = "Stock In",
                        notes = _notes.value.ifBlank { null },
                        userId = 0, // TODO: Ambil dari user login
                        createdAt = Date()
                    )
                )
                
                _isSuccess.value = true
            } catch (e: Exception) {
                _error.value = e.message ?: "Failed to update stock"
            } finally {
                _isLoading.value = false
            }
        }
    }
}
