package com.inventorypos.presentation.screens.settings

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class BackupRestoreViewModel @Inject constructor() : ViewModel() {
    private val _isBackingUp = MutableStateFlow(false)
    val isBackingUp: StateFlow<Boolean> = _isBackingUp

    private val _isRestoring = MutableStateFlow(false)
    val isRestoring: StateFlow<Boolean> = _isRestoring

    private val _lastBackup = MutableStateFlow<String?>("22 Jun 2026, 14:30")
    val lastBackup: StateFlow<String?> = _lastBackup

    fun backup() {
        viewModelScope.launch {
            _isBackingUp.value = true
            kotlinx.coroutines.delay(2000)
            _lastBackup.value = "23 Jun 2026, 10:00"
            _isBackingUp.value = false
        }
    }

    fun restore() {
        viewModelScope.launch {
            _isRestoring.value = true
            kotlinx.coroutines.delay(2000)
            _isRestoring.value = false
        }
    }
}
