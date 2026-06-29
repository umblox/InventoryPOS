package com.inventorypos.presentation.screens.inventory.supplier

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.inventorypos.domain.model.Supplier
import com.inventorypos.domain.repository.SupplierRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SupplierDeleteViewModel @Inject constructor(
    private val supplierRepository: SupplierRepository
) : ViewModel() {
    
    private val _supplier = MutableStateFlow<Supplier?>(null)
    val supplier: StateFlow<Supplier?> = _supplier
    
    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading
    
    private val _isDeleting = MutableStateFlow(false)
    val isDeleting: StateFlow<Boolean> = _isDeleting
    
    private val _isDeleted = MutableStateFlow(false)
    val isDeleted: StateFlow<Boolean> = _isDeleted
    
    fun loadSupplier(id: Long) {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                _supplier.value = supplierRepository.getSupplierById(id)
            } finally {
                _isLoading.value = false
            }
        }
    }
    
    fun deleteSupplier(id: Long) {
        viewModelScope.launch {
            _isDeleting.value = true
            try {
                supplierRepository.deleteSupplier(id)
                _isDeleted.value = true
            } catch (e: Exception) {
                // Handle error
            } finally {
                _isDeleting.value = false
            }
        }
    }
}

