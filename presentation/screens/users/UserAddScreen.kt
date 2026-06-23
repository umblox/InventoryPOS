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
import com.inventorypos.presentation.components.common.*
import com.inventorypos.presentation.theme.*

@Composable
fun UserAddScreen(
    navController: NavController,
    viewModel: UserAddViewModel = hiltViewModel()
) {
    val fullName by viewModel.fullName.collectAsState()
    val username by viewModel.username.collectAsState()
    val password by viewModel.password.collectAsState()
    val role by viewModel.role.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()
    val isSuccess by viewModel.isSuccess.collectAsState()
    val error by viewModel.error.collectAsState()

    LaunchedEffect(isSuccess) { if (isSuccess) navController.popBackStack() }

    Scaffold(
        topBar = {
            CustomTopBar(
                title = "Add User",
                subtitle = "Create new user account",
                onBackClick = { navController.popBackStack() }
            )
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
            CustomTextField(
                value = fullName,
                onValueChange = viewModel::onFullNameChange,
                label = "Full Name *",
                placeholder = "Enter full name",
                leadingIcon = Icons.Default.Person
            )

            CustomTextField(
                value = username,
                onValueChange = viewModel::onUsernameChange,
                label = "Username *",
                placeholder = "Enter username",
                leadingIcon = Icons.Default.AccountCircle
            )

            CustomTextField(
                value = password,
                onValueChange = viewModel::onPasswordChange,
                label = "Password *",
                placeholder = "Enter password",
                leadingIcon = Icons.Default.Lock,
                isPassword = true
            )

            // Role dropdown (simplified)
            CustomTextField(
                value = role,
                onValueChange = viewModel::onRoleChange,
                label = "Role *",
                placeholder = "CASHIER, ADMIN, etc.",
                leadingIcon = Icons.Default.Security
            )

            if (error != null) {
                Text(text = error!!, color = PremiumError, style = MaterialTheme.typography.bodySmall)
            }

            Spacer(modifier = Modifier.height(8.dp))

            CustomButton(
                text = "Save User",
                onClick = viewModel::saveUser,
                isLoading = isLoading,
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}
