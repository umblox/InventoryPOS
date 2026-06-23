package com.inventorypos.presentation.screens.customers

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
fun CustomerAddScreen(
    navController: NavController,
    viewModel: CustomerAddViewModel = hiltViewModel()
) {
    val name by viewModel.name.collectAsState()
    val phone by viewModel.phone.collectAsState()
    val email by viewModel.email.collectAsState()
    val address by viewModel.address.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()
    val isSuccess by viewModel.isSuccess.collectAsState()
    val error by viewModel.error.collectAsState()

    LaunchedEffect(isSuccess) { if (isSuccess) navController.popBackStack() }

    Scaffold(
        topBar = {
            CustomTopBar(
                title = "Add Customer",
                subtitle = "Create new customer",
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
                value = name,
                onValueChange = viewModel::onNameChange,
                label = "Customer Name *",
                placeholder = "Enter customer name",
                leadingIcon = Icons.Default.Person
            )

            CustomTextField(
                value = phone,
                onValueChange = viewModel::onPhoneChange,
                label = "Phone",
                placeholder = "Enter phone number",
                leadingIcon = Icons.Default.Phone
            )

            CustomTextField(
                value = email,
                onValueChange = viewModel::onEmailChange,
                label = "Email",
                placeholder = "Enter email address",
                leadingIcon = Icons.Default.Email
            )

            CustomTextField(
                value = address,
                onValueChange = viewModel::onAddressChange,
                label = "Address",
                placeholder = "Enter address",
                leadingIcon = Icons.Default.LocationOn,
                maxLines = 3,
                singleLine = false
            )

            if (error != null) {
                Text(text = error!!, color = PremiumError, style = MaterialTheme.typography.bodySmall)
            }

            Spacer(modifier = Modifier.height(8.dp))

            CustomButton(
                text = "Save Customer",
                onClick = viewModel::saveCustomer,
                isLoading = isLoading,
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}
