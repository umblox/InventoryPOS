package com.inventorypos.presentation.screens.inventory.po

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.inventorypos.data.local.entity.PoStatus
import com.inventorypos.data.local.entity.PurchaseOrderItemEntity
import com.inventorypos.presentation.components.common.CustomButton
import com.inventorypos.presentation.components.common.CustomTopBar
import com.inventorypos.presentation.components.common.LoadingIndicator
import com.inventorypos.presentation.theme.*

@Composable
fun PoDetailScreen(
    navController: NavController,
    poId: Long,
    viewModel: PoDetailViewModel = hiltViewModel()
) {
    LaunchedEffect(poId) { viewModel.loadPo(poId) }

    val poDetails by viewModel.poDetails.collectAsState()
    val receivingBatch by viewModel.receivingBatch.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()
    val isSuccess by viewModel.isSuccess.collectAsState()
    val context = LocalContext.current

    LaunchedEffect(isSuccess) {
        if (isSuccess) {
            Toast.makeText(context, "Berhasil! Stok inventaris otomatis bertambah.", Toast.LENGTH_LONG).show()
        }
    }

    Scaffold(
        topBar = {
            CustomTopBar(
                title = poDetails?.purchaseOrder?.poNumber ?: "Loading...",
                subtitle = "Validate Goods Receipt",
                onBackClick = { navController.popBackStack() }
            )
        },
        bottomBar = {
            if (poDetails != null && poDetails!!.purchaseOrder.status != PoStatus.COMPLETED) {
                Surface(color = PremiumDarkSurface, shadowElevation = 16.dp) {
                    Box(modifier = Modifier.fillMaxWidth().padding(16.dp)) {
                        CustomButton(
                            text = "Confirm Receive & Update Stock",
                            onClick = viewModel::submitReceiving,
                            icon = Icons.Default.CheckCircle,
                            isLoading = isLoading,
                            modifier = Modifier.fillMaxWidth()
                        )
                    }
                }
            }
        },
        containerColor = PremiumDarkBackground
    ) { padding ->
        if (isLoading && poDetails == null) {
            LoadingIndicator()
        } else if (poDetails != null) {
            val po = poDetails!!.purchaseOrder
            
            LazyColumn(
                modifier = Modifier.fillMaxSize().padding(padding),
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                // Info Status PO
                item {
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        colors = CardDefaults.cardColors(containerColor = PremiumDarkSurface)
                    ) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Text("Supplier: ${po.supplierName}", style = MaterialTheme.typography.titleMedium, color = PremiumTextPrimary)
                            Spacer(modifier = Modifier.height(4.dp))
                            Text("Status: ${po.status.name}", style = MaterialTheme.typography.bodyMedium, color = if (po.status == PoStatus.COMPLETED) PremiumSuccess else PremiumWarning)
                            if (po.status == PoStatus.COMPLETED) {
                                Text("Seluruh barang pada nota ini telah diterima dengan lengkap.", style = MaterialTheme.typography.labelSmall, color = PremiumSuccess)
                            }
                        }
                    }
                }

                // Daftar Barang
                items(poDetails!!.items) { item ->
                    val arrivingNow = receivingBatch[item.id] ?: 0
                    ReceivingItemCard(
                        item = item,
                        arrivingNow = arrivingNow,
                        isCompleted = po.status == PoStatus.COMPLETED,
                        onQtyChange = { viewModel.updateReceiveQty(item.id, it) }
                    )
                }
                item { Spacer(modifier = Modifier.height(80.dp)) }
            }
        }
    }
}

@Composable
fun ReceivingItemCard(
    item: PurchaseOrderItemEntity,
    arrivingNow: Int,
    isCompleted: Boolean,
    onQtyChange: (Int) -> Unit
) {
    val remaining = item.quantityOrdered - item.quantityReceived

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = PremiumDarkSurface)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(item.productName, style = MaterialTheme.typography.titleMedium, color = PremiumGold)
            Spacer(modifier = Modifier.height(8.dp))
            
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                Column {
                    Text("Ordered", style = MaterialTheme.typography.labelSmall, color = PremiumTextMuted)
                    Text("${item.quantityOrdered}", style = MaterialTheme.typography.bodyLarge, color = PremiumTextPrimary)
                }
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text("Received", style = MaterialTheme.typography.labelSmall, color = PremiumTextMuted)
                    Text("${item.quantityReceived}", style = MaterialTheme.typography.bodyLarge, color = PremiumSuccess)
                }
                Column(horizontalAlignment = Alignment.End) {
                    Text("Remaining", style = MaterialTheme.typography.labelSmall, color = PremiumTextMuted)
                    Text("$remaining", style = MaterialTheme.typography.bodyLarge, color = if (remaining > 0) PremiumError else PremiumTextMuted)
                }
            }

            // Input form untuk kurir (hanya muncul jika barang belum lengkap)
            if (!isCompleted && remaining > 0) {
                Spacer(modifier = Modifier.height(16.dp))
                Divider(color = PremiumTextMuted.copy(alpha = 0.2f))
                Spacer(modifier = Modifier.height(16.dp))
                
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text("Arriving Now:", style = MaterialTheme.typography.bodyMedium, color = PremiumInfo)
                    OutlinedTextField(
                        value = arrivingNow.toString(),
                        onValueChange = { onQtyChange(it.toIntOrNull() ?: 0) },
                        modifier = Modifier.width(100.dp),
                        textStyle = androidx.compose.ui.text.TextStyle(textAlign = TextAlign.Center, color = PremiumTextPrimary),
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = PremiumInfo,
                            unfocusedBorderColor = PremiumTextMuted
                        ),
                        singleLine = true
                    )
                }
            }
        }
    }
}
