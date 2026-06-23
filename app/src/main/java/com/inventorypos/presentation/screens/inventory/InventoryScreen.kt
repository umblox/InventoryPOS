package com.inventorypos.presentation.screens.inventory

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.inventorypos.presentation.components.common.CustomTopBar
import com.inventorypos.presentation.navigation.Screen
import com.inventorypos.presentation.theme.*

@Composable
fun InventoryScreen(navController: NavController) {
    val menuItems = listOf(
        InventoryMenuItem("Products", "Manage your products", Icons.Default.Inventory, Screen.ProductList.route, PremiumGold),
        InventoryMenuItem("Categories", "Organize by category", Icons.Default.Category, Screen.CategoryList.route, PremiumInfo),
        InventoryMenuItem("Stock", "Stock in/out/transfer", Icons.Default.SyncAlt, Screen.StockList.route, PremiumSuccess),
        InventoryMenuItem("Suppliers", "Manage suppliers", Icons.Default.LocalShipping, Screen.SupplierList.route, PremiumWarning),
        InventoryMenuItem("Stock Opname", "Physical count", Icons.Default.FactCheck, Screen.StockOpname.route, PremiumAccent),
        InventoryMenuItem("Barcode", "Generate & print", Icons.Default.QrCode, Screen.ProductList.route, PremiumTextSecondary)
    )
    
    Scaffold(
        topBar = {
            CustomTopBar(
                title = "Inventory",
                subtitle = "Manage your stock",
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
            items(menuItems.size) { index ->
                val item = menuItems[index]
                InventoryMenuCard(
                    item = item,
                    onClick = { navController.navigate(item.route) }
                )
            }
        }
    }
}

@Composable
fun InventoryMenuCard(
    item: InventoryMenuItem,
    onClick: () -> Unit
) {
    Card(
        onClick = onClick,
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = PremiumDarkSurface),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp),
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(52.dp)
                    .clip(RoundedCornerShape(14.dp))
                    .background(item.color.copy(alpha = 0.15f)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = item.icon,
                    contentDescription = item.title,
                    tint = item.color,
                    modifier = Modifier.size(26.dp)
                )
            }
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = item.title,
                    style = MaterialTheme.typography.titleMedium,
                    color = PremiumTextPrimary,
                    fontWeight = FontWeight.SemiBold
                )
                Text(
                    text = item.description,
                    style = MaterialTheme.typography.bodySmall,
                    color = PremiumTextMuted
                )
            }
            Icon(
                Icons.Default.ChevronRight,
                contentDescription = null,
                tint = PremiumTextMuted,
                modifier = Modifier.size(24.dp)
            )
        }
    }
}

data class InventoryMenuItem(
    val title: String,
    val description: String,
    val icon: ImageVector,
    val route: String,
    val color: androidx.compose.ui.graphics.Color
)
