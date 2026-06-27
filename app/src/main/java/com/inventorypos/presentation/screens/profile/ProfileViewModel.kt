package com.inventorypos.presentation.screens.profile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.inventorypos.data.local.dao.UserDao
import com.inventorypos.data.local.entity.UserEntity
import com.inventorypos.data.preferences.AuthPreferences
import com.inventorypos.domain.repository.AuthRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val authRepository: AuthRepository,
    private val userDao: UserDao,
    private val authPreferences: AuthPreferences
) : ViewModel() {
    
    private val _user = MutableStateFlow<UserEntity?>(null)
    val user: StateFlow<UserEntity?> = _user

    init {
        // Otomatis mengambil data karyawan yang sedang login saat ini
        viewModelScope.launch {
            authPreferences.loggedInUserId.collect { id ->
                if (id != -1L) {
                    _user.value = userDao.getById(id)
                }
            }
        }
    }

    fun logout() {
        viewModelScope.launch {
            authRepository.logout()
        }
    }
}
