package com.inventorypos.presentation.screens.inventory.supplier

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

@Composable
fun SupplierDetailScreen(
    navController: NavController,
    supplierId: Long,
    viewModel: SupplierDetailViewModel = hiltViewModel()
) {
    LaunchedEffect(supplierId) { viewModel.loadSupplier(supplierId) }

    val supplier by viewModel.supplier.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()

    Scaffold(
        topBar = {
            CustomTopBar(
                title = "Supplier Detail",
                subtitle = "ID: #$supplierId",
                onBackClick = { navController.popBackStack() },
                actions = {
                    IconButton(onClick = { navController.navigate(Screen.SupplierEdit.createRoute(supplierId)) }) {
                        Icon(Icons.Default.Edit, "Edit", tint = PremiumGold)
                    }
                }
            )
        },
        containerColor = PremiumDarkBackground
    ) { padding ->
        if (isLoading) {
            LoadingIndicator()
        } else if (supplier == null) {
            ErrorState(message = "Supplier not found")
        } else {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState())
                    .padding(padding)
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                DetailCard(
                    icon = Icons.Default.Business,
                    label = "Name",
                    value = supplier!!.name
                )
                if (!supplier!!.phone.isNullOrBlank()) {
                    DetailCard(
                        icon = Icons.Default.Phone,
                        label = "Phone",
                        value = supplier!!.phone!!
                    )
                }
                if (!supplier!!.email.isNullOrBlank()) {
                    DetailCard(
                        icon = Icons.Default.Email,
                        label = "Email",
                        value = supplier!!.email!!
                    )
                }
                if (!supplier!!.address.isNullOrBlank()) {
                    DetailCard(
                        icon = Icons.Default.LocationOn,
                        label = "Address",
                        value = supplier!!.address!!
                    )
                }
            }
        }
    }
}

@Composable
fun DetailCard(icon: androidx.compose.ui.graphics.vector.ImageVector, label: String, value: String) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = MaterialTheme.shapes.large,
        colors = CardDefaults.cardColors(containerColor = PremiumDarkSurface)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Icon(icon, contentDescription = null, tint = PremiumGold)
            Column {
                Text(text = label, style = MaterialTheme.typography.labelSmall, color = PremiumTextMuted)
                Text(text = value, style = MaterialTheme.typography.bodyLarge, color = PremiumTextPrimary)
            }
        }
    }
}
