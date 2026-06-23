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
class ProductDetailViewModel @Inject constructor(
    private val productRepository: ProductRepository
) : ViewModel() {
    
    private val _product = MutableStateFlow<Product?>(null)
    val product: StateFlow<Product?> = _product
    
    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading
    
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
}
