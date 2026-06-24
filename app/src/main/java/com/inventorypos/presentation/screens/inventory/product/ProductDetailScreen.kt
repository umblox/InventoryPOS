package com.inventorypos.presentation.screens.inventory.product

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.inventorypos.presentation.components.common.*
import com.inventorypos.presentation.navigation.Screen
import com.inventorypos.presentation.theme.*

@Composable
fun ProductDetailScreen(
    navController: NavController,
    productId: Long,
    viewModel: ProductDetailViewModel = hiltViewModel()
) {
    LaunchedEffect(productId) {
        viewModel.loadProduct(productId)
    }
    
    val product by viewModel.product.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()
    
    Scaffold(
        topBar = {
            CustomTopBar(
                title = "Product Detail",
                subtitle = "ID: #$productId",
                onBackClick = { navController.popBackStack() },
                actions = {
                    IconButton(onClick = {
                        navController.navigate(Screen.ProductEdit.createRoute(productId))
                    }) {
                        Icon(Icons.Default.Edit, "Edit", tint = PremiumGold)
                    }
                    IconButton(onClick = {
                        navController.navigate(Screen.ProductDelete.createRoute(productId))
                    }) {
                        Icon(Icons.Default.Delete, "Delete", tint = PremiumError)
                    }
                }
            )
        },
        containerColor = PremiumDarkBackground
    ) { padding ->
        if (isLoading) {
            LoadingIndicator()
        } else if (product == null) {
            ErrorState(message = "Product not found")
        } else {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState())
                    .padding(padding)
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                // Product Image
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(200.dp)
                        .clip(RoundedCornerShape(20.dp))
                        .background(PremiumDarkSurface),
                    contentAlignment = Alignment.Center
                ) {
                    if (product!!.imageUrl != null) {
                        AsyncImage(
                            model = product!!.imageUrl,
                            contentDescription = product!!.name,
                            modifier = Modifier.fillMaxSize(),
                            contentScale = ContentScale.Crop
                        )
                    } else {
                        Icon(
                            Icons.Default.Inventory,
                            contentDescription = null,
                            tint = PremiumGold.copy(alpha = 0.3f),
                            modifier = Modifier.size(64.dp)
                        )
                    }
                }
                
                // Product Name & SKU
                Column {
                    Text(
                        text = product!!.name,
                        style = MaterialTheme.typography.headlineSmall,
                        color = PremiumTextPrimary,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = "SKU: ${product!!.sku}",
                        style = MaterialTheme.typography.bodyMedium,
                        color = PremiumTextMuted
                    )
                }
                
                // Price Cards
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    PriceCard(
                        label = "Buy Price",
                        price = product!!.buyPrice,
                        color = PremiumInfo,
                        modifier = Modifier.weight(1f)
                    )
                    PriceCard(
                        label = "Sell Price",
                        price = product!!.sellPrice,
                        color = PremiumSuccess,
                        modifier = Modifier.weight(1f)
                    )
                }
                
                // Stock Info
                GradientCard {
                    Column {
                        Text(
                            text = "Stock Information",
                            style = MaterialTheme.typography.titleSmall,
                            color = PremiumTextPrimary,
                            fontWeight = FontWeight.SemiBold
                        )
                        Spacer(modifier = Modifier.height(12.dp))
                        StockInfoRow("Current Stock", product!!.stock.toString(), 
                            if (product!!.stock <= 0) PremiumError 
                            else if (product!!.stock <= product!!.minStock) PremiumWarning 
                            else PremiumSuccess)
                        StockInfoRow("Minimum Stock", product!!.minStock.toString(), PremiumTextMuted)
                        StockInfoRow("Unit", product!!.unit, PremiumTextMuted)
                    }
                }
                
                // Description
                if (!product!!.description.isNullOrBlank()) {
                    GradientCard {
                        Column {
                            Text(
                                text = "Description",
                                style = MaterialTheme.typography.titleSmall,
                                color = PremiumTextPrimary,
                                fontWeight = FontWeight.SemiBold
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(
                                text = product!!.description!!,
                                style = MaterialTheme.typography.bodyMedium,
                                color = PremiumTextSecondary
                            )
                        }
                    }
                }
                
                Spacer(modifier = Modifier.height(32.dp))
            }
        }
    }
}

@Composable
private fun PriceCard(label: String, price: Double, color: androidx.compose.ui.graphics.Color, modifier: Modifier = Modifier) {
    Card(
        modifier = modifier,
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = PremiumDarkSurface)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(color.copy(alpha = 0.1f))
                .padding(16.dp)
        ) {
            Text(
                text = label,
                style = MaterialTheme.typography.labelMedium,
                color = PremiumTextSecondary
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = "Rp ${String.format("%,.0f", price)}",
                style = MaterialTheme.typography.titleLarge,
                color = color,
                fontWeight = FontWeight.Bold
            )
        }
    }
}

@Composable
private fun StockInfoRow(label: String, value: String, valueColor: androidx.compose.ui.graphics.Color) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(text = label, style = MaterialTheme.typography.bodyMedium, color = PremiumTextSecondary)
        Text(text = value, style = MaterialTheme.typography.bodyMedium, color = valueColor, fontWeight = FontWeight.SemiBold)
    }
}
