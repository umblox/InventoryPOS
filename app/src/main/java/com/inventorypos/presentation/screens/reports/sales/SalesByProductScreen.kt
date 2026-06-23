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
fun SalesByProductScreen(
    navController: NavController,
    viewModel: SalesByProductViewModel = hiltViewModel()
) {
    val sales by viewModel.sales.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()

    Scaffold(
        topBar = {
            CustomTopBar(
                title = "Sales by Product",
                subtitle = "Top selling products",
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
                    items(sales.size) { index ->
                        val item = sales[index]
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
                                Column(modifier = Modifier.weight(1f)) {
                                    Text(text = item.productName, style = MaterialTheme.typography.bodyMedium, color = PremiumTextPrimary)
                                    Text(text = "${item.quantitySold} sold", style = MaterialTheme.typography.labelSmall, color = PremiumTextMuted)
                                }
                                Text(
                                    text = CurrencyFormatter.format(item.revenue),
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

data class ProductSale(val productName: String, val quantitySold: Int, val revenue: Double)
