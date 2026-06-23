package com.inventorypos.presentation.screens.reports.sales

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SalesByProductViewModel @Inject constructor() : ViewModel() {
    private val _sales = MutableStateFlow<List<ProductSale>>(emptyList())
    val sales: StateFlow<List<ProductSale>> = _sales

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    init {
        loadData()
    }

    private fun loadData() {
        viewModelScope.launch {
            _isLoading.value = true
            _sales.value = listOf(
                ProductSale("Indomie Goreng", 450, 6750000.0),
                ProductSale("Aqua 600ml", 380, 3800000.0),
                ProductSale("Teh Botol", 320, 2560000.0),
                ProductSale("Chitato", 280, 1960000.0),
                ProductSale("Oreo", 250, 1750000.0)
            )
            _isLoading.value = false
        }
    }
}
