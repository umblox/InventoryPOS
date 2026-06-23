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
class ProductDeleteViewModel @Inject constructor(
    private val productRepository: ProductRepository
) : ViewModel() {
    
    private val _product = MutableStateFlow<Product?>(null)
    val product: StateFlow<Product?> = _product
    
    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading
    
    private val _isDeleting = MutableStateFlow(false)
    val isDeleting: StateFlow<Boolean> = _isDeleting
    
    private val _isDeleted = MutableStateFlow(false)
    val isDeleted: StateFlow<Boolean> = _isDeleted
    
    fun loadProduct(id: Long) {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                _product.value = productRepository.getProductById(id)
            } finally {
                _isLoading.value = false
            }
        }
    }
    
    fun deleteProduct(id: Long) {
        viewModelScope.launch {
            _isDeleting.value = true
            try {
                productRepository.deleteProduct(id)
                _isDeleted.value = true
            } catch (e: Exception) {
                // Handle error
            } finally {
                _isDeleting.value = false
            }
        }
    }
}
