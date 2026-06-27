package com.inventorypos.presentation.screens.profile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.inventorypos.domain.repository.AuthRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val authRepository: AuthRepository
) : ViewModel() { // <-- Di sinilah error Anda sebelumnya karena kurang ini
    
    fun logout() {
        viewModelScope.launch {
            authRepository.logout()
        }
    }
}
