package com.inventorypos.presentation.screens.pos.payment

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SplitPaymentViewModel @Inject constructor() : ViewModel() {
    private val _totalAmount = MutableStateFlow(150000.0)
    val totalAmount: StateFlow<Double> = _totalAmount

    private val _cashAmount = MutableStateFlow("")
    val cashAmount: StateFlow<String> = _cashAmount

    private val _cardAmount = MutableStateFlow("")
    val cardAmount: StateFlow<String> = _cardAmount

    private val _remaining = MutableStateFlow(150000.0)
    val remaining: StateFlow<Double> = _remaining

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    private val _isSuccess = MutableStateFlow(false)
    val isSuccess: StateFlow<Boolean> = _isSuccess

    fun onCashAmountChange(value: String) {
        _cashAmount.value = value
        calculateRemaining()
    }

    fun onCardAmountChange(value: String) {
        _cardAmount.value = value
        calculateRemaining()
    }

    private fun calculateRemaining() {
        val cash = _cashAmount.value.toDoubleOrNull() ?: 0.0
        val card = _cardAmount.value.toDoubleOrNull() ?: 0.0
        _remaining.value = _totalAmount.value - cash - card
    }

    fun completePayment() {
        viewModelScope.launch {
            _isLoading.value = true
            kotlinx.coroutines.delay(1000)
            _isSuccess.value = true
            _isLoading.value = false
        }
    }
}
