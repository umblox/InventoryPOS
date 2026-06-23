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
fun CashFlowScreen(
    navController: NavController,
    viewModel: CashFlowViewModel = hiltViewModel()
) {
    val data by viewModel.data.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()

    Scaffold(
        topBar = {
            CustomTopBar(
                title = "Cash Flow",
                subtitle = "Inflow & outflow",
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
                    FinanceCard("Cash Inflow", data.inflow, PremiumSuccess)
                    FinanceCard("Cash Outflow", data.outflow, PremiumError)
                    FinanceCard("Net Cash Flow", data.netFlow, if (data.netFlow >= 0) PremiumGold else PremiumError)
                }
            }
        }
    }
}

data class CashFlowData(val inflow: Double, val outflow: Double, val netFlow: Double)
