package com.inventorypos.presentation.screens.settings

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PrinterSettingsViewModel @Inject constructor() : ViewModel() {
    private val _isBluetoothEnabled = MutableStateFlow(false)
    val isBluetoothEnabled: StateFlow<Boolean> = _isBluetoothEnabled

    private val _pairedDevices = MutableStateFlow<List<String>>(emptyList())
    val pairedDevices: StateFlow<List<String>> = _pairedDevices

    private val _selectedDevice = MutableStateFlow<String?>(null)
    val selectedDevice: StateFlow<String?> = _selectedDevice

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    fun toggleBluetooth() {
        _isBluetoothEnabled.value = !_isBluetoothEnabled.value
        if (_isBluetoothEnabled.value) {
            scanDevices()
        }
    }

    private fun scanDevices() {
        viewModelScope.launch {
            _isLoading.value = true
            kotlinx.coroutines.delay(1000)
            _pairedDevices.value = listOf("Printer-001", "EPSON-TM", "XPrinter-58")
            _isLoading.value = false
        }
    }

    fun selectDevice(device: String) {
        _selectedDevice.value = device
    }

    fun testPrint() {
        // TODO: Implement test print
    }
}
