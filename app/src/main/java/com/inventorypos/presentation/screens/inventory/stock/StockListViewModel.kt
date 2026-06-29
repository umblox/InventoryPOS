package com.inventorypos.presentation.screens.inventory.stock

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.inventorypos.domain.model.SmartPoItem
import com.inventorypos.domain.usecase.product.GetSmartPoListUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@HiltViewModel
class StockListViewModel @Inject constructor(
    private val getSmartPoListUseCase: GetSmartPoListUseCase
) : ViewModel() {

    // Mengambil data barang yang stoknya di bawah batas (minStock)
    val lowStockItems: StateFlow<List<SmartPoItem>> = getSmartPoListUseCase()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )
}

