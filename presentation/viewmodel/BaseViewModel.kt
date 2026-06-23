package com.inventorypos.presentation.viewmodel

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

abstract class BaseViewModel : ViewModel() {
    
    protected fun <T> MutableStateFlow<T>.toStateFlow(): StateFlow<T> = this
    
    protected fun validateRequired(value: String, fieldName: String): String? {
        return if (value.isBlank()) "$fieldName is required" else null
    }
    
    protected fun validateNumber(value: String, fieldName: String): String? {
        return if (value.toDoubleOrNull() == null) "$fieldName must be a valid number" else null
    }
    
    protected fun validatePositive(value: Double, fieldName: String): String? {
        return if (value <= 0) "$fieldName must be greater than 0" else null
    }
}
