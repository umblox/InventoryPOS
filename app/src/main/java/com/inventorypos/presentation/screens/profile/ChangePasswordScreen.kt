package com.inventorypos.presentation.screens.profile

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
fun ChangePasswordScreen(
    navController: NavController,
    viewModel: ChangePasswordViewModel = hiltViewModel()
) {
    val currentPassword by viewModel.currentPassword.collectAsState()
    val newPassword by viewModel.newPassword.collectAsState()
    val confirmPassword by viewModel.confirmPassword.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()
    val isSuccess by viewModel.isSuccess.collectAsState()
    val error by viewModel.error.collectAsState()

    LaunchedEffect(isSuccess) { if (isSuccess) navController.popBackStack() }

    Scaffold(
        topBar = {
            CustomTopBar(
                title = "Change Password",
                subtitle = "Update your password",
                onBackClick = { navController.popBackStack() }
            )
        },
        containerColor = PremiumDarkBackground
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            CustomTextField(
                value = currentPassword,
                onValueChange = viewModel::onCurrentPasswordChange,
                label = "Current Password",
                placeholder = "Enter current password",
                leadingIcon = Icons.Default.Lock,
                isPassword = true
            )

            CustomTextField(
                value = newPassword,
                onValueChange = viewModel::onNewPasswordChange,
                label = "New Password",
                placeholder = "Enter new password",
                leadingIcon = Icons.Default.Lock,
                isPassword = true
            )

            CustomTextField(
                value = confirmPassword,
                onValueChange = viewModel::onConfirmPasswordChange,
                label = "Confirm New Password",
                placeholder = "Confirm new password",
                leadingIcon = Icons.Default.Lock,
                isPassword = true
            )

            if (error != null) {
                Text(text = error!!, color = PremiumError, style = MaterialTheme.typography.bodySmall)
            }

            Spacer(modifier = Modifier.height(8.dp))

            CustomButton(
                text = "Update Password",
                onClick = viewModel::changePassword,
                isLoading = isLoading,
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}
