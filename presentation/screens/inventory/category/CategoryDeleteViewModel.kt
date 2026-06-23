package com.inventorypos.presentation.screens.inventory.category

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.inventorypos.domain.model.Category
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CategoryDeleteViewModel @Inject constructor() : ViewModel() {
    
    private val _category = MutableStateFlow<Category?>(null)
    val category: StateFlow<Category?> = _category
    
    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading
    
    private val _isDeleting = MutableStateFlow(false)
    val isDeleting: StateFlow<Boolean> = _isDeleting
    
    private val _isDeleted = MutableStateFlow(false)
    val isDeleted: StateFlow<Boolean> = _isDeleted
    
    fun loadCategory(id: Long) {
        viewModelScope.launch {
            _isLoading.value = true
            kotlinx.coroutines.delay(300)
            _category.value = Category(id, "Food", "Food items")
            _isLoading.value = false
        }
    }
    
    fun deleteCategory(id: Long) {
        viewModelScope.launch {
            _isDeleting.value = true
            kotlinx.coroutines.delay(800)
            _isDeleted.value = true
            _isDeleting.value = false
        }
    }
}
