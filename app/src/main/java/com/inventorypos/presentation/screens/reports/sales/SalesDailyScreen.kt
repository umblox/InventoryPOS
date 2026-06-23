package com.inventorypos.presentation.screens.reports.sales

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
import com.inventorypos.utils.CurrencyFormatter

@Composable
fun SalesDailyScreen(
    navController: NavController,
    viewModel: SalesDailyViewModel = hiltViewModel()
) {
    val dailySales by viewModel.dailySales.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()

    Scaffold(
        topBar = {
            CustomTopBar(
                title = "Daily Sales",
                subtitle = "Sales by day",
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
                    items(dailySales.size) { index ->
                        val sale = dailySales[index]
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
                                    Text(text = sale.date, style = MaterialTheme.typography.bodyMedium, color = PremiumTextSecondary)
                                    Text(text = "${sale.transactionCount} transactions", style = MaterialTheme.typography.labelSmall, color = PremiumTextMuted)
                                }
                                Text(
                                    text = CurrencyFormatter.format(sale.total),
                                    style = MaterialTheme.typography.titleSmall,
                                    color = PremiumGold
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

data class DailySale(val date: String, val total: Double, val transactionCount: Int)
