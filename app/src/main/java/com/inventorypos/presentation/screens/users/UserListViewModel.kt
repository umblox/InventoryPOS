package com.inventorypos.presentation.screens.users

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.inventorypos.domain.model.User
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class UserListViewModel @Inject constructor() : ViewModel() {
    private val _users = MutableStateFlow<List<User>>(emptyList())
    val users: StateFlow<List<User>> = _users

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    init {
        loadUsers()
    }

    private fun loadUsers() {
        viewModelScope.launch {
            _isLoading.value = true
            _users.value = listOf(
                User(1, "admin", "Administrator", "admin@pos.com", null, "SUPER_ADMIN"),
                User(2, "kasir1", "Kasir Satu", null, "08123456789", "CASHIER"),
                User(3, "gudang1", "Staff Gudang", null, null, "WAREHOUSE")
            )
            _isLoading.value = false
        }
    }
}
