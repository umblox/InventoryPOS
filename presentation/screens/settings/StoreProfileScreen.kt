package com.inventorypos.presentation.screens.settings

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
fun StoreProfileScreen(
    navController: NavController,
    viewModel: StoreProfileViewModel = hiltViewModel()
) {
    val storeName by viewModel.storeName.collectAsState()
    val address by viewModel.address.collectAsState()
    val phone by viewModel.phone.collectAsState()
    val taxRate by viewModel.taxRate.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()
    val isSuccess by viewModel.isSuccess.collectAsState()

    LaunchedEffect(isSuccess) { if (isSuccess) navController.popBackStack() }

    Scaffold(
        topBar = {
            CustomTopBar(
                title = "Store Profile",
                subtitle = "Business information",
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
                value = storeName,
                onValueChange = viewModel::onStoreNameChange,
                label = "Store Name",
                placeholder = "Your business name",
                leadingIcon = Icons.Default.Store
            )

            CustomTextField(
                value = address,
                onValueChange = viewModel::onAddressChange,
                label = "Address",
                placeholder = "Business address",
                leadingIcon = Icons.Default.LocationOn,
                maxLines = 3,
                singleLine = false
            )

            CustomTextField(
                value = phone,
                onValueChange = viewModel::onPhoneChange,
                label = "Phone",
                placeholder = "Business phone",
                leadingIcon = Icons.Default.Phone
            )

            CustomTextField(
                value = taxRate,
                onValueChange = viewModel::onTaxRateChange,
                label = "Tax Rate (%)",
                placeholder = "10",
                leadingIcon = Icons.Default.Percent
            )

            Spacer(modifier = Modifier.height(8.dp))

            CustomButton(
                text = "Save Profile",
                onClick = viewModel::saveProfile,
                isLoading = isLoading,
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}
