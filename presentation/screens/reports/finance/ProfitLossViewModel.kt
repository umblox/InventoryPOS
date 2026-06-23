package com.inventorypos.presentation.screens.reports.finance

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProfitLossViewModel @Inject constructor() : ViewModel() {
    private val _data = MutableStateFlow(ProfitLossData(0.0, 0.0, 0.0, 0.0, 0.0))
    val data: StateFlow<ProfitLossData> = _data

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    init {
        loadData()
    }

    private fun loadData() {
        viewModelScope.launch {
            _isLoading.value = true
            val revenue = 45000000.0
            val cogs = 28000000.0
            val profit = revenue - cogs
            val expenses = 5000000.0
            val netProfit = profit - expenses
            _data.value = ProfitLossData(revenue, cogs, profit, expenses, netProfit)
            _isLoading.value = false
        }
    }
}
