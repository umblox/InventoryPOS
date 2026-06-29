package com.inventorypos.presentation.screens.inventory.product

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.inventorypos.domain.model.Category
import com.inventorypos.domain.model.Product
import com.inventorypos.domain.repository.CategoryRepository
import com.inventorypos.domain.repository.ProductRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProductAddViewModel @Inject constructor(
    private val productRepository: ProductRepository,
    private val categoryRepository: CategoryRepository // MENGINJEKSI CATEGORY REPOSITORY
) : ViewModel() {
    
    // Menarik daftar kategori asli dari database secara reaktif
    val categories: StateFlow<List<Category>> = categoryRepository.getAllCategories()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    private val _name = MutableStateFlow("")
    val name: StateFlow<String> = _name
    
    private val _sku = MutableStateFlow("")
    val sku: StateFlow<String> = _sku
    
    private val _categoryId = MutableStateFlow<Long?>(null)
    val categoryId: StateFlow<Long?> = _categoryId
    
    private val _buyPrice = MutableStateFlow("")
    val buyPrice: StateFlow<String> = _buyPrice
    
    private val _sellPrice = MutableStateFlow("")
    val sellPrice: StateFlow<String> = _sellPrice
    
    private val _stock = MutableStateFlow("")
    val stock: StateFlow<String> = _stock
    
    private val _minStock = MutableStateFlow("5")
    val minStock: StateFlow<String> = _minStock
    
    private val _description = MutableStateFlow("")
    val description: StateFlow<String> = _description
    
    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading
    
    private val _isSuccess = MutableStateFlow(false)
    val isSuccess: StateFlow<Boolean> = _isSuccess
    
    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error
    
    fun onNameChange(value: String) { _name.value = value; _error.value = null }
    fun onSkuChange(value: String) { _sku.value = value; _error.value = null }
    fun onCategoryChange(value: Long?) { _categoryId.value = value; _error.value = null }
    fun onBuyPriceChange(value: String) { _buyPrice.value = value; _error.value = null }
    fun onSellPriceChange(value: String) { _sellPrice.value = value; _error.value = null }
    fun onStockChange(value: String) { _stock.value = value; _error.value = null }
    fun onMinStockChange(value: String) { _minStock.value = value; _error.value = null }
    fun onDescriptionChange(value: String) { _description.value = value; _error.value = null }
    fun onImageSelected(uri: String?) { /* TODO */ }
    
    fun saveProduct() {
        when {
            _name.value.isBlank() -> { _error.value = "Product name is required"; return }
            _sku.value.isBlank() -> { _error.value = "SKU is required"; return }
            _buyPrice.value.isBlank() -> { _error.value = "Buy price is required"; return }
            _sellPrice.value.isBlank() -> { _error.value = "Sell price is required"; return }
            _stock.value.isBlank() -> { _error.value = "Stock is required"; return }
        }
        
        val buyPrice = _buyPrice.value.toDoubleOrNull()
        val sellPrice = _sellPrice.value.toDoubleOrNull()
        val stock = _stock.value.toIntOrNull()
        val minStock = _minStock.value.toIntOrNull() ?: 5
        
        if (buyPrice == null || sellPrice == null || stock == null) {
            _error.value = "Please enter valid numbers"
            return
        }
        
        viewModelScope.launch {
            _isLoading.value = true
            _error.value = null
            
            try {
                val product = Product(
                    name = _name.value,
                    sku = _sku.value,
                    categoryId = _categoryId.value,
                    description = _description.value,
                    buyPrice = buyPrice,
                    sellPrice = sellPrice,
                    stock = stock,
                    minStock = minStock,
                    isActive = true // Memastikan produk aktif saat dibuat
                )
                productRepository.addProduct(product)
                _isSuccess.value = true
            } catch (e: Exception) {
                _error.value = e.message ?: "Failed to save product"
            } finally {
                _isLoading.value = false
            }
        }
    }
}
