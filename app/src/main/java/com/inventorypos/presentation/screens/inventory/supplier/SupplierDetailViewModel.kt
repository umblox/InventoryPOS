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
class SupplierDetailViewModel @Inject constructor(
    private val supplierRepository: SupplierRepository // Injeksi Repository
) : ViewModel() {
    private val _supplier = MutableStateFlow<Supplier?>(null)
    val supplier: StateFlow<Supplier?> = _supplier

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    fun loadSupplier(id: Long) {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                // Menarik data asli dari SQLite/Room
                _supplier.value = supplierRepository.getSupplierById(id)
            } catch (e: Exception) {
                // Handle error jika diperlukan
            } finally {
                _isLoading.value = false
            }
        }
    }
}
