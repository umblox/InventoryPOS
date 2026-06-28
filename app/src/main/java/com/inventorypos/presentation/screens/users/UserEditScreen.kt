package com.inventorypos.presentation.screens.users

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.inventorypos.data.local.entity.UserRole
import com.inventorypos.presentation.components.common.*
import com.inventorypos.presentation.theme.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UserEditScreen(
    navController: NavController,
    userId: Long,
    viewModel: UserEditViewModel = hiltViewModel()
) {
    val fullName by viewModel.fullName.collectAsState()
    val username by viewModel.username.collectAsState()
    val role by viewModel.role.collectAsState()
    val isSaving by viewModel.isSaving.collectAsState()
    val isSuccess by viewModel.isSuccess.collectAsState()
    
    var expanded by remember { mutableStateOf(false) }

    LaunchedEffect(userId) { viewModel.loadUser(userId) }
    LaunchedEffect(isSuccess) { if (isSuccess) navController.popBackStack() }

    Scaffold(
        topBar = {
            CustomTopBar(title = "Edit User", subtitle = "Update staff information", onBackClick = { navController.popBackStack() })
        },
        containerColor = PremiumDarkBackground
    ) { padding ->
        Column(
            modifier = Modifier.fillMaxSize().verticalScroll(rememberScrollState()).padding(padding).padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            CustomTextField(value = fullName, onValueChange = viewModel::onFullNameChange, label = "Full Name", leadingIcon = Icons.Default.Person)
            
            // PERBAIKAN 1: Mengunci kolom Username agar Read-Only menggunakan OutlinedTextField bertema premium
            OutlinedTextField(
                value = username,
                onValueChange = {},
                readOnly = true,
                label = { Text("Username (Tidak dapat diubah)", color = PremiumTextMuted) },
                leadingIcon = { Icon(Icons.Default.AccountCircle, null, tint = PremiumTextMuted) },
                modifier = Modifier.fillMaxWidth(),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = PremiumDarkSurface,
                    unfocusedBorderColor = PremiumDarkSurface,
                    focusedTextColor = PremiumTextMuted,
                    unfocusedTextColor = PremiumTextMuted
                )
            )

            // PERBAIKAN 2: Proteksi Super Admin Utama (ID #1). Jika ID = 1, Dropdown dikunci mati.
            if (userId == 1L) {
                OutlinedTextField(
                    value = "SUPER_ADMIN",
                    onValueChange = {},
                    readOnly = true,
                    label = { Text("Role / Jabatan (Owner Mutlak)", color = PremiumTextMuted) },
                    leadingIcon = { Icon(Icons.Default.Security, null, tint = PremiumTextMuted) },
                    modifier = Modifier.fillMaxWidth(),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = PremiumDarkSurface,
                        unfocusedBorderColor = PremiumDarkSurface,
                        focusedTextColor = PremiumTextMuted,
                        unfocusedTextColor = PremiumTextMuted
                    )
                )
            } else {
                // Jika user biasa, tampilkan dropdown pilihan jabatan seperti biasa
                ExposedDropdownMenuBox(
                    expanded = expanded,
                    onExpandedChange = { expanded = !expanded }
                ) {
                    OutlinedTextField(
                        value = role,
                        onValueChange = {},
                        readOnly = true,
                        label = { Text("Role / Jabatan", color = PremiumTextSecondary) },
                        trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
                        modifier = Modifier.menuAnchor().fillMaxWidth(),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = PremiumGold, unfocusedBorderColor = PremiumDarkSurface,
                            focusedTextColor = PremiumTextPrimary, unfocusedTextColor = PremiumTextPrimary
                        )
                    )
                    
                    ExposedDropdownMenu(
                        expanded = expanded,
                        onDismissRequest = { expanded = false },
                        modifier = Modifier.background(PremiumDarkSurface)
                    ) {
                        UserRole.values().forEach { roleEnum ->
                            // Super admin bawaan tidak boleh digandakan demi kerapian hirarki
                            if (roleEnum != UserRole.SUPER_ADMIN) {
                                DropdownMenuItem(
                                    text = { Text(roleEnum.name, color = PremiumTextPrimary) },
                                    onClick = { viewModel.onRoleChange(roleEnum.name); expanded = false }
                                )
                            }
                        }
                    }
                }
            }
            
            Spacer(modifier = Modifier.height(24.dp))
            
            CustomButton(
                text = "Save Changes",
                onClick = { viewModel.updateUser(userId) },
                isLoading = isSaving,
                modifier = Modifier.fillMaxWidth()
            )

            // TOMBOL BARU: Hapus/Nonaktifkan Karyawan (Hanya muncul jika bukan Super Admin Utama)
            if (userId != 1L) {
                Spacer(modifier = Modifier.height(16.dp))
                OutlinedButton(
                    onClick = { viewModel.deactivateUser(userId) },
                    modifier = Modifier.fillMaxWidth(),
                    colors = ButtonDefaults.outlinedButtonColors(contentColor = PremiumError),
                    border = androidx.compose.foundation.BorderStroke(1.dp, PremiumError)
                ) {
                    Icon(Icons.Default.Block, contentDescription = "Deactivate", modifier = Modifier.size(18.dp))
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Deactivate / Resign Account")
                }
            }
        }   
    }
}
