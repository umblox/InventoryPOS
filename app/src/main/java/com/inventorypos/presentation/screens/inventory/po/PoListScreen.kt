package com.inventorypos.presentation.screens.inventory.po

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.inventorypos.data.local.entity.PoStatus
import com.inventorypos.data.local.entity.PurchaseOrderEntity
import com.inventorypos.presentation.components.common.CustomTopBar
import com.inventorypos.presentation.components.common.EmptyState
import com.inventorypos.presentation.navigation.Screen
import com.inventorypos.presentation.theme.*
import java.text.SimpleDateFormat
import java.util.Locale

@Composable
fun PoListScreen(
    navController: NavController,
    viewModel: PoListViewModel = hiltViewModel()
) {
    val purchaseOrders by viewModel.purchaseOrders.collectAsState()
    val dateFormat = SimpleDateFormat("dd MMM yyyy, HH:mm", Locale.getDefault())

    Scaffold(
        topBar = {
            CustomTopBar(
                title = "Purchase Orders",
                subtitle = "Manage supplier restocks",
                onBackClick = { navController.popBackStack() },
                actions = {
                    IconButton(onClick = { navController.navigate(Screen.SmartPo.route) }) {
                        Icon(Icons.Default.AddShoppingCart, "New PO", tint = PremiumGold)
                    }
                }
            )
        },
        containerColor = PremiumDarkBackground
    ) { padding ->
        if (purchaseOrders.isEmpty()) {
            EmptyState(
                icon = Icons.Default.ReceiptLong,
                title = "No Purchase Orders",
                message = "You haven't created any POs yet."
            )
        } else {
            LazyColumn(
                modifier = Modifier.fillMaxSize().padding(padding),
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(purchaseOrders) { po ->
                    PoItemCard(
                        po = po,
                        dateStr = dateFormat.format(po.createdAt),
                        onClick = { navController.navigate(Screen.PoDetail.createRoute(po.id)) }
                    )
                }
            }
        }
    }
}

@Composable
fun PoItemCard(po: PurchaseOrderEntity, dateStr: String, onClick: () -> Unit) {
    val (statusColor, statusIcon) = when (po.status) {
        PoStatus.PENDING -> PremiumWarning to Icons.Default.PendingActions
        PoStatus.PARTIAL -> PremiumInfo to Icons.Default.LocalShipping
        PoStatus.COMPLETED -> PremiumSuccess to Icons.Default.CheckCircle
        PoStatus.CANCELLED -> PremiumError to Icons.Default.Cancel
    }

    Card(
        modifier = Modifier.fillMaxWidth().clickable { onClick() },
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = PremiumDarkSurface)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                Text(po.poNumber, style = MaterialTheme.typography.titleMedium, color = PremiumGold, fontWeight = FontWeight.Bold)
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(statusIcon, contentDescription = null, tint = statusColor, modifier = Modifier.size(16.dp))
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(po.status.name, style = MaterialTheme.typography.labelSmall, color = statusColor)
                }
            }
            Spacer(modifier = Modifier.height(8.dp))
            Text("Supplier: ${po.supplierName}", style = MaterialTheme.typography.bodyMedium, color = PremiumTextPrimary)
            Text(dateStr, style = MaterialTheme.typography.bodySmall, color = PremiumTextMuted)
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                "Total: Rp ${String.format("%,.0f", po.totalAmount)}",
                style = MaterialTheme.typography.titleMedium, color = PremiumSuccess, fontWeight = FontWeight.SemiBold
            )
        }
    }
}
