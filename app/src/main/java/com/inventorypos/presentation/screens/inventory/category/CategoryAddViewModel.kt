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
class CategoryAddViewModel @Inject constructor(
    private val categoryRepository: CategoryRepository // Injeksi Repository
) : ViewModel() {
    
    private val _name = MutableStateFlow("")
    val name: StateFlow<String> = _name
    
    private val _description = MutableStateFlow("")
    val description: StateFlow<String> = _description
    
    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading
    
    private val _isSuccess = MutableStateFlow(false)
    val isSuccess: StateFlow<Boolean> = _isSuccess
    
    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error
    
    fun onNameChange(value: String) { _name.value = value; _error.value = null }
    fun onDescriptionChange(value: String) { _description.value = value; _error.value = null }
    
    fun saveCategory() {
        if (_name.value.isBlank()) {
            _error.value = "Category name is required"
            return
        }
        
        viewModelScope.launch {
            _isLoading.value = true
            _error.value = null
            try {
                // Membuat domain model dari inputan
                val newCategory = Category(
                    name = _name.value,
                    description = _description.value,
                    isActive = true
                )
                
                // Menyimpan ke database melalui repository
                categoryRepository.insertCategory(newCategory)
                
                _isSuccess.value = true
            } catch (e: Exception) {
                _error.value = "Failed to save: ${e.message}"
            } finally {
                _isLoading.value = false
            }
        }
    }
}
