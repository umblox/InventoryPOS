package com.inventorypos.presentation.screens.inventory.stock

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.inventorypos.data.local.entity.StockLogEntity
import com.inventorypos.data.local.entity.StockLogType
import com.inventorypos.domain.model.Product
import com.inventorypos.domain.repository.ProductRepository
import com.inventorypos.domain.repository.StockRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import java.util.Date
import javax.inject.Inject

@HiltViewModel
class StockOpnameViewModel @Inject constructor(
    private val productRepository: ProductRepository,
    private val stockRepository: StockRepository
) : ViewModel() {

    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery

    // Daftar semua produk aktif
    private val _allProducts = MutableStateFlow<List<Product>>(emptyList())
    
    // Daftar produk yang ditampilkan (setelah di-filter pencarian)
    val displayedProducts: StateFlow<List<Product>> = combine(_allProducts, _searchQuery) { products, query ->
        if (query.isBlank()) products else products.filter { 
            it.name.contains(query, ignoreCase = true) || it.sku.contains(query, ignoreCase = true) 
        }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    // WADAH PENYIMPANAN SEMENTARA: ProductID -> Jumlah Fisik (Inputan Karyawan)
    private val _opnameInputs = MutableStateFlow<Map<Long, Int>>(emptyMap())
    val opnameInputs: StateFlow<Map<Long, Int>> = _opnameInputs

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    private val _isSuccess = MutableStateFlow(false)
    val isSuccess: StateFlow<Boolean> = _isSuccess

    init {
        loadProducts()
    }

    private fun loadProducts() {
        viewModelScope.launch {
            _isLoading.value = true
            productRepository.getAllProducts().collectLatest { list ->
                _allProducts.value = list
                _isLoading.value = false
            }
        }
    }

    fun onSearchQueryChange(query: String) {
        _searchQuery.value = query
    }

    // Fungsi saat karyawan mengetik angka stok di rak
    fun updatePhysicalCount(productId: Long, count: Int) {
        val currentInputs = _opnameInputs.value.toMutableMap()
        currentInputs[productId] = count
        _opnameInputs.value = currentInputs
    }

    // Fungsi untuk membatalkan/mengosongkan inputan barang tertentu
    fun clearInput(productId: Long) {
        val currentInputs = _opnameInputs.value.toMutableMap()
        currentInputs.remove(productId)
        _opnameInputs.value = currentInputs
    }

    // Fungsi Finalisasi: Mengeksekusi perubahan ke Database HANYA untuk barang yang diinput
    fun finalizeOpname() {
        if (_opnameInputs.value.isEmpty()) return

        viewModelScope.launch {
            _isLoading.value = true
            try {
                _opnameInputs.value.forEach { (productId, physicalCount) ->
                    val product = _allProducts.value.find { it.id == productId }
                    
                    if (product != null && product.stock != physicalCount) {
                        val difference = physicalCount - product.stock

                        // 1. Update stok utama di tabel produk
                        productRepository.updateProduct(product.copy(stock = physicalCount))

                        // 2. Catat riwayat penyesuaian di Stock Log
                        stockRepository.insertLog(
                            StockLogEntity(
                                productId = productId,
                                type = StockLogType.ADJUSTMENT, // <--- Penanda khusus opname
                                quantity = difference, // Bisa minus (hilang) atau plus (lebih)
                                previousStock = product.stock,
                                newStock = physicalCount,
                                reference = "OPNAME-${System.currentTimeMillis()}",
                                notes = "Penyesuaian stok opname manual",
                                userId = 0L, // Idealnya ambil dari ID kasir yang login
                                createdAt = Date()
                            )
                        )
                    }
                }
                _isSuccess.value = true
                _opnameInputs.value = emptyMap() // Bersihkan sesi setelah berhasil
            } catch (e: Exception) {
                e.printStackTrace()
            } finally {
                _isLoading.value = false
            }
        }
    }
}
