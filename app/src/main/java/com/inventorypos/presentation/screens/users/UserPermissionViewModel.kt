package com.inventorypos.presentation.screens.users

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.inventorypos.data.local.dao.UserPermissionDao
import com.inventorypos.data.local.entity.UserPermissionEntity
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class UserPermissionViewModel @Inject constructor(
    private val userPermissionDao: UserPermissionDao
) : ViewModel() {
    
    private val _permissions = MutableStateFlow<List<Permission>>(emptyList())
    val permissions: StateFlow<List<Permission>> = _permissions

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    private val _isSuccess = MutableStateFlow(false)
    val isSuccess: StateFlow<Boolean> = _isSuccess

    // Standar 7 Fitur POS Anda
    private val defaultPermissionNames = listOf(
        "View Dashboard",
        "Manage POS",
        "Manage Products",
        "Manage Stock",
        "View Reports",
        "Manage Users",
        "Manage Settings"
    )

    fun loadPermissions(userId: Long) {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                val dbPerms = userPermissionDao.getPermissionsByUserId(userId)
                if (dbPerms.isEmpty()) {
                    // Jika belum disetting, buat default semua false
                    _permissions.value = defaultPermissionNames.map { Permission(it, false) }
                } else {
                    // Sinkronisasi data dari DB ke UI
                    _permissions.value = defaultPermissionNames.map { name ->
                        val found = dbPerms.find { it.permissionName == name }
                        Permission(name, found?.isGranted ?: false)
                    }
                }
            } catch (e: Exception) {
                // Handle Error
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun togglePermission(name: String) {
        _permissions.value = _permissions.value.map {
            if (it.name == name) it.copy(isGranted = !it.isGranted) else it
        }
    }

    fun savePermissions(userId: Long) {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                userPermissionDao.deletePermissionsByUserId(userId)
                val entities = _permissions.value.map { perm ->
                    UserPermissionEntity(
                        userId = userId,
                        permissionName = perm.name,
                        isGranted = perm.isGranted
                    )
                }
                userPermissionDao.insertPermissions(entities)
                _isSuccess.value = true
            } catch (e: Exception) {
                _isSuccess.value = false
            } finally {
                _isLoading.value = false
            }
        }
    }
}
