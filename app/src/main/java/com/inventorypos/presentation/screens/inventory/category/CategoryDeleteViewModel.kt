package com.inventorypos.presentation.screens.inventory.category

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.inventorypos.domain.model.Category
import com.inventorypos.domain.repository.CategoryRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CategoryDeleteViewModel @Inject constructor(
    private val categoryRepository: CategoryRepository // Injeksi Repository
) : ViewModel() {
    
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
            try {
                // Menarik data kategori asli dari database
                _category.value = categoryRepository.getCategoryById(id)
            } catch (e: Exception) {
                // Tangani error jika diperlukan
            } finally {
                _isLoading.value = false
            }
        }
    }
    
    fun deleteCategory(id: Long) {
        viewModelScope.launch {
            _isDeleting.value = true
            try {
                // Mengeksekusi soft delete (isActive = 0) ke database
                categoryRepository.deleteCategory(id)
                _isDeleted.value = true
            } catch (e: Exception) {
                // Tangani error jika diperlukan
            } finally {
                _isDeleting.value = false
            }
        }
    }
}
