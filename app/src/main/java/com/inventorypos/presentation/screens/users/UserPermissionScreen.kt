package com.inventorypos.presentation.screens.users

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.inventorypos.presentation.components.common.*
import com.inventorypos.presentation.theme.*

@Composable
fun UserPermissionScreen(
    navController: NavController,
    userId: Long,
    viewModel: UserPermissionViewModel = hiltViewModel()
) {
    val permissions by viewModel.permissions.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()
    val isSuccess by viewModel.isSuccess.collectAsState()

    LaunchedEffect(userId) {
        viewModel.loadPermissions(userId)
    }

    LaunchedEffect(isSuccess) {
        if (isSuccess) navController.popBackStack()
    }

    Scaffold(
        topBar = {
            CustomTopBar(title = "Permissions", subtitle = "User ID: #$userId", onBackClick = { navController.popBackStack() })
        },
        containerColor = PremiumDarkBackground
    ) { padding ->
        if (isLoading) {
            LoadingIndicator()
        } else {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding)
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                permissions.forEach { perm ->
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = androidx.compose.ui.Alignment.CenterVertically
                    ) {
                        Text(text = perm.name, style = MaterialTheme.typography.bodyLarge, color = PremiumTextPrimary)
                        Switch(
                            checked = perm.isGranted,
                            onCheckedChange = { viewModel.togglePermission(perm.name) },
                            colors = SwitchDefaults.colors(
                                checkedThumbColor = PremiumGold,
                                checkedTrackColor = PremiumGold.copy(alpha = 0.5f)
                            )
                        )
                    }
                    // PERBAIKAN: Menggunakan Divider (Material 3 standard)
                    Divider(color = PremiumDarkSurfaceVariant)
                }

                Spacer(modifier = Modifier.height(16.dp))

                CustomButton(
                    text = "Save Permissions",
                    onClick = { viewModel.savePermissions(userId) },
                    modifier = Modifier.fillMaxWidth()
                )
            }
        }
    }
}
