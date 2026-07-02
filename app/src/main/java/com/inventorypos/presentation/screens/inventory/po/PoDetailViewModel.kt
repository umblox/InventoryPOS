package com.inventorypos.presentation.screens.inventory.po

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.inventorypos.data.local.entity.PurchaseOrderWithItems
import com.inventorypos.domain.repository.PurchaseOrderRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PoDetailViewModel @Inject constructor(
    private val repository: PurchaseOrderRepository
) : ViewModel() {

    private val _poDetails = MutableStateFlow<PurchaseOrderWithItems?>(null)
    val poDetails: StateFlow<PurchaseOrderWithItems?> = _poDetails

    // Menyimpan input jumlah barang yang datang hari ini (Map<ItemId, Qty>)
    private val _receivingBatch = MutableStateFlow<Map<Long, Int>>(emptyMap())
    val receivingBatch: StateFlow<Map<Long, Int>> = _receivingBatch

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    private val _isSuccess = MutableStateFlow(false)
    val isSuccess: StateFlow<Boolean> = _isSuccess

    fun loadPo(poId: Long) {
        viewModelScope.launch {
            _isLoading.value = true
            val details = repository.getPurchaseOrderById(poId)
            _poDetails.value = details
            
            // Otomatis mengisi form dengan nilai SISA BARANG YANG BELUM DITERIMA
            if (details != null) {
                val initialBatch = mutableMapOf<Long, Int>()
                details.items.forEach { item ->
                    val remaining = item.quantityOrdered - item.quantityReceived
                    initialBatch[item.id] = if (remaining > 0) remaining else 0
                }
                _receivingBatch.value = initialBatch
            }
            _isLoading.value = false
        }
    }

    fun updateReceiveQty(itemId: Long, qty: Int) {
        val current = _receivingBatch.value.toMutableMap()
        current[itemId] = qty
        _receivingBatch.value = current
    }

    fun submitReceiving() {
        val currentPo = _poDetails.value ?: return
        viewModelScope.launch {
            _isLoading.value = true
            try {
                // Eksekusi fungsi hebat kita: Terima PO & Otomatis Stock In!
                repository.receivePurchaseOrder(currentPo.purchaseOrder.id, _receivingBatch.value)
                _isSuccess.value = true
                loadPo(currentPo.purchaseOrder.id) // Refresh data
            } catch (e: Exception) {
                e.printStackTrace()
            } finally {
                _isLoading.value = false
            }
        }
    }
}
