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
class CategoryEditViewModel @Inject constructor() : ViewModel() {
    
    private val _name = MutableStateFlow("")
    val name: StateFlow<String> = _name
    
    private val _description = MutableStateFlow("")
    val description: StateFlow<String> = _description
    
    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading
    
    private val _isSaving = MutableStateFlow(false)
    val isSaving: StateFlow<Boolean> = _isSaving
    
    private val _isSuccess = MutableStateFlow(false)
    val isSuccess: StateFlow<Boolean> = _isSuccess
    
    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error
    
    fun loadCategory(id: Long) {
        viewModelScope.launch {
            _isLoading.value = true
            kotlinx.coroutines.delay(500)
            _name.value = "Food"
            _description.value = "Food items"
            _isLoading.value = false
        }
    }
    
    fun onNameChange(value: String) { _name.value = value }
    fun onDescriptionChange(value: String) { _description.value = value }
    
    fun updateCategory(id: Long) {
        viewModelScope.launch {
            _isSaving.value = true
            kotlinx.coroutines.delay(1000)
            _isSuccess.value = true
            _isSaving.value = false
        }
    }
}
