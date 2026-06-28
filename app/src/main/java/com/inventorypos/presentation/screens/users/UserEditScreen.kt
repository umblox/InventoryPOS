package com.inventorypos.presentation.screens.users

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
    
    // State untuk kontrol dropdown
    var expanded by remember { mutableStateOf(false) }

    LaunchedEffect(userId) {
        viewModel.loadUser(userId)
    }

    LaunchedEffect(isSuccess) {
        if (isSuccess) {
            navController.popBackStack()
        }
    }

    Scaffold(
        topBar = {
            CustomTopBar(title = "Edit User", subtitle = "Update staff information", onBackClick = { navController.popBackStack() })
        },
        containerColor = PremiumDarkBackground
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(padding)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            CustomTextField(value = fullName, onValueChange = viewModel::onFullNameChange, label = "Full Name", leadingIcon = Icons.Default.Person)
            CustomTextField(value = username, onValueChange = viewModel::onUsernameChange, label = "Username", leadingIcon = Icons.Default.AccountCircle)

            // DROPDOWN MENU UNTUK ROLE
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
                        focusedBorderColor = PremiumGold,
                        unfocusedBorderColor = PremiumDarkSurface,
                        focusedTextColor = PremiumTextPrimary,
                        unfocusedTextColor = PremiumTextPrimary
                    )
                )
                
                ExposedDropdownMenu(
                    expanded = expanded,
                    onDismissRequest = { expanded = false },
                    modifier = Modifier.background(PremiumDarkSurface)
                ) {
                    UserRole.values().forEach { roleEnum ->
                        DropdownMenuItem(
                            text = { Text(roleEnum.name, color = PremiumTextPrimary) },
                            onClick = {
                                viewModel.onRoleChange(roleEnum.name)
                                expanded = false
                            }
                        )
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
        }
    }
}
