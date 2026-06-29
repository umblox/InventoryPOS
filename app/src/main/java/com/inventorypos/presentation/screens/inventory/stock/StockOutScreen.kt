package com.inventorypos.presentation.screens.inventory.stock

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.inventorypos.presentation.components.common.*
import com.inventorypos.presentation.theme.*

@Composable
fun StockOutScreen(
    navController: NavController,
    viewModel: StockOutViewModel = hiltViewModel()
) {
    val searchQuery by viewModel.searchQuery.collectAsState()
    val searchResults by viewModel.searchResults.collectAsState()
    val selectedProduct by viewModel.selectedProduct.collectAsState()
    
    val quantity by viewModel.quantity.collectAsState()
    val reason by viewModel.reason.collectAsState()
    val notes by viewModel.notes.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()
    val isSuccess by viewModel.isSuccess.collectAsState()
    val error by viewModel.error.collectAsState()
    
    LaunchedEffect(isSuccess) { if (isSuccess) navController.popBackStack() }
    
    Scaffold(
        topBar = {
            CustomTopBar(
                title = "Stock Out",
                subtitle = "Remove products from inventory",
                onBackClick = { navController.popBackStack() }
            )
        },
        containerColor = PremiumDarkBackground
    ) { padding ->
        Column(
            modifier = Modifier.fillMaxSize().padding(padding).padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            
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
                            // Menggunakan komponen yang sudah ada di StockInScreen.kt
                            ProductSearchResultItem(product) { viewModel.selectProduct(product) }
                        }
                    }
                } else if (searchQuery.length > 2) {
                    Text("No products found", color = PremiumTextMuted, style = MaterialTheme.typography.bodyMedium)
                }
            } else {
                // Menggunakan komponen yang sudah ada di StockInScreen.kt
                SelectedProductCard(product = selectedProduct!!, onClear = viewModel::clearSelection)
                
                CustomTextField(
                    value = quantity,
                    onValueChange = viewModel::onQuantityChange,
                    label = "Quantity to Remove *",
                    placeholder = "Enter quantity",
                    leadingIcon = Icons.Default.RemoveCircle,
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
                )
                
                CustomTextField(
                    value = reason,
                    onValueChange = viewModel::onReasonChange,
                    label = "Reason *",
                    placeholder = "Damaged, Expired, Lost, etc.",
                    leadingIcon = Icons.Default.Help
                )
                
                CustomTextField(
                    value = notes,
                    onValueChange = viewModel::onNotesChange,
                    label = "Notes (Optional)",
                    placeholder = "Details...",
                    leadingIcon = Icons.Default.Notes,
                    maxLines = 3,
                    singleLine = false
                )
                
                if (error != null) {
                    Text(text = error!!, color = PremiumError, style = MaterialTheme.typography.bodySmall)
                }
                
                Spacer(modifier = Modifier.weight(1f))
                
                CustomButton(
                    text = "Confirm Stock Out",
                    onClick = viewModel::confirmStockOut,
                    isLoading = isLoading,
                    modifier = Modifier.fillMaxWidth(),
                    icon = Icons.Default.CheckCircle,
                    containerColor = PremiumError
                )
            }
        }
    }
}
