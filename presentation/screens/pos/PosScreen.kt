package com.inventorypos.presentation.screens.pos

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.inventorypos.presentation.components.common.*
import com.inventorypos.presentation.navigation.Screen
import com.inventorypos.presentation.theme.*
import com.inventorypos.utils.CurrencyFormatter

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PosScreen(
    navController: NavController,
    viewModel: PosViewModel = hiltViewModel()
) {
    val products by viewModel.products.collectAsState()
    val cartItems by viewModel.cartItems.collectAsState()
    val searchQuery by viewModel.searchQuery.collectAsState()
    val totalAmount by viewModel.totalAmount.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()
    
    val categories = listOf("All", "Food", "Drink", "Snack", "Electronics", "Other")
    var selectedCategory by remember { mutableStateOf("All") }
    
    Scaffold(
        topBar = {
            CustomTopBar(
                title = "Point of Sale",
                subtitle = "New Transaction",
                showBackButton = false,
                actions = {
                    IconButton(onClick = { /* Hold bill */ }) {
                        Icon(Icons.Default.Pause, "Hold", tint = PremiumGold)
                    }
                    IconButton(onClick = { /* Clear cart */ }) {
                        Icon(Icons.Default.ClearAll, "Clear", tint = PremiumError)
                    }
                }
            )
        },
        containerColor = PremiumDarkBackground
    ) { padding ->
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
        ) {
            // Left Panel - Product Grid
            Column(
                modifier = Modifier
                    .weight(0.6f)
                    .fillMaxHeight()
                    .padding(16.dp)
            ) {
                // Search & Category
                CustomTextField(
                    value = searchQuery,
                    onValueChange = viewModel::onSearchQueryChange,
                    placeholder = "Search products...",
                    leadingIcon = Icons.Default.Search,
                    modifier = Modifier.fillMaxWidth()
                )
                
                Spacer(modifier = Modifier.height(12.dp))
                
                // Category chips
                Row(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    categories.forEach { category ->
                        FilterChip(
                            selected = selectedCategory == category,
                            onClick = { selectedCategory = category },
                            label = { Text(category) },
                            colors = FilterChipDefaults.filterChipColors(
                                selectedContainerColor = PremiumGold.copy(alpha = 0.2f),
                                selectedLabelColor = PremiumGold,
                                containerColor = PremiumDarkSurface,
                                labelColor = PremiumTextSecondary
                            ),
                            shape = RoundedCornerShape(10.dp)
                        )
                    }
                }
                
                Spacer(modifier = Modifier.height(12.dp))
                
                // Product Grid
                if (isLoading) {
                    LoadingIndicator()
                } else {
                    LazyVerticalGrid(
                        columns = GridCells.Fixed(3),
                        horizontalArrangement = Arrangement.spacedBy(12.dp),
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        items(products) { product ->
                            ProductCard(
                                product = product,
                                onClick = { viewModel.addToCart(product) }
                            )
                        }
                    }
                }
            }
            
            // Right Panel - Cart
            Column(
                modifier = Modifier
                    .weight(0.4f)
                    .fillMaxHeight()
                    .background(PremiumDarkSurface)
                    .padding(16.dp)
            ) {
                Text(
                    text = "Current Order",
                    style = MaterialTheme.typography.titleMedium,
                    color = PremiumTextPrimary,
                    fontWeight = FontWeight.Bold
                )
                
                Spacer(modifier = Modifier.height(12.dp))
                
                // Cart Items
                if (cartItems.isEmpty()) {
                    EmptyState(
                        icon = Icons.Default.ShoppingCart,
                        title = "Cart is Empty",
                        message = "Tap products to add"
                    )
                } else {
                    LazyColumn(
                        modifier = Modifier.weight(1f),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        items(cartItems) { item ->
                            CartItemCard(
                                item = item,
                                onQuantityChange = { qty -> viewModel.updateQuantity(item.productId, qty) },
                                onRemove = { viewModel.removeFromCart(item.productId) }
                            )
                        }
                    }
                }
                
                Divider(color = PremiumDarkBackground, thickness = 1.dp)
                Spacer(modifier = Modifier.height(12.dp))
                
                // Totals
                Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                    TotalRow("Subtotal", totalAmount)
                    TotalRow("Discount", 0.0)
                    TotalRow("Tax", totalAmount * 0.1)
                    Divider(color = PremiumDarkBackground, thickness = 1.dp, modifier = Modifier.padding(vertical = 8.dp))
                    TotalRow("Total", totalAmount * 1.1, isBold = true, color = PremiumGold)
                }
                
                Spacer(modifier = Modifier.height(16.dp))
                
                // Pay Button
                CustomButton(
                    text = "Pay ${CurrencyFormatter.format(totalAmount * 1.1)}",
                    onClick = { navController.navigate(Screen.CashPayment.route) },
                    modifier = Modifier.fillMaxWidth(),
                    icon = Icons.Default.Payment
                )
            }
        }
    }
}

