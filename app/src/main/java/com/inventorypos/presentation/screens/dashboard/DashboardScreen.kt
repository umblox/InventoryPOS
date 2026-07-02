package com.inventorypos.presentation.screens.dashboard

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.inventorypos.data.local.entity.UserRole
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
    val currentUser by viewModel.currentUser.collectAsState()
    val permissions by viewModel.userPermissions.collectAsState()
    
    val todaySales by viewModel.todaySales.collectAsState()
    val transactionCount by viewModel.transactionCount.collectAsState()
    val lowStockCount by viewModel.lowStockCount.collectAsState()
    val productCount by viewModel.productCount.collectAsState()
    val recentTransactions by viewModel.recentTransactions.collectAsState()
    
    val context = LocalContext.current
    
    // Matriks Hak Akses (Granular Permissions)
    val isSuperAdmin = currentUser?.role == UserRole.SUPER_ADMIN
    val canViewDashboard = isSuperAdmin || permissions.contains("View Dashboard")
    val canManagePOS      = isSuperAdmin || permissions.contains("Manage POS")
    val canManageProducts = isSuperAdmin || permissions.contains("Manage Products")
    val canManageStock    = isSuperAdmin || permissions.contains("Manage Stock")
    val canViewReports    = isSuperAdmin || permissions.contains("View Reports")
    val canManageUsers    = isSuperAdmin || permissions.contains("Manage Users")
    val canManageSettings = isSuperAdmin || permissions.contains("Manage Settings")

    Scaffold(
        topBar = {
            CustomTopBar(
                title = "Dashboard",
                subtitle = "Overview of your business",
                showBackButton = false,
                actions = {
                    IconButton(onClick = { Toast.makeText(context, "Belum ada notifikasi baru", Toast.LENGTH_SHORT).show() }) {
                        BadgedBox(
                            badge = {
                                Badge(containerColor = PremiumError, contentColor = PremiumTextPrimary) {
                                    Text("3", style = MaterialTheme.typography.labelSmall)
                                }
                            }
                        ) {
                            Icon(Icons.Default.Notifications, "Notifications", tint = PremiumGold)
                        }
                    }
                    if (canManageSettings) {
                        IconButton(onClick = { navController.navigate(Screen.Settings.route) }) {
                            Icon(Icons.Default.Settings, "Settings", tint = PremiumGold, modifier = Modifier.size(24.dp))
                        }
                    }
                    IconButton(onClick = { navController.navigate(Screen.Profile.route) }) {
                        Icon(Icons.Default.AccountCircle, "Profile", tint = PremiumGold, modifier = Modifier.size(28.dp))
                    }
                }
            )
        },
        containerColor = PremiumDarkBackground
    ) { padding ->
        LazyColumn(
            modifier = Modifier.fillMaxSize().padding(padding),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            item {
                WelcomeBanner(userName = currentUser?.fullName ?: "Pegawai", roleName = currentUser?.role?.name ?: "Loading...")
            }
            
            if (canViewReports) {
                item {
                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                        SummaryCard("Today's Sales", "Rp ${String.format("%,.0f", todaySales)}", Icons.Default.AttachMoney, Brush.linearGradient(colors = listOf(PremiumGold.copy(alpha = 0.3f), PremiumGold.copy(alpha = 0.1f))), Modifier.weight(1f))
                        SummaryCard("Transactions", transactionCount.toString(), Icons.Default.ReceiptLong, Brush.linearGradient(colors = listOf(PremiumAccent.copy(alpha = 0.3f), PremiumAccent.copy(alpha = 0.1f))), Modifier.weight(1f))
                    }
                }
            }
            
            if (canManageStock || canManageProducts) {
                item {
                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                        SummaryCard("Products", productCount.toString(), Icons.Default.Inventory, Brush.linearGradient(colors = listOf(PremiumInfo.copy(alpha = 0.3f), PremiumInfo.copy(alpha = 0.1f))), Modifier.weight(1f))
                        SummaryCard("Low Stock", lowStockCount.toString(), Icons.Default.Warning, Brush.linearGradient(colors = listOf(PremiumError.copy(alpha = 0.3f), PremiumError.copy(alpha = 0.1f))), Modifier.weight(1f), onClick = { navController.navigate(Screen.SmartPo.route) })

                    }
                }
            }
            
            item {
                QuickActionsPanel(
                    navController = navController,
                    canManagePOS = canManagePOS,
                    canManageProducts = canManageProducts,
                    canManageStock = canManageStock,
                    canManageUsers = canManageUsers
                )
            }
            
            if (canViewReports) {
                item {
                    RecentTransactionsCard(transactions = recentTransactions, onViewAll = { navController.navigate(Screen.SalesReport.route) }, onTransactionClick = { })
                }
                item {
                    TopProductsCard()
                }
            }
            
            item { Spacer(modifier = Modifier.height(32.dp)) }
        }
    }
}

