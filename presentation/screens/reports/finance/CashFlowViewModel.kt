package com.inventorypos.presentation.screens.reports.finance

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CashFlowViewModel @Inject constructor() : ViewModel() {
    private val _data = MutableStateFlow(CashFlowData(0.0, 0.0, 0.0))
    val data: StateFlow<CashFlowData> = _data

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    init {
        loadData()
    }

    private fun loadData() {
        viewModelScope.launch {
            _isLoading.value = true
            val inflow = 45000000.0
            val outflow = 32000000.0
            _data.value = CashFlowData(inflow, outflow, inflow - outflow)
            _isLoading.value = false
        }
    }
}
