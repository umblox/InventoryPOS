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
fun StockAdjustmentScreen(
    navController: NavController,
    viewModel: StockAdjustmentViewModel = hiltViewModel()
) {
    val searchQuery by viewModel.searchQuery.collectAsState()
    val searchResults by viewModel.searchResults.collectAsState()
    val selectedProduct by viewModel.selectedProduct.collectAsState()
    
    val currentStock by viewModel.currentStock.collectAsState()
    val newStock by viewModel.newStock.collectAsState()
    val reason by viewModel.reason.collectAsState()
    val notes by viewModel.notes.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()
    val isSuccess by viewModel.isSuccess.collectAsState()
    val error by viewModel.error.collectAsState()
    
    LaunchedEffect(isSuccess) { if (isSuccess) navController.popBackStack() }
    
    Scaffold(
        topBar = {
            CustomTopBar(
                title = "Stock Adjustment",
                subtitle = "Fix stock discrepancies",
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
                            ProductSearchResultItem(product) { viewModel.selectProduct(product) }
                        }
                    }
                } else if (searchQuery.length > 2) {
                    Text("No products found", color = PremiumTextMuted, style = MaterialTheme.typography.bodyMedium)
                }
            } else {
                SelectedProductCard(product = selectedProduct!!, onClear = viewModel::clearSelection)
                
                Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                    // Current Stock (Read-only / Disabled)
                    Box(modifier = Modifier.weight(1f)) {
                        CustomTextField(
                            value = currentStock,
                            onValueChange = { },
                            label = "System Stock",
                            leadingIcon = Icons.Default.Inventory,
                            readOnly = true
                        )
                    }
                    
                    // New Stock (Input)
                    Box(modifier = Modifier.weight(1f)) {
                        CustomTextField(
                            value = newStock,
                            onValueChange = viewModel::onNewStockChange,
                            label = "Actual Stock *",
                            placeholder = "New qty",
                            leadingIcon = Icons.Default.Edit,
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
                        )
                    }
                }
                
                CustomTextField(
                    value = reason,
                    onValueChange = viewModel::onReasonChange,
                    label = "Reason *",
                    placeholder = "Found extra, System error, etc.",
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
                    text = "Confirm Adjustment",
                    onClick = viewModel::confirmAdjustment,
                    isLoading = isLoading,
                    modifier = Modifier.fillMaxWidth(),
                    icon = Icons.Default.Build,
                    containerColor = PremiumAccent
                )
            }
        }
    }
}
