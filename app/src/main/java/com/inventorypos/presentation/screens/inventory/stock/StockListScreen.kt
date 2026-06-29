package com.inventorypos.presentation.screens.inventory.stock

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.inventorypos.domain.model.SmartPoItem
import com.inventorypos.presentation.components.common.*
import com.inventorypos.presentation.navigation.Screen
import com.inventorypos.presentation.theme.*

@Composable
fun StockListScreen(
    navController: NavController,
    viewModel: StockListViewModel = hiltViewModel()
) {
    val lowStockItems by viewModel.lowStockItems.collectAsState()
    
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
                subtitle = "Manage inventory & PO",
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
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // === SECTION: Smart PO Recommendations ===
            if (lowStockItems.isNotEmpty()) {
                item {
                    Text("Restock Recommendations", style = MaterialTheme.typography.titleLarge, color = PremiumGold)
                    Spacer(modifier = Modifier.height(8.dp))
                }
                items(lowStockItems) { item ->
                    SmartPoCard(item = item)
                }
                item { Divider(color = PremiumTextMuted.copy(alpha = 0.2f), thickness = 1.dp) }
            }

            // === SECTION: Stock Actions ===
            item {
                Text("Stock Actions", style = MaterialTheme.typography.titleLarge, color = PremiumTextPrimary)
                Spacer(modifier = Modifier.height(8.dp))
            }
            
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
                        horizontalArrangement = Arrangement.spacedBy(16.dp),
                        verticalAlignment = Alignment.CenterVertically
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

@Composable
fun SmartPoCard(item: SmartPoItem) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = MaterialTheme.shapes.large,
        colors = CardDefaults.cardColors(containerColor = PremiumDarkSurface)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(item.product.name, style = MaterialTheme.typography.titleMedium, color = PremiumTextPrimary)
            Text("Current Stock: ${item.product.stock} (Min: ${item.product.minStock})", style = MaterialTheme.typography.bodySmall, color = PremiumError)
            
            Spacer(modifier = Modifier.height(8.dp))
            
            val supplier = item.recommendedSupplier
            if (supplier != null) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Default.LocalShipping, null, tint = PremiumGold, modifier = Modifier.size(16.dp))
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = "Order to: ${supplier.supplierName} @ Rp${supplier.buyPrice.toInt()}",
                        style = MaterialTheme.typography.bodySmall, color = PremiumGold
                    )
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
