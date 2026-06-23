package com.inventorypos.presentation.screens.reports.sales

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SalesDailyViewModel @Inject constructor() : ViewModel() {
    private val _dailySales = MutableStateFlow<List<DailySale>>(emptyList())
    val dailySales: StateFlow<List<DailySale>> = _dailySales

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    init {
        loadData()
    }

    private fun loadData() {
        viewModelScope.launch {
            _isLoading.value = true
            _dailySales.value = listOf(
                DailySale("22 Jun 2026", 2450000.0, 42),
                DailySale("21 Jun 2026", 1890000.0, 35),
                DailySale("20 Jun 2026", 3200000.0, 58),
                DailySale("19 Jun 2026", 2100000.0, 40),
                DailySale("18 Jun 2026", 1750000.0, 31)
            )
            _isLoading.value = false
        }
    }
}
