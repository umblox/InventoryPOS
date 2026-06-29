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
fun StockTransferScreen(
    navController: NavController,
    viewModel: StockTransferViewModel = hiltViewModel()
) {
    val searchQuery by viewModel.searchQuery.collectAsState()
    val searchResults by viewModel.searchResults.collectAsState()
    val selectedProduct by viewModel.selectedProduct.collectAsState()
    
    val quantity by viewModel.quantity.collectAsState()
    val fromLocation by viewModel.fromLocation.collectAsState()
    val toLocation by viewModel.toLocation.collectAsState()
    val notes by viewModel.notes.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()
    val isSuccess by viewModel.isSuccess.collectAsState()
    val error by viewModel.error.collectAsState()
    
    LaunchedEffect(isSuccess) { if (isSuccess) navController.popBackStack() }
    
    Scaffold(
        topBar = {
            CustomTopBar(
                title = "Stock Transfer",
                subtitle = "Move products between locations",
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
                }
            } else {
                SelectedProductCard(product = selectedProduct!!, onClear = viewModel::clearSelection)
                
                CustomTextField(
                    value = quantity,
                    onValueChange = viewModel::onQuantityChange,
                    label = "Quantity to Transfer *",
                    placeholder = "Enter quantity",
                    leadingIcon = Icons.Default.SyncAlt,
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
                )
                
                Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                    Box(modifier = Modifier.weight(1f)) {
                        CustomTextField(
                            value = fromLocation,
                            onValueChange = viewModel::onFromLocationChange,
                            label = "From *",
                            placeholder = "Whs A",
                            leadingIcon = Icons.Default.Storefront
                        )
                    }
                    Box(modifier = Modifier.weight(1f)) {
                        CustomTextField(
                            value = toLocation,
                            onValueChange = viewModel::onToLocationChange,
                            label = "To *",
                            placeholder = "Whs B",
                            leadingIcon = Icons.Default.Storefront
                        )
                    }
                }
                
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
                    text = "Confirm Transfer",
                    onClick = viewModel::confirmTransfer,
                    isLoading = isLoading,
                    modifier = Modifier.fillMaxWidth(),
                    icon = Icons.Default.CheckCircle,
                    containerColor = PremiumInfo
                )
            }
        }
    }
}
