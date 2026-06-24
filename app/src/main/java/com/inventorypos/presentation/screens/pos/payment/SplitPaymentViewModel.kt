package com.inventorypos.presentation.screens.pos.payment

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SplitPaymentViewModel @Inject constructor(
    // TODO: Injeksi UseCase atau Repository di sini
    // private val createTransactionUseCase: CreateTransactionUseCase
) : ViewModel() {
    
    // Jangan hardcode nilainya. Mulai dari 0.0.
    private val _totalAmount = MutableStateFlow(0.0)
    val totalAmount: StateFlow<Double> = _totalAmount

    private val _cashAmount = MutableStateFlow("")
    val cashAmount: StateFlow<String> = _cashAmount

    private val _cardAmount = MutableStateFlow("")
    val cardAmount: StateFlow<String> = _cardAmount

    private val _remaining = MutableStateFlow(0.0)
    val remaining: StateFlow<Double> = _remaining

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    private val _isSuccess = MutableStateFlow(false)
    val isSuccess: StateFlow<Boolean> = _isSuccess

    // Panggil fungsi ini dari UI (Screen/Fragment) saat pertama kali halaman dibuka
    fun initTotalAmount(amount: Double) {
        _totalAmount.value = amount
        calculateRemaining()
    }

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
        // Cegah pembayaran jika uang masih kurang (sisa tagihan > 0)
        if (_remaining.value > 0) return

        viewModelScope.launch {
            _isLoading.value = true
            try {
                // TODO: Ganti ini dengan pemanggilan database/API yang sebenarnya
                // val transactionData = Transaction(...)
                // createTransactionUseCase(transactionData)
                
                // Hapus delay ini setelah UseCase diimplementasikan
                kotlinx.coroutines.delay(1000) 
                
                _isSuccess.value = true
            } catch (e: Exception) {
                // Tangani error (misalnya gagal menyimpan ke database)
                e.printStackTrace()
                _isSuccess.value = false
            } finally {
                _isLoading.value = false
            }
        }
    }
}
