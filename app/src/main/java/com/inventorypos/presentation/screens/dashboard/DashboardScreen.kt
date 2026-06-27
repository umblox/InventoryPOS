package com.inventorypos.presentation.screens.dashboard

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.clickable
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.inventorypos.presentation.components.common.CustomTopBar
import com.inventorypos.presentation.navigation.Screen
import com.inventorypos.presentation.screens.dashboard.widgets.*
import com.inventorypos.presentation.theme.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DashboardScreen(
    navController: NavController,
    viewModel: DashboardViewModel = hiltViewModel()
) {
    val todaySales by viewModel.todaySales.collectAsState()
    val transactionCount by viewModel.transactionCount.collectAsState()
    val lowStockCount by viewModel.lowStockCount.collectAsState()
    val productCount by viewModel.productCount.collectAsState()
    val recentTransactions by viewModel.recentTransactions.collectAsState()
    
    Scaffold(
        topBar = {
            CustomTopBar(
                title = "Dashboard",
                subtitle = "Overview of your business",
                showBackButton = false,
                actions = {
                    IconButton(onClick = { navController.navigate(Screen.Profile.route) }) {
                        Icon(
                            Icons.Default.AccountCircle,
                            "Profile",
                            tint = PremiumGold,
                            modifier = Modifier.size(28.dp)
                        )
                    }
                    IconButton(onClick = { /* Notifications */ }) {
                        BadgedBox(
                            badge = {
                                Badge(
                                    containerColor = PremiumError,
                                    contentColor = PremiumTextPrimary
                                ) {
                                    Text("3", style = MaterialTheme.typography.labelSmall)
                                }
                            }
                        ) {
                            Icon(
                                Icons.Default.Notifications,
                                "Notifications",
                                tint = PremiumGold
                            )
                        }
                    }
                }
            )
        },
        containerColor = PremiumDarkBackground
    ) { padding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Welcome Banner
            item {
                WelcomeBanner(userName = "Admin")
            }
            
            // Summary Cards Row
            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    SummaryCard(
                        title = "Today's Sales",
                        value = "Rp ${String.format("%,.0f", todaySales)}",
                        icon = Icons.Default.AttachMoney,
                        gradient = Brush.linearGradient(
                            colors = listOf(PremiumGold.copy(alpha = 0.3f), PremiumGold.copy(alpha = 0.1f))
                        ),
                        modifier = Modifier.weight(1f)
                    )
                    SummaryCard(
                        title = "Transactions",
                        value = transactionCount.toString(),
                        icon = Icons.Default.ReceiptLong,
                        gradient = Brush.linearGradient(
                            colors = listOf(PremiumAccent.copy(alpha = 0.3f), PremiumAccent.copy(alpha = 0.1f))
                        ),
                        modifier = Modifier.weight(1f)
                    )
                }
            }
            
            // Second Row
            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    SummaryCard(
                        title = "Products",
                        value = productCount.toString(),
                        icon = Icons.Default.Inventory,
                        gradient = Brush.linearGradient(
                            colors = listOf(PremiumInfo.copy(alpha = 0.3f), PremiumInfo.copy(alpha = 0.1f))
                        ),
                        modifier = Modifier.weight(1f)
                    )
                    SummaryCard(
                        title = "Low Stock",
                        value = lowStockCount.toString(),
                        icon = Icons.Default.Warning,
                        gradient = Brush.linearGradient(
                            colors = listOf(PremiumError.copy(alpha = 0.3f), PremiumError.copy(alpha = 0.1f))
                        ),
                        modifier = Modifier.weight(1f),
                        onClick = { navController.navigate(Screen.StockList.route) }
                    )
                }
            }
            
            // Quick Actions
            item {
                QuickActionsPanel(navController)
            }
            
            // Recent Transactions
            item {
                RecentTransactionsCard(
                    transactions = recentTransactions,
                    onViewAll = { navController.navigate(Screen.SalesReport.route) },
                    onTransactionClick = { /* Detail */ }
                )
            }
            
            // Top Products Chart
            item {
                TopProductsCard()
            }
            
            Spacer(modifier = Modifier.height(32.dp))
        }
    }
}

