package com.inventorypos.presentation.screens.inventory.product

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.inventorypos.domain.model.Supplier
import com.inventorypos.domain.model.SupplierOffer
import com.inventorypos.domain.repository.ProductRepository
import com.inventorypos.domain.repository.SupplierRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProductSupplierViewModel @Inject constructor(
    private val supplierRepository: SupplierRepository,
    private val productRepository: ProductRepository
) : ViewModel() {

    private val _suppliers = MutableStateFlow<List<SupplierOffer>>(emptyList())
    val suppliers: StateFlow<List<SupplierOffer>> = _suppliers

    private val _allSuppliers = MutableStateFlow<List<Supplier>>(emptyList())
    val allSuppliers: StateFlow<List<Supplier>> = _allSuppliers

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    private val _showAddDialog = MutableStateFlow(false)
    val showAddDialog: StateFlow<Boolean> = _showAddDialog

    // --- STATE UNTUK EDIT HARGA ---
    private val _showEditDialog = MutableStateFlow(false)
    val showEditDialog: StateFlow<Boolean> = _showEditDialog

    private val _editingSupplier = MutableStateFlow<SupplierOffer?>(null)
    val editingSupplier: StateFlow<SupplierOffer?> = _editingSupplier

    fun loadSuppliers(productId: Long) {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                _suppliers.value = supplierRepository.getSuppliersForProduct(productId)
                val list = supplierRepository.getAllSuppliers().first()
                val existingIds = _suppliers.value.map { it.supplierId }
                _allSuppliers.value = list.filter { it.id !in existingIds }
            } catch (e: Exception) {
                e.printStackTrace()
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun addSupplier(productId: Long, supplierId: Long, buyPrice: Double, isPrimary: Boolean) {
        viewModelScope.launch {
            supplierRepository.addSupplierToProduct(productId, supplierId, buyPrice, isPrimary)
            _showAddDialog.value = false
            loadSuppliers(productId)
        }
    }

    fun removeSupplier(productId: Long, supplierId: Long) {
        viewModelScope.launch {
            supplierRepository.removeSupplierFromProduct(productId, supplierId)
            loadSuppliers(productId)
        }
    }

    fun setPrimary(productId: Long, supplierId: Long) {
        viewModelScope.launch {
            supplierRepository.setPrimarySupplier(productId, supplierId)
            loadSuppliers(productId)
        }
    }

    fun onShowAddDialog() {
        _showAddDialog.value = true
    }

    fun onDismissDialog() {
        _showAddDialog.value = false
    }

    // --- FUNGSI UNTUK EDIT HARGA ---
    fun onShowEditDialog(offer: SupplierOffer) {
        _editingSupplier.value = offer
        _showEditDialog.value = true
    }

    fun onDismissEditDialog() {
        _showEditDialog.value = false
        _editingSupplier.value = null
    }

    fun updateSupplierPrice(productId: Long, supplierId: Long, newPrice: Double) {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                supplierRepository.updateSupplierPriceForProduct(productId, supplierId, newPrice)
                _showEditDialog.value = false
                _editingSupplier.value = null
                
                val updatedSuppliers = supplierRepository.getSuppliersForProduct(productId)
                _suppliers.value = updatedSuppliers
            } finally {
                _isLoading.value = false
            }
        }
    }
} // <--- Semua kode di atas harus berada di ATAS kurung kurawal penutup ini
