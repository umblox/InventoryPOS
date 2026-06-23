package com.inventorypos.presentation.screens.users

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class UserPermissionViewModel @Inject constructor() : ViewModel() {
    private val _permissions = MutableStateFlow<List<Permission>>(emptyList())
    val permissions: StateFlow<List<Permission>> = _permissions

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    init {
        loadPermissions()
    }

    private fun loadPermissions() {
        viewModelScope.launch {
            _isLoading.value = true
            _permissions.value = listOf(
                Permission("View Dashboard", true),
                Permission("Manage POS", true),
                Permission("Manage Products", false),
                Permission("Manage Stock", false),
                Permission("View Reports", true),
                Permission("Manage Users", false),
                Permission("Manage Settings", false)
            )
            _isLoading.value = false
        }
    }

    fun togglePermission(name: String) {
        _permissions.value = _permissions.value.map {
            if (it.name == name) it.copy(isGranted = !it.isGranted) else it
        }
    }
}
