package com.inventorypos.presentation.screens.reports.sales

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
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
fun SalesMonthlyScreen(
    navController: NavController,
    viewModel: SalesMonthlyViewModel = hiltViewModel()
) {
    val monthlySales by viewModel.monthlySales.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()

    Scaffold(
        topBar = {
            CustomTopBar(
                title = "Monthly Sales",
                subtitle = "Sales by month",
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
                    items(monthlySales.size) { index ->
                        val sale = monthlySales[index]
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
                                    Text(text = sale.month, style = MaterialTheme.typography.bodyMedium, color = PremiumTextSecondary)
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

data class MonthlySale(val month: String, val total: Double, val transactionCount: Int)
