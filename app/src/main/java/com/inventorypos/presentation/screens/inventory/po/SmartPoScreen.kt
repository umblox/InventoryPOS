package com.inventorypos.presentation.screens.inventory.po

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.inventorypos.domain.model.SmartPoItem
import com.inventorypos.presentation.components.common.*
import com.inventorypos.presentation.theme.*

@Composable
fun SmartPoScreen(
    navController: NavController,
    viewModel: SmartPoViewModel = hiltViewModel()
) {
    val poItems by viewModel.poItems.collectAsState()
    val selectedQuantities by viewModel.selectedQuantities.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()
    val isSuccess by viewModel.isSuccess.collectAsState()

    val context = LocalContext.current

    LaunchedEffect(isSuccess) {
        if (isSuccess) {
            Toast.makeText(context, "Purchase Orders created successfully!", Toast.LENGTH_SHORT).show()
            navController.popBackStack()
        }
    }

    Scaffold(
        topBar = {
            CustomTopBar(
                title = "Smart PO Cart",
                subtitle = "Restock Recommendations",
                onBackClick = { navController.popBackStack() },
                actions = {
                    IconButton(onClick = viewModel::autoSelectRecommended) {
                        Icon(Icons.Default.AutoAwesome, "Auto Select", tint = PremiumGold)
                    }
                }
            )
        },
        bottomBar = {
            if (selectedQuantities.isNotEmpty()) {
                Surface(
                    color = PremiumDarkSurface,
                    shadowElevation = 16.dp
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Text(
                                text = "${selectedQuantities.size} Items Selected",
                                style = MaterialTheme.typography.titleMedium,
                                color = PremiumTextPrimary
                            )
                            Text(
                                text = "Auto-grouped by supplier",
                                style = MaterialTheme.typography.bodySmall,
                                color = PremiumAccent
                            )
                        }
                        CustomButton(
                            text = "Create PO",
                            onClick = viewModel::createPurchaseOrders,
                            isLoading = isLoading
                        )
                    }
                }
            }
        },
        containerColor = PremiumDarkBackground
    ) { padding ->
        if (isLoading && poItems.isEmpty()) {
            LoadingIndicator()
        } else if (poItems.isEmpty()) {
            EmptyState(
                icon = Icons.Default.CheckCircle,
                title = "All Good!",
                message = "No items need restocking right now."
            )
        } else {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding),
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(poItems) { item ->
                    val isSelected = selectedQuantities.containsKey(item.product.id)
                    val qty = selectedQuantities[item.product.id] ?: 0

                    SmartPoItemCard(
                        item = item,
                        isSelected = isSelected,
                        quantity = qty,
                        onToggleSelect = { checked -> viewModel.toggleSelection(item, checked) },
                        onQuantityChange = { newQty -> viewModel.updateQuantity(item.product.id, newQty) }
                    )
                }
                item { Spacer(modifier = Modifier.height(80.dp)) } 
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SmartPoItemCard(
    item: SmartPoItem,
    isSelected: Boolean,
    quantity: Int,
    onToggleSelect: (Boolean) -> Unit,
    onQuantityChange: (Int) -> Unit
) {
    val borderColor = if (isSelected) PremiumGold else PremiumDarkSurface

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = if (isSelected) PremiumGold.copy(alpha = 0.05f) else PremiumDarkSurface
        ),
        border = androidx.compose.foundation.BorderStroke(1.dp, borderColor)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Top
            ) {
                Row(
                    modifier = Modifier.weight(1f),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Checkbox(
                        checked = isSelected,
                        onCheckedChange = onToggleSelect,
                        colors = CheckboxDefaults.colors(checkedColor = PremiumGold)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Column {
                        Text(
                            text = item.product.name,
                            style = MaterialTheme.typography.titleMedium,
                            color = PremiumTextPrimary,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = "Stock: ${item.product.stock} / Min: ${item.product.minStock}",
                            style = MaterialTheme.typography.bodySmall,
                            color = if (item.product.stock <= 0) PremiumError else PremiumWarning
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Info Supplier Termurah
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(8.dp))
                    .background(PremiumDarkBackground)
                    .padding(12.dp)
            ) {
                if (item.recommendedSupplier != null) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Text("Recommended Supplier", style = MaterialTheme.typography.labelSmall, color = PremiumTextMuted)
                            Text(item.recommendedSupplier.supplierName, style = MaterialTheme.typography.bodyMedium, color = PremiumGold)
                        }
                        Text(
                            text = "Rp ${String.format("%,.0f", item.recommendedSupplier.buyPrice)}",
                            style = MaterialTheme.typography.titleMedium,
                            color = PremiumSuccess,
                            fontWeight = FontWeight.Bold
                        )
                    }
                } else {
                    Text("No supplier connected. Add supplier first.", style = MaterialTheme.typography.bodyMedium, color = PremiumError)
                }
            }

            // Form QTY muncul jika dicentang
            if (isSelected && item.recommendedSupplier != null) {
                Spacer(modifier = Modifier.height(12.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.End
                ) {
                    Text("Order Qty:", style = MaterialTheme.typography.bodyMedium, color = PremiumTextSecondary)
                    Spacer(modifier = Modifier.width(12.dp))
                    
                    OutlinedTextField(
                        value = quantity.toString(),
                        onValueChange = { onQuantityChange(it.toIntOrNull() ?: 0) },
                        modifier = Modifier.width(100.dp),
                        textStyle = androidx.compose.ui.text.TextStyle(textAlign = TextAlign.Center, color = PremiumTextPrimary),
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = PremiumGold,
                            unfocusedBorderColor = PremiumTextMuted
                        ),
                        singleLine = true
                    )
                }
            }
        }
    }
}
