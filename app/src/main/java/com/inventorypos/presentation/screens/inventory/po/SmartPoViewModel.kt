package com.inventorypos.presentation.screens.inventory.po

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.inventorypos.data.local.entity.PurchaseOrderEntity
import com.inventorypos.data.local.entity.PurchaseOrderItemEntity
import com.inventorypos.domain.model.SmartPoItem
import com.inventorypos.domain.repository.PurchaseOrderRepository
import com.inventorypos.domain.usecase.product.GetSmartPoListUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject

@HiltViewModel
class SmartPoViewModel @Inject constructor(
    private val getSmartPoListUseCase: GetSmartPoListUseCase,
    private val poRepository: PurchaseOrderRepository
) : ViewModel() {

    private val _poItems = MutableStateFlow<List<SmartPoItem>>(emptyList())
    val poItems: StateFlow<List<SmartPoItem>> = _poItems

    // Menyimpan berapa jumlah pesanan untuk setiap ProductID
    private val _selectedQuantities = MutableStateFlow<Map<Long, Int>>(emptyMap())
    val selectedQuantities: StateFlow<Map<Long, Int>> = _selectedQuantities

    // Menyimpan Supplier mana yang dipilih untuk ProductID tersebut
    private val _selectedSuppliers = MutableStateFlow<Map<Long, Long>>(emptyMap())
    val selectedSuppliers: StateFlow<Map<Long, Long>> = _selectedSuppliers

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    private val _isSuccess = MutableStateFlow(false)
    val isSuccess: StateFlow<Boolean> = _isSuccess

    init {
        loadSmartPoList()
    }

    private fun loadSmartPoList() {
        viewModelScope.launch {
            _isLoading.value = true
            getSmartPoListUseCase().collectLatest { list ->
                _poItems.value = list
                _isLoading.value = false
            }
        }
    }

    fun toggleSelection(item: SmartPoItem, isSelected: Boolean) {
        val currentQs = _selectedQuantities.value.toMutableMap()
        val currentSs = _selectedSuppliers.value.toMutableMap()

        if (isSelected) {
            // Rumus Default: Genapkan stok kembali ke batas minimal + bonus 10 pcs
            val defaultQty = if (item.product.stock < item.product.minStock) {
                (item.product.minStock - item.product.stock) + 10
            } else 10

            currentQs[item.product.id] = defaultQty
            item.recommendedSupplier?.let {
                currentSs[item.product.id] = it.supplierId
            }
        } else {
            currentQs.remove(item.product.id)
            currentSs.remove(item.product.id)
        }
        _selectedQuantities.value = currentQs
        _selectedSuppliers.value = currentSs
    }

    fun updateQuantity(productId: Long, qty: Int) {
        val currentQs = _selectedQuantities.value.toMutableMap()
        if (currentQs.containsKey(productId)) {
            currentQs[productId] = qty
            _selectedQuantities.value = currentQs
        }
    }

    fun autoSelectRecommended() {
        val currentQs = mutableMapOf<Long, Int>()
        val currentSs = mutableMapOf<Long, Long>()

        _poItems.value.forEach { item ->
            if (item.recommendedSupplier != null) {
                val defaultQty = if (item.product.stock < item.product.minStock) {
                    (item.product.minStock - item.product.stock) + 10
                } else 10
                currentQs[item.product.id] = defaultQty
                currentSs[item.product.id] = item.recommendedSupplier.supplierId
            }
        }
        _selectedQuantities.value = currentQs
        _selectedSuppliers.value = currentSs
    }

    fun createPurchaseOrders() {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                // Wadah untuk mengelompokkan barang berdasarkan ID Supplier
                val groupedItems = mutableMapOf<Long, MutableList<PurchaseOrderItemEntity>>()
                val supplierNames = mutableMapOf<Long, String>()

                _selectedQuantities.value.forEach { (productId, qty) ->
                    val supplierId = _selectedSuppliers.value[productId] ?: return@forEach
                    val smartItem = _poItems.value.find { it.product.id == productId } ?: return@forEach
                    val offer = smartItem.recommendedSupplier 

                    if (offer != null) {
                        if (!groupedItems.containsKey(supplierId)) {
                            groupedItems[supplierId] = mutableListOf()
                            supplierNames[supplierId] = offer.supplierName
                        }

                        groupedItems[supplierId]?.add(
                            PurchaseOrderItemEntity(
                                poId = 0L, // Akan diisi otomatis oleh DAO Transaction
                                productId = productId,
                                productName = smartItem.product.name,
                                buyPrice = offer.buyPrice,
                                quantityOrdered = qty,
                                quantityReceived = 0
                            )
                        )
                    }
                }

                val ordersToSave = mutableMapOf<PurchaseOrderEntity, List<PurchaseOrderItemEntity>>()
                val dateStr = SimpleDateFormat("yyyyMMdd", Locale.getDefault()).format(Date())

                // Buat Nota PO per Supplier
                groupedItems.forEach { (supplierId, items) ->
                    val totalAmount = items.sumOf { it.buyPrice * it.quantityOrdered }
                    val poNumber = "PO-$dateStr-${(100..999).random()}"

                    val poEntity = PurchaseOrderEntity(
                        poNumber = poNumber,
                        supplierId = supplierId,
                        supplierName = supplierNames[supplierId] ?: "Unknown",
                        totalAmount = totalAmount,
                        notes = "Auto-generated Smart PO"
                    )
                    ordersToSave[poEntity] = items
                }

                poRepository.createPurchaseOrders(ordersToSave)
                _isSuccess.value = true
            } catch (e: Exception) {
                e.printStackTrace()
            } finally {
                _isLoading.value = false
            }
        }
    }
}

