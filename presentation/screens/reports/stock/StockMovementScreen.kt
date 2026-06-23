package com.inventorypos.presentation.screens.reports.stock

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
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
fun StockMovementScreen(
    navController: NavController,
    viewModel: StockMovementViewModel = hiltViewModel()
) {
    val movements by viewModel.movements.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()

    Scaffold(
        topBar = {
            CustomTopBar(
                title = "Stock Movement",
                subtitle = "In/Out history",
                onBackClick = { navController.popBackStack() }
            )
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
            } else {
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(16.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    items(movements.size) { index ->
                        val movement = movements[index]
                        Card(
                            modifier = Modifier.fillMaxWidth(),
                            shape = MaterialTheme.shapes.large,
                            colors = CardDefaults.cardColors(containerColor = PremiumDarkSurface)
                        ) {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(16.dp),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Column {
                                    Text(text = movement.productName, style = MaterialTheme.typography.bodyMedium, color = PremiumTextPrimary)
                                    Text(text = movement.date, style = MaterialTheme.typography.labelSmall, color = PremiumTextMuted)
                                }
                                Text(
                                    text = "${if (movement.type == "IN") "+" else "-"}${movement.quantity}",
                                    style = MaterialTheme.typography.titleSmall,
                                    color = if (movement.type == "IN") PremiumSuccess else PremiumError
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

data class StockMovement(val productName: String, val date: String, val type: String, val quantity: Int)
