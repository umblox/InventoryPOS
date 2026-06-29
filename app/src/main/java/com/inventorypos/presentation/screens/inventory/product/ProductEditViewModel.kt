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
class ProductEditViewModel @Inject constructor(
    private val productRepository: ProductRepository,
    private val categoryRepository: CategoryRepository // TAMBAHAN: Injeksi Kategori
) : ViewModel() {
    
    // TAMBAHAN: Mengambil list kategori untuk dropdown edit
    val categories: StateFlow<List<Category>> = categoryRepository.getAllCategories()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    private val _product = MutableStateFlow<Product?>(null)
    val product: StateFlow<Product?> = _product
    
    val name = MutableStateFlow("")
    val sku = MutableStateFlow("")
    val categoryId = MutableStateFlow<Long?>(null)
    val buyPrice = MutableStateFlow("")
    val sellPrice = MutableStateFlow("")
    val stock = MutableStateFlow("")
    val minStock = MutableStateFlow("5")
    val description = MutableStateFlow("")
    
    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading
    
    private val _isSaving = MutableStateFlow(false)
    val isSaving: StateFlow<Boolean> = _isSaving
    
    private val _isSuccess = MutableStateFlow(false)
    val isSuccess: StateFlow<Boolean> = _isSuccess
    
    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error
    
    fun loadProduct(id: Long) {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                productRepository.getProductById(id)?.let { p ->
                    _product.value = p
                    name.value = p.name
                    sku.value = p.sku
                    categoryId.value = p.categoryId
                    buyPrice.value = p.buyPrice.toString()
                    sellPrice.value = p.sellPrice.toString()
                    stock.value = p.stock.toString()
                    minStock.value = p.minStock.toString()
                    description.value = p.description ?: ""
                }
            } catch (e: Exception) {
                _error.value = e.message
            } finally {
                _isLoading.value = false
            }
        }
    }
    
    fun onNameChange(value: String) { name.value = value; _error.value = null }
    fun onSkuChange(value: String) { sku.value = value; _error.value = null }
    fun onCategoryChange(value: Long?) { categoryId.value = value; _error.value = null }
    fun onBuyPriceChange(value: String) { buyPrice.value = value; _error.value = null }
    fun onSellPriceChange(value: String) { sellPrice.value = value; _error.value = null }
    fun onStockChange(value: String) { stock.value = value; _error.value = null }
    fun onMinStockChange(value: String) { minStock.value = value; _error.value = null }
    fun onDescriptionChange(value: String) { description.value = value; _error.value = null }
    fun onImageSelected(uri: String?) { /* TODO */ }
    
    fun updateProduct(id: Long) {
        val buy = buyPrice.value.toDoubleOrNull()
        val sell = sellPrice.value.toDoubleOrNull()
        val stk = stock.value.toIntOrNull()
        val min = minStock.value.toIntOrNull() ?: 5
        
        if (name.value.isBlank() || sku.value.isBlank()) {
            _error.value = "Name and SKU cannot be empty"
            return
        }
        
        if (buy == null || sell == null || stk == null) {
            _error.value = "Prices and Stock must be valid numbers"
            return
        }
        
        viewModelScope.launch {
            _isSaving.value = true
            _error.value = null
            try {
                val updated = Product(
                    id = id,
                    name = name.value,
                    sku = sku.value,
                    categoryId = categoryId.value,
                    description = description.value,
                    buyPrice = buy,
                    sellPrice = sell,
                    stock = stk,
                    minStock = min,
                    imageUrl = _product.value?.imageUrl,
                    isActive = true
                )
                productRepository.updateProduct(updated)
                _isSuccess.value = true
            } catch (e: Exception) {
                _error.value = e.message
            } finally {
                _isSaving.value = false
            }
        }
    }
}