@Composable
fun WelcomeBanner(userName: String, roleName: String) {
    Box(
        modifier = Modifier.fillMaxWidth().clip(RoundedCornerShape(20.dp))
            .background(Brush.linearGradient(colors = listOf(PremiumGold.copy(alpha = 0.2f), PremiumDarkSurface)))
            .padding(20.dp)
    ) {
        Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.SpaceBetween, modifier = Modifier.fillMaxWidth()) {
            Column {
                Text(text = "Welcome back,", style = MaterialTheme.typography.bodyMedium, color = PremiumTextSecondary)
                Text(text = userName, style = MaterialTheme.typography.headlineSmall, color = PremiumGold, fontWeight = FontWeight.Bold)
                Text(text = "Login as: $roleName", style = MaterialTheme.typography.bodySmall, color = PremiumTextMuted)
            }
            Box(modifier = Modifier.size(56.dp).clip(RoundedCornerShape(16.dp)).background(PremiumGold.copy(alpha = 0.2f)), contentAlignment = Alignment.Center) {
                Icon(Icons.Default.Store, contentDescription = "Store", tint = PremiumGold, modifier = Modifier.size(28.dp))
            }
        }
    }
}

@Composable
fun SummaryCard(title: String, value: String, icon: androidx.compose.ui.graphics.vector.ImageVector, gradient: Brush, modifier: Modifier = Modifier, onClick: (() -> Unit)? = null) {
    Card(
        modifier = if (onClick != null) modifier.clickable { onClick() } else modifier,
        shape = RoundedCornerShape(16.dp), colors = CardDefaults.cardColors(containerColor = PremiumDarkSurface), elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) { 
        Box(modifier = Modifier.fillMaxWidth().background(gradient).padding(16.dp)) {
            Column {
                Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    Box(modifier = Modifier.size(36.dp).clip(RoundedCornerShape(10.dp)).background(PremiumDarkBackground.copy(alpha = 0.5f)), contentAlignment = Alignment.Center) {
                        Icon(icon, contentDescription = title, tint = PremiumTextPrimary, modifier = Modifier.size(20.dp))
                    }
                    Text(text = title, style = MaterialTheme.typography.labelMedium, color = PremiumTextSecondary)
                }
                Spacer(modifier = Modifier.height(12.dp))
                Text(text = value, style = MaterialTheme.typography.headlineSmall, color = PremiumTextPrimary, fontWeight = FontWeight.Bold)
            }
        }
    }
}

@Composable
fun QuickActionsPanel(
    navController: NavController,
    canManagePOS: Boolean,
    canManageProducts: Boolean,
    canManageStock: Boolean,
    canManageUsers: Boolean
) {
    Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
        Text("Quick Actions", style = MaterialTheme.typography.titleMedium, color = PremiumTextPrimary)
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(12.dp)) {
            if (canManagePOS) {
                QuickActionButton(Icons.Default.PointOfSale, "New Sale", PremiumGold, Modifier.weight(1f)) { navController.navigate(Screen.POS.route) }
            }
            if (canManageProducts) {
                QuickActionButton(Icons.Default.Inventory, "Inventory", PremiumAccent, Modifier.weight(1f)) { navController.navigate(Screen.Inventory.route) }
            }
            if (canManageStock) {
                QuickActionButton(Icons.Default.Inventory, "Stock In", PremiumInfo, Modifier.weight(1f)) { navController.navigate(Screen.StockIn.route) }
            }
            if (canManageUsers) {
                QuickActionButton(Icons.Default.People, "Staffs", PremiumWarning, Modifier.weight(1f)) { navController.navigate(Screen.UserList.route) }
            } else {
                QuickActionButton(Icons.Default.PersonAdd, "Customer", PremiumWarning, Modifier.weight(1f)) { navController.navigate(Screen.CustomerAdd.route) }
            }
        }
    }
}

@Composable
fun QuickActionButton(icon: androidx.compose.ui.graphics.vector.ImageVector, label: String, color: androidx.compose.ui.graphics.Color, modifier: Modifier = Modifier, onClick: () -> Unit) {
    Card(
        modifier = modifier.clickable { onClick() },
        shape = RoundedCornerShape(16.dp), colors = CardDefaults.cardColors(containerColor = PremiumDarkSurface), elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(modifier = Modifier.fillMaxWidth().padding(vertical = 16.dp), horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.Center) {
            Box(modifier = Modifier.size(44.dp).clip(RoundedCornerShape(12.dp)).background(color.copy(alpha = 0.15f)), contentAlignment = Alignment.Center) {
                Icon(icon, contentDescription = label, tint = color, modifier = Modifier.size(22.dp))
            }
            Spacer(modifier = Modifier.height(8.dp))
            Text(text = label, style = MaterialTheme.typography.labelSmall, color = PremiumTextSecondary)
        }
    }
}
