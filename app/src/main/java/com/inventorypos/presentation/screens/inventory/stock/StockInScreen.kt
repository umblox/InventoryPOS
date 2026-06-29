package com.inventorypos.presentation.screens.inventory.stock

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.inventorypos.domain.model.Product
import com.inventorypos.presentation.components.common.*
import com.inventorypos.presentation.theme.*

@Composable
fun StockInScreen(
    navController: NavController,
    viewModel: StockInViewModel = hiltViewModel()
) {
    val searchQuery by viewModel.searchQuery.collectAsState()
    val searchResults by viewModel.searchResults.collectAsState()
    val selectedProduct by viewModel.selectedProduct.collectAsState()
    
    val quantity by viewModel.quantity.collectAsState()
    val notes by viewModel.notes.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()
    val isSuccess by viewModel.isSuccess.collectAsState()
    val error by viewModel.error.collectAsState()
    
    LaunchedEffect(isSuccess) { if (isSuccess) navController.popBackStack() }
    
    Scaffold(
        topBar = {
            CustomTopBar(
                title = "Stock In",
                subtitle = "Receive products from supplier",
                onBackClick = { navController.popBackStack() }
            )
        },
        containerColor = PremiumDarkBackground
    ) { padding ->
        Column(
            modifier = Modifier.fillMaxSize().padding(padding).padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            
            // KONDISI 1: JIKA PRODUK BELUM DIPILIH
            if (selectedProduct == null) {
                CustomTextField(
                    value = searchQuery,
                    onValueChange = viewModel::onSearchQueryChange,
                    label = "Search Product *",
                    placeholder = "Type name or SKU...",
                    leadingIcon = Icons.Default.Search
                )
                
                if (searchResults.isNotEmpty()) {
                    LazyColumn(modifier = Modifier.fillMaxWidth().heightIn(max = 300.dp)) {
                        items(searchResults) { product ->
                            ProductSearchResultItem(product) { viewModel.selectProduct(product) }
                        }
                    }
                } else if (searchQuery.length > 2) {
                    Text("No products found", color = PremiumTextMuted, style = MaterialTheme.typography.bodyMedium)
                }
            } 
            // KONDISI 2: JIKA PRODUK SUDAH DIPILIH
            else {
                SelectedProductCard(product = selectedProduct!!, onClear = viewModel::clearSelection)
                
                CustomTextField(
                    value = quantity,
                    onValueChange = viewModel::onQuantityChange,
                    label = "Quantity Received *",
                    placeholder = "Enter quantity",
                    leadingIcon = Icons.Default.AddBox,
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
                )
                
                CustomTextField(
                    value = notes,
                    onValueChange = viewModel::onNotesChange,
                    label = "Notes (Optional)",
                    placeholder = "Supplier name, Invoice number, etc.",
                    leadingIcon = Icons.Default.Notes,
                    maxLines = 3,
                    singleLine = false
                )
                
                if (error != null) {
                    Text(text = error!!, color = PremiumError, style = MaterialTheme.typography.bodySmall)
                }
                
                Spacer(modifier = Modifier.weight(1f))
                
                CustomButton(
                    text = "Confirm Stock In",
                    onClick = viewModel::confirmStockIn,
                    isLoading = isLoading,
                    modifier = Modifier.fillMaxWidth(),
                    icon = Icons.Default.CheckCircle,
                    containerColor = PremiumSuccess
                )
            }
        }
    }
}

@Composable
fun ProductSearchResultItem(product: Product, onClick: () -> Unit) {
    Card(
        modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp).clickable(onClick = onClick),
        colors = CardDefaults.cardColors(containerColor = PremiumDarkSurface)
    ) {
        Row(modifier = Modifier.padding(16.dp), verticalAlignment = Alignment.CenterVertically) {
            Icon(Icons.Default.Inventory, null, tint = PremiumGold)
            Spacer(modifier = Modifier.width(16.dp))
            Column {
                Text(product.name, style = MaterialTheme.typography.titleMedium, color = PremiumTextPrimary)
                Text("SKU: ${product.sku} | Stock: ${product.stock}", style = MaterialTheme.typography.bodySmall, color = PremiumTextMuted)
            }
        }
    }
}

@Composable
fun SelectedProductCard(product: Product, onClear: () -> Unit) {
    Card(modifier = Modifier.fillMaxWidth(), colors = CardDefaults.cardColors(containerColor = PremiumDarkSurface)) {
        Row(modifier = Modifier.padding(16.dp).fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
            Column(modifier = Modifier.weight(1f)) {
                Text("Selected Product", style = MaterialTheme.typography.labelSmall, color = PremiumGold)
                Text(product.name, style = MaterialTheme.typography.titleMedium, color = PremiumTextPrimary)
                Text("Current Stock: ${product.stock}", style = MaterialTheme.typography.bodySmall, color = PremiumTextMuted)
            }
            IconButton(onClick = onClear) { Icon(Icons.Default.Close, "Clear", tint = PremiumError) }
        }
    }
}
