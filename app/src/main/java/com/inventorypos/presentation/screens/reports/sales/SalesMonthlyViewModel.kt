package com.inventorypos.presentation.screens.reports.sales

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SalesMonthlyViewModel @Inject constructor() : ViewModel() {
    private val _monthlySales = MutableStateFlow<List<MonthlySale>>(emptyList())
    val monthlySales: StateFlow<List<MonthlySale>> = _monthlySales

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    init {
        loadData()
    }

    private fun loadData() {
        viewModelScope.launch {
            _isLoading.value = true
            _monthlySales.value = listOf(
                MonthlySale("June 2026", 45000000.0, 850),
                MonthlySale("May 2026", 42000000.0, 790),
                MonthlySale("April 2026", 38000000.0, 720)
            )
            _isLoading.value = false
        }
    }
}