@Composable
fun ProductCard(
    product: com.inventorypos.domain.model.Product,
    onClick: () -> Unit
) {
    Card(
        onClick = onClick,
        modifier = Modifier
            .fillMaxWidth()
            .height(120.dp),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = PremiumDarkSurface),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(12.dp),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = product.name,
                style = MaterialTheme.typography.labelLarge,
                color = PremiumTextPrimary,
                maxLines = 2
            )
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = CurrencyFormatter.format(product.sellPrice),
                    style = MaterialTheme.typography.labelMedium,
                    color = PremiumGold,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = "${product.stock}",
                    style = MaterialTheme.typography.labelSmall,
                    color = if (product.stock <= product.minStock) PremiumWarning else PremiumTextMuted
                )
            }
        }
    }
}

@Composable
fun CartItemCard(
    item: CartItem,
    onQuantityChange: (Int) -> Unit,
    onRemove: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp))
            .background(PremiumDarkBackground)
            .padding(12.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = item.productName,
                style = MaterialTheme.typography.bodyMedium,
                color = PremiumTextPrimary
            )
            Text(
                text = CurrencyFormatter.format(item.unitPrice) + " x ${item.quantity}",
                style = MaterialTheme.typography.labelSmall,
                color = PremiumTextMuted
            )
        }
        
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            // Quantity controls
            IconButton(
                onClick = { if (item.quantity > 1) onQuantityChange(item.quantity - 1) },
                modifier = Modifier.size(28.dp)
            ) {
                Icon(Icons.Default.Remove, null, tint = PremiumTextSecondary, modifier = Modifier.size(16.dp))
            }
            Text(
                text = item.quantity.toString(),
                style = MaterialTheme.typography.bodyMedium,
                color = PremiumTextPrimary,
                modifier = Modifier.width(24.dp),
                textAlign = TextAlign.Center
            )
            IconButton(
                onClick = { onQuantityChange(item.quantity + 1) },
                modifier = Modifier.size(28.dp)
            ) {
                Icon(Icons.Default.Add, null, tint = PremiumGold, modifier = Modifier.size(16.dp))
            }
            
            Spacer(modifier = Modifier.width(8.dp))
            
            Text(
                text = CurrencyFormatter.format(item.totalPrice),
                style = MaterialTheme.typography.labelMedium,
                color = PremiumGold,
                fontWeight = FontWeight.SemiBold
            )
            
            IconButton(onClick = onRemove, modifier = Modifier.size(28.dp)) {
                Icon(Icons.Default.Close, null, tint = PremiumError, modifier = Modifier.size(16.dp))
            }
        }
    }
}

@Composable
fun TotalRow(
    label: String,
    amount: Double,
    isBold: Boolean = false,
    color: androidx.compose.ui.graphics.Color = PremiumTextSecondary
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = label,
            style = if (isBold) MaterialTheme.typography.titleSmall else MaterialTheme.typography.bodyMedium,
            color = if (isBold) PremiumTextPrimary else PremiumTextSecondary
        )
        Text(
            text = CurrencyFormatter.format(amount),
            style = if (isBold) MaterialTheme.typography.titleSmall else MaterialTheme.typography.bodyMedium,
            color = color,
            fontWeight = if (isBold) FontWeight.Bold else FontWeight.Normal
        )
    }
}

data class CartItem(
    val productId: Long,
    val productName: String,
    val unitPrice: Double,
    val quantity: Int,
    val totalPrice: Double
)
