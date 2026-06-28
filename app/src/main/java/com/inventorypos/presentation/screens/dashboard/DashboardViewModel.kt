package com.inventorypos.presentation.screens.dashboard

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.inventorypos.data.local.dao.UserDao
import com.inventorypos.data.local.entity.UserEntity
import com.inventorypos.data.preferences.AuthPreferences
import com.inventorypos.domain.repository.ProductRepository
import com.inventorypos.presentation.screens.dashboard.widgets.RecentTransaction
import com.inventorypos.presentation.screens.dashboard.widgets.TopProduct
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.util.Date
import javax.inject.Inject

@HiltViewModel
class DashboardViewModel @Inject constructor(
    private val productRepository: ProductRepository,
    private val authPreferences: AuthPreferences, // Tambahkan injeksi ini
    private val userDao: UserDao                 // Tambahkan injeksi ini
) : ViewModel() {
    
    // VARIABEL BARU: Menyimpan data user yang sedang login
    private val _currentUser = MutableStateFlow<UserEntity?>(null)
    val currentUser: StateFlow<UserEntity?> = _currentUser

    private val _todaySales = MutableStateFlow(0.0)
    val todaySales: StateFlow<Double> = _todaySales
    
    private val _transactionCount = MutableStateFlow(0)
    val transactionCount: StateFlow<Int> = _transactionCount
    
    private val _lowStockCount = MutableStateFlow(0)
    val lowStockCount: StateFlow<Int> = _lowStockCount
    
    private val _productCount = MutableStateFlow(0)
    val productCount: StateFlow<Int> = _productCount
    
    private val _recentTransactions = MutableStateFlow<List<RecentTransaction>>(emptyList())
    val recentTransactions: StateFlow<List<RecentTransaction>> = _recentTransactions
    
    private val _topProducts = MutableStateFlow<List<TopProduct>>(emptyList())
    val topProducts: StateFlow<List<TopProduct>> = _topProducts
    
    init {
        loadCurrentUser()
        loadDashboardData()
    }
    
    // FUNGSI BARU: Menarik profil pengguna aktif
    private fun loadCurrentUser() {
        viewModelScope.launch {
            authPreferences.loggedInUserId.collect { userId ->
                if (userId != -1L) {
                    _currentUser.value = userDao.getById(userId)
                }
            }
        }
    }
    
    private fun loadDashboardData() {
        viewModelScope.launch {
            productRepository.getProductCount().collect { count ->
                _productCount.value = count
            }
        }
        
        viewModelScope.launch {
            productRepository.getLowStockProducts().collect { products ->
                _lowStockCount.value = products.size
            }
        }
        
        // Dummy data for demo
        _todaySales.value = 2450000.0
        _transactionCount.value = 42
        _recentTransactions.value = listOf(
            RecentTransaction(1, "TRX-001", 150000.0, Date(), "PAID"),
            RecentTransaction(2, "TRX-002", 275000.0, Date(), "PAID"),
            RecentTransaction(3, "TRX-003", 89000.0, Date(), "PAID"),
            RecentTransaction(4, "TRX-004", 450000.0, Date(), "PAID"),
            RecentTransaction(5, "TRX-005", 125000.0, Date(), "PAID")
        )
        _topProducts.value = listOf(
            TopProduct("Indomie Goreng", 45, 675000.0),
            TopProduct("Aqua 600ml", 38, 380000.0),
            TopProduct("Teh Botol", 32, 256000.0),
            TopProduct("Chitato", 28, 196000.0),
            TopProduct("Oreo", 25, 175000.0)
        )
    }
}
