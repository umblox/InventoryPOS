package com.inventorypos.presentation.screens.reports.finance

import androidx.compose.foundation.layout.*
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
fun ProfitLossScreen(
    navController: NavController,
    viewModel: ProfitLossViewModel = hiltViewModel()
) {
    val data by viewModel.data.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()

    Scaffold(
        topBar = {
            CustomTopBar(
                title = "Profit & Loss",
                subtitle = "Financial overview",
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
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    FinanceCard("Total Revenue", data.revenue, PremiumSuccess)
                    FinanceCard("Total COGS", data.cogs, PremiumError)
                    FinanceCard("Gross Profit", data.profit, PremiumGold)
                    FinanceCard("Operating Expenses", data.expenses, PremiumWarning)
                    FinanceCard("Net Profit", data.netProfit, if (data.netProfit >= 0) PremiumSuccess else PremiumError)
                }
            }
        }
    }
}

@Composable
fun FinanceCard(label: String, amount: Double, color: androidx.compose.ui.graphics.Color) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = MaterialTheme.shapes.large,
        colors = CardDefaults.cardColors(containerColor = PremiumDarkSurface)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(text = label, style = MaterialTheme.typography.bodyLarge, color = PremiumTextSecondary)
            Text(
                text = CurrencyFormatter.format(amount),
                style = MaterialTheme.typography.titleMedium,
                color = color,
                fontWeight = androidx.compose.ui.text.font.FontWeight.Bold
            )
        }
    }
}

data class ProfitLossData(
    val revenue: Double,
    val cogs: Double,
    val profit: Double,
    val expenses: Double,
    val netProfit: Double
)
