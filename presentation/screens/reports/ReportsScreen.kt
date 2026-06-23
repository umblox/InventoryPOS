package com.inventorypos.presentation.screens.reports

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.inventorypos.presentation.components.common.CustomTopBar
import com.inventorypos.presentation.navigation.Screen
import com.inventorypos.presentation.theme.*

@Composable
fun ReportsScreen(navController: NavController) {
    val reportMenus = listOf(
        ReportMenu("Sales Reports", "Daily, monthly, by product", Icons.Default.TrendingUp, Screen.SalesReport.route, PremiumGold),
        ReportMenu("Stock Reports", "Movement & history", Icons.Default.Inventory, Screen.StockReport.route, PremiumInfo),
        ReportMenu("Financial", "Profit/loss & cash flow", Icons.Default.AccountBalance, Screen.ProfitLoss.route, PremiumSuccess)
    )

    Scaffold(
        topBar = {
            CustomTopBar(
                title = "Reports",
                subtitle = "Business analytics",
                showBackButton = false
            )
        },
        containerColor = PremiumDarkBackground
    ) { padding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            items(reportMenus.size) { index ->
                val menu = reportMenus[index]
                Card(
                    onClick = { navController.navigate(menu.route) },
                    modifier = Modifier.fillMaxWidth(),
                    shape = MaterialTheme.shapes.large,
                    colors = CardDefaults.cardColors(containerColor = PremiumDarkSurface)
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(20.dp),
                        horizontalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        Icon(menu.icon, contentDescription = menu.title, tint = menu.color, modifier = Modifier.size(32.dp))
                        Column {
                            Text(text = menu.title, style = MaterialTheme.typography.titleMedium, color = PremiumTextPrimary)
                            Text(text = menu.description, style = MaterialTheme.typography.bodySmall, color = PremiumTextMuted)
                        }
                    }
                }
            }
        }
    }
}

data class ReportMenu(
    val title: String,
    val description: String,
    val icon: androidx.compose.ui.graphics.vector.ImageVector,
    val route: String,
    val color: androidx.compose.ui.graphics.Color
)
