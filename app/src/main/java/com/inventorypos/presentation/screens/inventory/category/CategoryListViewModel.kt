package com.inventorypos.presentation.screens.inventory.category

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.inventorypos.domain.model.Category
import com.inventorypos.domain.repository.CategoryRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CategoryListViewModel @Inject constructor(
    private val categoryRepository: CategoryRepository
) : ViewModel() {
    
    private val _categories = MutableStateFlow<List<Category>>(emptyList())
    val categories: StateFlow<List<Category>> = _categories.asStateFlow()
    
    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()
    
    init {
        loadCategories()
    }
    
    private fun loadCategories() {
        viewModelScope.launch {
            _isLoading.value = true
            // TODO: Implement repository
            _categories.value = listOf(
                Category(1, "Food", "Food items"),
                Category(2, "Drink", "Beverages"),
                Category(3, "Snack", "Snacks")
            )
            _isLoading.value = false
        }
    }
}
