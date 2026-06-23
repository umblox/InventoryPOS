package com.inventorypos.presentation.screens.reports.stock

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class StockMovementViewModel @Inject constructor() : ViewModel() {
    private val _movements = MutableStateFlow<List<StockMovement>>(emptyList())
    val movements: StateFlow<List<StockMovement>> = _movements

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    init {
        loadData()
    }

    private fun loadData() {
        viewModelScope.launch {
            _isLoading.value = true
            _movements.value = listOf(
                StockMovement("Indomie Goreng", "22 Jun 2026", "IN", 100),
                StockMovement("Aqua 600ml", "22 Jun 2026", "OUT", 20),
                StockMovement("Teh Botol", "21 Jun 2026", "IN", 50),
                StockMovement("Chitato", "21 Jun 2026", "OUT", 15)
            )
            _isLoading.value = false
        }
    }
}
