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
class UserDetailViewModel @Inject constructor() : ViewModel() {
    private val _user = MutableStateFlow<User?>(null)
    val user: StateFlow<User?> = _user

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    fun loadUser(id: Long) {
        viewModelScope.launch {
            _isLoading.value = true
            kotlinx.coroutines.delay(300)
            _user.value = User(id, "kasir1", "Kasir Satu", null, "08123456789", "CASHIER")
            _isLoading.value = false
        }
    }
}
