package com.inventorypos.presentation.screens.inventory.category

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CategoryAddViewModel @Inject constructor() : ViewModel() {
    
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
            kotlinx.coroutines.delay(1000)
            _isSuccess.value = true
            _isLoading.value = false
        }
    }
}
