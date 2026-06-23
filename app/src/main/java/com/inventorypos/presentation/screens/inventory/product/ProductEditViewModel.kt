package com.inventorypos.presentation.screens.inventory.product

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.inventorypos.domain.model.Product
import com.inventorypos.domain.repository.ProductRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProductEditViewModel @Inject constructor(
    private val productRepository: ProductRepository
) : ViewModel() {
    
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
    
    fun onNameChange(value: String) { name.value = value }
    fun onSkuChange(value: String) { sku.value = value }
    fun onCategoryChange(value: Long?) { categoryId.value = value }
    fun onBuyPriceChange(value: String) { buyPrice.value = value }
    fun onSellPriceChange(value: String) { sellPrice.value = value }
    fun onStockChange(value: String) { stock.value = value }
    fun onMinStockChange(value: String) { minStock.value = value }
    fun onDescriptionChange(value: String) { description.value = value }
    fun onImageSelected(uri: String?) { /* TODO */ }
    
    fun updateProduct(id: Long) {
        val buy = buyPrice.value.toDoubleOrNull()
        val sell = sellPrice.value.toDoubleOrNull()
        val stk = stock.value.toIntOrNull()
        val min = minStock.value.toIntOrNull() ?: 5
        
        if (buy == null || sell == null || stk == null) {
            _error.value = "Invalid numbers"
            return
        }
        
        viewModelScope.launch {
            _isSaving.value = true
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
                    imageUrl = _product.value?.imageUrl
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
