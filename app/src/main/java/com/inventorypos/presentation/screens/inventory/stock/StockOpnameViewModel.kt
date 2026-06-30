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
class StockOpnameViewModel @Inject constructor(
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

    private val _systemStock = MutableStateFlow("")
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

    fun onSearchQueryChange(value: String) { _searchQuery.value = value; _error.value = null }
    
    fun selectProduct(product: Product) {
        _selectedProduct.value = product
        _systemStock.value = product.stock.toString()
        _searchQuery.value = ""
    }
    
    fun clearSelection() {
        _selectedProduct.value = null
        _systemStock.value = ""
        _physicalStock.value = ""
        _notes.value = ""
    }

    fun onPhysicalStockChange(value: String) { _physicalStock.value = value; _error.value = null }
    fun onNotesChange(value: String) { _notes.value = value }

    fun confirmOpname() {
        val product = _selectedProduct.value
        if (product == null) { _error.value = "Please select a product"; return }

        val physicalQty = _physicalStock.value.toIntOrNull()
        if (physicalQty == null || physicalQty < 0) { _error.value = "Valid physical stock is required"; return }

        viewModelScope.launch {
            _isLoading.value = true
            try {
                // 1. Update stok ke hasil fisik
                val updatedProduct = product.copy(stock = physicalQty)
                productRepository.updateProduct(updatedProduct)
                
                // 2. Insert log opname
                val diff = physicalQty - product.stock
                stockRepository.insertLog(
                    StockLogEntity(
                        productId = product.id,
                        type = StockLogType.OPNAME,
                        quantity = kotlin.math.abs(diff),
                        previousStock = product.stock,
                        newStock = physicalQty,
                        reference = "Stock Opname",
                        notes = _notes.value.ifBlank { null },
                        userId = 0, // TODO: Ambil dari user login
                        createdAt = Date()
                    )
                )
                
                _isSuccess.value = true
            } catch (e: Exception) {
                _error.value = e.message ?: "Failed to process opname"
            } finally {
                _isLoading.value = false
            }
        }
    }
}
