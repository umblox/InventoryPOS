package com.inventorypos.presentation.screens.pos.payment

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class QrisPaymentViewModel @Inject constructor() : ViewModel() {
    private val _totalAmount = MutableStateFlow(150000.0)
    val totalAmount: StateFlow<Double> = _totalAmount

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    private val _isSuccess = MutableStateFlow(false)
    val isSuccess: StateFlow<Boolean> = _isSuccess

    fun confirmPayment() {
        viewModelScope.launch {
            _isLoading.value = true
            kotlinx.coroutines.delay(1500)
            _isSuccess.value = true
            _isLoading.value = false
        }
    }
}
