package com.inventorypos.presentation.screens.profile

import androidx.lifecycle.ViewModel
import com.inventorypos.domain.model.User
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val authRepository: com.inventorypos.domain.repository.AuthRepository
) : ViewModel() {
    
    fun logout() {
        viewModelScope.launch {
            authRepository.logout()
        }
    }
}