@Composable
fun WelcomeBanner(userName: String) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(20.dp))
            .background(
                Brush.linearGradient(
                    colors = listOf(
                        PremiumGold.copy(alpha = 0.2f),
                        PremiumDarkSurface
                    )
                )
            )
            .padding(20.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier.fillMaxWidth()
        ) {
            Column {
                Text(
                    text = "Welcome back,",
                    style = MaterialTheme.typography.bodyMedium,
                    color = PremiumTextSecondary
                )
                Text(
                    text = userName,
                    style = MaterialTheme.typography.headlineSmall,
                    color = PremiumGold,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = "Ready to manage your business?",
                    style = MaterialTheme.typography.bodySmall,
                    color = PremiumTextMuted
                )
            }
            Box(
                modifier = Modifier
                    .size(56.dp)
                    .clip(RoundedCornerShape(16.dp))
                    .background(PremiumGold.copy(alpha = 0.2f)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    Icons.Default.Store,
                    contentDescription = "Store",
                    tint = PremiumGold,
                    modifier = Modifier.size(28.dp)
                )
            }
        }
    }
}

@Composable
fun SummaryCard(
    title: String,
    value: String,
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    gradient: Brush,
    modifier: Modifier = Modifier,
    onClick: (() -> Unit)? = null
) {
    Card(
        onClick = { onClick?.invoke() },
        modifier = modifier,
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = PremiumDarkSurface),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(gradient)
                .padding(16.dp)
        ) {
            Column {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .size(36.dp)
                            .clip(RoundedCornerShape(10.dp))
                            .background(PremiumDarkBackground.copy(alpha = 0.5f)),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            icon,
                            contentDescription = title,
                            tint = PremiumTextPrimary,
                            modifier = Modifier.size(20.dp)
                        )
                    }
                    Text(
                        text = title,
                        style = MaterialTheme.typography.labelMedium,
                        color = PremiumTextSecondary
                    )
                }
                Spacer(modifier = Modifier.height(12.dp))
                Text(
                    text = value,
                    style = MaterialTheme.typography.headlineSmall,
                    color = PremiumTextPrimary,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}

@Composable
fun QuickActionsPanel(navController: NavController) {
    Column(
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Text(
            text = "Quick Actions",
            style = MaterialTheme.typography.titleMedium,
            color = PremiumTextPrimary
        )
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            QuickActionButton(
                icon = Icons.Default.PointOfSale,
                label = "New Sale",
                color = PremiumGold,
                modifier = Modifier.weight(1f),
                onClick = { navController.navigate(Screen.POS.route) }
            )
            QuickActionButton(
                icon = Icons.Default.Add,
                label = "Add Product",
                color = PremiumAccent,
                modifier = Modifier.weight(1f),
                onClick = { navController.navigate(Screen.ProductAdd.route) }
            )
            QuickActionButton(
                icon = Icons.Default.Inventory,
                label = "Stock In",
                color = PremiumInfo,
                modifier = Modifier.weight(1f),
                onClick = { navController.navigate(Screen.StockIn.route) }
            )
            QuickActionButton(
                icon = Icons.Default.People,
                label = "Customer",
                color = PremiumWarning,
                modifier = Modifier.weight(1f),
                onClick = { navController.navigate(Screen.CustomerAdd.route) }
            )
        }
    }
}

@Composable
fun QuickActionButton(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    label: String,
    color: androidx.compose.ui.graphics.Color,
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    Card(
        onClick = onClick,
        modifier = modifier,
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = PremiumDarkSurface),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Box(
                modifier = Modifier
                    .size(44.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .background(color.copy(alpha = 0.15f)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    icon,
                    contentDescription = label,
                    tint = color,
                    modifier = Modifier.size(22.dp)
                )
            }
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = label,
                style = MaterialTheme.typography.labelSmall,
                color = PremiumTextSecondary
            )
        }
    }
}
