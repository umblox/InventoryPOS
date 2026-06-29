package com.inventorypos.presentation.screens.inventory.supplier

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
import androidx.compose.ui.Alignment
import com.inventorypos.domain.model.Supplier
import com.inventorypos.presentation.components.common.*
import com.inventorypos.presentation.navigation.Screen
import com.inventorypos.presentation.theme.*

@Composable
fun SupplierListScreen(
    navController: NavController,
    viewModel: SupplierListViewModel = hiltViewModel()
) {
    val suppliers by viewModel.suppliers.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()

    Scaffold(
        topBar = {
            CustomTopBar(
                title = "Suppliers",
                subtitle = "${suppliers.size} suppliers",
                onBackClick = { navController.popBackStack() }
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = { navController.navigate(Screen.SupplierAdd.route) },
                containerColor = PremiumGold,
                contentColor = PremiumDarkBackground
            ) {
                Icon(Icons.Default.Add, "Add Supplier")
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
            } else if (suppliers.isEmpty()) {
                EmptyState(
                    icon = Icons.Default.LocalShipping,
                    title = "No Suppliers",
                    message = "Add your first supplier"
                )
            } else {
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(16.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    items(suppliers) { supplier ->
                        SupplierCard(
                            supplier = supplier,
                            onClick = { navController.navigate(Screen.SupplierDetail.createRoute(supplier.id)) },
                            onEdit = { navController.navigate(Screen.SupplierEdit.createRoute(supplier.id)) }
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun SupplierCard(
    supplier: Supplier,
    onClick: () -> Unit,
    onEdit: () -> Unit
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
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = supplier.name,
                    style = MaterialTheme.typography.titleMedium,
                    color = PremiumTextPrimary
                )
                if (!supplier.phone.isNullOrBlank()) {
                    Text(
                        text = supplier.phone,
                        style = MaterialTheme.typography.bodySmall,
                        color = PremiumTextMuted
                    )
                }
            }
            IconButton(onClick = onEdit) {
                Icon(Icons.Default.Edit, "Edit", tint = PremiumInfo)
            }
        }
    }
}
