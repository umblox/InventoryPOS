package com.inventorypos.presentation.screens.inventory.product

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.inventorypos.presentation.components.common.*
import com.inventorypos.presentation.navigation.Screen
import com.inventorypos.presentation.theme.*
import com.inventorypos.domain.model.Product

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProductListScreen(
    navController: NavController,
    viewModel: ProductListViewModel = hiltViewModel()
) {
    val products by viewModel.products.collectAsState()
    val searchQuery by viewModel.searchQuery.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()
    
    Scaffold(
        topBar = {
            CustomTopBar(
                title = "Products",
                subtitle = "${products.size} items in inventory",
                onBackClick = { navController.popBackStack() },
                actions = {
                    IconButton(onClick = { /* Search */ }) {
                        Icon(Icons.Default.Search, "Search", tint = PremiumGold)
                    }
                    IconButton(onClick = { /* Filter */ }) {
                        Icon(Icons.Default.FilterList, "Filter", tint = PremiumGold)
                    }
                }
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = { navController.navigate(Screen.ProductAdd.route) },
                containerColor = PremiumGold,
                contentColor = PremiumDarkBackground,
                shape = RoundedCornerShape(16.dp)
            ) {
                Icon(Icons.Default.Add, "Add Product")
            }
        },
        containerColor = PremiumDarkBackground
    ) { padding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
        ) {
            if (isLoading) {
                LoadingIndicator()
            } else if (products.isEmpty()) {
                EmptyState(
                    icon = Icons.Default.Inventory,
                    title = "No Products Yet",
                    message = "Add your first product to get started"
                )
            } else {
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(16.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    // Search Bar
                    item {
                        CustomTextField(
                            value = searchQuery,
                            onValueChange = viewModel::onSearchQueryChange,
                            placeholder = "Search products...",
                            leadingIcon = Icons.Default.Search,
                            modifier = Modifier.fillMaxWidth()
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                    }
                    
                    // Product List
                    items(products, key = { it.id }) { product ->
                        ProductCard(
                            product = product,
                            onClick = {
                                navController.navigate(Screen.ProductDetail.createRoute(product.id))
                            },
                            onEdit = {
                                navController.navigate(Screen.ProductEdit.createRoute(product.id))
                            },
                            onDelete = {
                                navController.navigate(Screen.ProductDelete.createRoute(product.id))
                            }
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun ProductCard(
    product: Product, // Domain model
    onClick: () -> Unit,
    onEdit: () -> Unit,
    onDelete: () -> Unit
) {
    Card(
        onClick = onClick,
        modifier = Modifier
            .fillMaxWidth()
            .heightIn(min = 100.dp),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = PremiumDarkSurface
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(
                    Brush.horizontalGradient(
                        colors = listOf(CardGradientStart, CardGradientEnd)
                    )
                )
                .padding(16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(
                    modifier = Modifier.weight(1f)
                ) {
                    Text(
                        text = product.name,
                        style = MaterialTheme.typography.titleMedium,
                        color = PremiumTextPrimary,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                    Text(
                        text = "SKU: ${product.sku}",
                        style = MaterialTheme.typography.bodySmall,
                        color = PremiumTextMuted
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        // Price badge
                        Surface(
                            color = PremiumGold.copy(alpha = 0.15f),
                            shape = RoundedCornerShape(8.dp)
                        ) {
                            Text(
                                text = "Rp ${product.sellPrice}",
                                style = MaterialTheme.typography.labelMedium,
                                color = PremiumGold,
                                modifier = Modifier.padding(horizontal = 12.dp, vertical = 4.dp)
                            )
                        }
                        // Stock badge
                        val stockColor = when {
                            product.stock <= 0 -> PremiumError
                            product.stock <= product.minStock -> PremiumWarning
                            else -> PremiumSuccess
                        }
                        Surface(
                            color = stockColor.copy(alpha = 0.15f),
                            shape = RoundedCornerShape(8.dp)
                        ) {
                            Text(
                                text = "${product.stock} in stock",
                                style = MaterialTheme.typography.labelMedium,
                                color = stockColor,
                                modifier = Modifier.padding(horizontal = 12.dp, vertical = 4.dp)
                            )
                        }
                    }
                }
                
                // Action buttons
                Row(
                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    IconButton(
                        onClick = onEdit,
                        modifier = Modifier
                            .clip(RoundedCornerShape(10.dp))
                            .background(PremiumInfo.copy(alpha = 0.15f))
                    ) {
                        Icon(
                            Icons.Default.Edit,
                            "Edit",
                            tint = PremiumInfo,
                            modifier = Modifier.size(18.dp)
                        )
                    }
                    IconButton(
                        onClick = onDelete,
                        modifier = Modifier
                            .clip(RoundedCornerShape(10.dp))
                            .background(PremiumError.copy(alpha = 0.15f))
                    ) {
                        Icon(
                            Icons.Default.Delete,
                            "Delete",
                            tint = PremiumError,
                            modifier = Modifier.size(18.dp)
                        )
                    }
                }
            }
        }
    }
}

