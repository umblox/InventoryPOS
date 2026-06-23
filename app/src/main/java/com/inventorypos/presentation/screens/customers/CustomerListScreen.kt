package com.inventorypos.presentation.screens.customers

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
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

@Composable
fun CustomerListScreen(
    navController: NavController,
    viewModel: CustomerListViewModel = hiltViewModel()
) {
    val customers by viewModel.customers.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()

    Scaffold(
        topBar = {
            CustomTopBar(
                title = "Customers",
                subtitle = "${customers.size} customers",
                showBackButton = false
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = { navController.navigate(Screen.CustomerAdd.route) },
                containerColor = PremiumGold,
                contentColor = PremiumDarkBackground
            ) {
                Icon(Icons.Default.Add, "Add Customer")
            }
        },
        containerColor = PremiumDarkBackground
    ) { padding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
        ) {
            if (isLoading) {
                LoadingIndicator()
            } else if (customers.isEmpty()) {
                EmptyState(
                    icon = Icons.Default.People,
                    title = "No Customers",
                    message = "Add your first customer"
                )
            } else {
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(16.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    items(customers) { customer ->
                        CustomerCard(
                            customer = customer,
                            onClick = { navController.navigate(Screen.CustomerDetail.createRoute(customer.id)) },
                            onEdit = { navController.navigate(Screen.CustomerEdit.createRoute(customer.id)) },
                            onDelete = { navController.navigate(Screen.CustomerDelete.createRoute(customer.id)) }
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun CustomerCard(
    customer: com.inventorypos.domain.model.Customer,
    onClick: () -> Unit,
    onEdit: () -> Unit,
    onDelete: () -> Unit
) {
    Card(
        onClick = onClick,
        modifier = Modifier.fillMaxWidth(),
        shape = MaterialTheme.shapes.large,
        colors = CardDefaults.cardColors(containerColor = PremiumDarkSurface)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = androidx.compose.ui.Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = customer.name,
                    style = MaterialTheme.typography.titleMedium,
                    color = PremiumTextPrimary
                )
                if (!customer.phone.isNullOrBlank()) {
                    Text(
                        text = customer.phone,
                        style = MaterialTheme.typography.bodySmall,
                        color = PremiumTextMuted
                    )
                }
                Text(
                    text = "${customer.loyaltyPoints} points",
                    style = MaterialTheme.typography.labelSmall,
                    color = PremiumGold
                )
            }
            Row {
                IconButton(onClick = onEdit) {
                    Icon(Icons.Default.Edit, "Edit", tint = PremiumInfo)
                }
                IconButton(onClick = onDelete) {
                    Icon(Icons.Default.Delete, "Delete", tint = PremiumError)
                }
            }
        }
    }
}
