package com.inventorypos.presentation.screens.pos.payment

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CashPaymentViewModel @Inject constructor() : ViewModel() {
    private val _totalAmount = MutableStateFlow(150000.0)
    val totalAmount: StateFlow<Double> = _totalAmount

    private val _amountPaid = MutableStateFlow("")
    val amountPaid: StateFlow<String> = _amountPaid

    private val _change = MutableStateFlow(0.0)
    val change: StateFlow<Double> = _change

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    private val _isSuccess = MutableStateFlow(false)
    val isSuccess: StateFlow<Boolean> = _isSuccess

    fun onAmountPaidChange(value: String) {
        _amountPaid.value = value
        val paid = value.toDoubleOrNull() ?: 0.0
        _change.value = paid - _totalAmount.value
    }

    fun completePayment() {
        val paid = _amountPaid.value.toDoubleOrNull() ?: 0.0
        if (paid < _totalAmount.value) return
        viewModelScope.launch {
            _isLoading.value = true
            kotlinx.coroutines.delay(1000)
            _isSuccess.value = true
            _isLoading.value = false
        }
    }
}
