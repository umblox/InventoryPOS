package com.inventorypos.presentation.screens.inventory.stock

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.inventorypos.presentation.components.common.*
import com.inventorypos.presentation.navigation.Screen
import com.inventorypos.presentation.theme.*

@Composable
fun StockListScreen(navController: NavController) {
    val actions = listOf(
        StockAction("Stock In", "Receive from supplier", Icons.Default.ArrowDownward, Screen.StockIn.route, PremiumSuccess),
        StockAction("Stock Out", "Remove from inventory", Icons.Default.ArrowUpward, Screen.StockOut.route, PremiumError),
        StockAction("Transfer", "Move between locations", Icons.Default.SyncAlt, Screen.StockTransfer.route, PremiumInfo),
        StockAction("Stock Opname", "Physical count", Icons.Default.FactCheck, Screen.StockOpname.route, PremiumWarning),
        StockAction("Adjustment", "Fix stock discrepancies", Icons.Default.Build, Screen.StockAdjustment.route, PremiumAccent)
    )
    
    Scaffold(
        topBar = {
            CustomTopBar(
                title = "Stock Management",
                subtitle = "Choose an action",
                onBackClick = { navController.popBackStack() }
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
            items(actions.size) { index ->
                val action = actions[index]
                Card(
                    onClick = { navController.navigate(action.route) },
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
                        Icon(
                            action.icon,
                            contentDescription = action.title,
                            tint = action.color,
                            modifier = Modifier.size(32.dp)
                        )
                        Column {
                            Text(
                                text = action.title,
                                style = MaterialTheme.typography.titleMedium,
                                color = PremiumTextPrimary
                            )
                            Text(
                                text = action.description,
                                style = MaterialTheme.typography.bodySmall,
                                color = PremiumTextMuted
                            )
                        }
                    }
                }
            }
        }
    }
}

data class StockAction(
    val title: String,
    val description: String,
    val icon: androidx.compose.ui.graphics.vector.ImageVector,
    val route: String,
    val color: androidx.compose.ui.graphics.Color
)
