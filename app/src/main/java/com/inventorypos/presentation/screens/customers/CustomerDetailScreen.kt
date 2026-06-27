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
import com.inventorypos.presentation.navigation.Screen
import com.inventorypos.presentation.theme.*
import com.inventorypos.utils.CurrencyFormatter

@Composable
fun DetailCard(title: String, value: String) {
    androidx.compose.material3.Card(
        modifier = androidx.compose.ui.Modifier.fillMaxWidth().padding(vertical = 4.dp),
        colors = androidx.compose.material3.CardDefaults.cardColors(
            containerColor = androidx.compose.material3.MaterialTheme.colorScheme.surfaceVariant
        )
    ) {
        androidx.compose.foundation.layout.Column(modifier = androidx.compose.ui.Modifier.padding(16.dp)) {
            androidx.compose.material3.Text(text = title, style = androidx.compose.material3.MaterialTheme.typography.labelMedium)
            androidx.compose.material3.Text(text = value, style = androidx.compose.material3.MaterialTheme.typography.bodyLarge)
        }
    }
}

@Composable
fun CustomerDetailScreen(
    navController: NavController,
    customerId: Long,
    viewModel: CustomerDetailViewModel = hiltViewModel()
) {
    LaunchedEffect(customerId) { viewModel.loadCustomer(customerId) }

    val customer by viewModel.customer.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()

    Scaffold(
        topBar = {
            CustomTopBar(
                title = "Customer Detail",
                subtitle = "ID: #$customerId",
                onBackClick = { navController.popBackStack() },
                actions = {
                    IconButton(onClick = { navController.navigate(Screen.CustomerEdit.createRoute(customerId)) }) {
                        Icon(Icons.Default.Edit, "Edit", tint = PremiumGold)
                    }
                }
            )
        },
        containerColor = PremiumDarkBackground
    ) { padding ->
        if (isLoading) {
            LoadingIndicator()
        } else if (customer == null) {
            ErrorState(message = "Customer not found")
        } else {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState())
                    .padding(padding)
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                DetailCard(Icons.Default.Person, "Name", customer!!.name)
                if (!customer!!.phone.isNullOrBlank()) DetailCard(Icons.Default.Phone, "Phone", customer!!.phone!!)
                if (!customer!!.email.isNullOrBlank()) DetailCard(Icons.Default.Email, "Email", customer!!.email!!)
                if (!customer!!.address.isNullOrBlank()) DetailCard(Icons.Default.LocationOn, "Address", customer!!.address!!)
                DetailCard(Icons.Default.Star, "Loyalty Points", customer!!.loyaltyPoints.toString())
                DetailCard(Icons.Default.AttachMoney, "Total Spent", CurrencyFormatter.format(customer!!.totalSpent))
            }
        }
    }
}
