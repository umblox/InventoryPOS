package com.inventorypos.presentation.screens.inventory.product

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.inventorypos.domain.model.Supplier
import com.inventorypos.domain.model.SupplierOffer
import com.inventorypos.domain.repository.ProductRepository
import com.inventorypos.domain.repository.SupplierRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
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

    fun loadSuppliers(productId: Long) {
     viewModelScope.launch {
         _isLoading.value = true
         try {
             // 1. Ambil data supplier yang sudah terhubung dengan produk ini
             _suppliers.value = supplierRepository.getSuppliersForProduct(productId)

             // 2. Gunakan first() BUKAN collect(), agar coroutine tidak nyangkut (hanging)
             val list = supplierRepository.getAllSuppliers().first()

             // 3. Filter data agar supplier yang sudah terpilih tidak muncul lagi di dialog
             val existingIds = _suppliers.value.map { it.supplierId }
             _allSuppliers.value = list.filter { it.id !in existingIds }

         } catch (e: Exception) {
             // (Opsional) Anda bisa menambahkan log error di sini
             e.printStackTrace()
         } finally {
             // 4. Finally menjamin loading PASTI BERHENTI, apapun yang terjadi di blok try
             _isLoading.value = false
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
}
    // 1. TAMBAHKAN VARIABEL STATE INI DI BAGIAN ATAS (di bawah _showAddDialog)
    private val _showEditDialog = MutableStateFlow(false)
    val showEditDialog: StateFlow<Boolean> = _showEditDialog

    private val _editingSupplier = MutableStateFlow<SupplierOffer?>(null)
    val editingSupplier: StateFlow<SupplierOffer?> = _editingSupplier

    // 2. TAMBAHKAN FUNGSI-FUNGSI INI DI BAGIAN BAWAH
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
                
                // Gunakan blok try-finally seperti yang kita pelajari sebelumnya agar loading terjamin berhenti
                val updatedSuppliers = supplierRepository.getSuppliersForProduct(productId)
                _suppliers.value = updatedSuppliers
            } finally {
                _isLoading.value = false
            }
        }
    }
}
