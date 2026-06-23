package com.inventorypos.presentation.screens.profile

import androidx.lifecycle.ViewModel
import com.inventorypos.domain.model.User
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor() : ViewModel() {
    private val _user = MutableStateFlow<User?>(User(1, "admin", "Administrator", "admin@pos.com", null, "SUPER_ADMIN"))
    val user: StateFlow<User?> = _user

    fun logout() {
        // TODO: Clear session and navigate to login
    }
}
