package com.inventorypos.presentation.screens.pos

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.inventorypos.domain.model.Product
import com.inventorypos.domain.repository.ProductRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PosViewModel @Inject constructor(
    private val productRepository: ProductRepository
) : ViewModel() {
    
    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery
    
    private val _cartItems = MutableStateFlow<List<CartItem>>(emptyList())
    val cartItems: StateFlow<List<CartItem>> = _cartItems.asStateFlow()
    
    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading
    
    val products: StateFlow<List<Product>> = _searchQuery
        .debounce(200)
        .flatMapLatest { query ->
            if (query.isBlank()) {
                productRepository.getAllProducts()
            } else {
                productRepository.searchProducts(query)
            }
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )
    
    val totalAmount: StateFlow<Double> = _cartItems
        .map { items -> items.sumOf { it.totalPrice } }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubvised(5000),
            initialValue = 0.0
        )
    
    fun onSearchQueryChange(query: String) {
        _searchQuery.value = query
    }
    
    fun addToCart(product: Product) {
        val currentItems = _cartItems.value.toMutableList()
        val existingIndex = currentItems.indexOfFirst { it.productId == product.id }
        
        if (existingIndex >= 0) {
            val existing = currentItems[existingIndex]
            currentItems[existingIndex] = existing.copy(
                quantity = existing.quantity + 1,
                totalPrice = (existing.quantity + 1) * existing.unitPrice
            )
        } else {
            currentItems.add(
                CartItem(
                    productId = product.id,
                    productName = product.name,
                    unitPrice = product.sellPrice,
                    quantity = 1,
                    totalPrice = product.sellPrice
                )
            )
        }
        _cartItems.value = currentItems
    }
    
    fun updateQuantity(productId: Long, quantity: Int) {
        val currentItems = _cartItems.value.toMutableList()
        val index = currentItems.indexOfFirst { it.productId == productId }
        if (index >= 0) {
            if (quantity <= 0) {
                currentItems.removeAt(index)
            } else {
                val item = currentItems[index]
                currentItems[index] = item.copy(
                    quantity = quantity,
                    totalPrice = quantity * item.unitPrice
                )
            }
            _cartItems.value = currentItems
        }
    }
    
    fun removeFromCart(productId: Long) {
        _cartItems.value = _cartItems.value.filter { it.productId != productId }
    }
    
    fun clearCart() {
        _cartItems.value = emptyList()
    }
    
    fun holdBill() {
        // TODO: Save to held bills
    }
}
