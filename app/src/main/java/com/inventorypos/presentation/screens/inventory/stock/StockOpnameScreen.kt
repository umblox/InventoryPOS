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
fun StockOpnameScreen(
    navController: NavController,
    viewModel: StockOpnameViewModel = hiltViewModel()
) {
    val searchQuery by viewModel.searchQuery.collectAsState()
    val searchResults by viewModel.searchResults.collectAsState()
    val selectedProduct by viewModel.selectedProduct.collectAsState()
    
    val systemStock by viewModel.systemStock.collectAsState()
    val physicalStock by viewModel.physicalStock.collectAsState()
    val notes by viewModel.notes.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()
    val isSuccess by viewModel.isSuccess.collectAsState()
    val error by viewModel.error.collectAsState()
    
    val variance = (physicalStock.toIntOrNull() ?: 0) - (systemStock.toIntOrNull() ?: 0)

    LaunchedEffect(isSuccess) { if (isSuccess) navController.popBackStack() }
    
    Scaffold(
        topBar = {
            CustomTopBar(
                title = "Stock Opname",
                subtitle = "Physical stock count",
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
                
                Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                    Box(modifier = Modifier.weight(1f)) {
                        CustomTextField(
                            value = systemStock,
                            onValueChange = { },
                            label = "System Stock",
                            leadingIcon = Icons.Default.Computer,
                            readOnly = true
                        )
                    }
                    Box(modifier = Modifier.weight(1f)) {
                        CustomTextField(
                            value = physicalStock,
                            onValueChange = viewModel::onPhysicalStockChange,
                            label = "Physical Stock *",
                            placeholder = "Count",
                            leadingIcon = Icons.Default.FactCheck,
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
                        )
                    }
                }
                
                if (systemStock.isNotBlank() && physicalStock.isNotBlank()) {
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        shape = MaterialTheme.shapes.large,
                        colors = CardDefaults.cardColors(
                            containerColor = if (variance == 0) PremiumSuccess.copy(alpha = 0.1f) else PremiumWarning.copy(alpha = 0.1f)
                        )
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth().padding(16.dp),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text("Variance", color = PremiumTextSecondary)
                            Text(
                                "${if (variance > 0) "+" else ""}$variance",
                                color = if (variance == 0) PremiumSuccess else PremiumWarning,
                                fontWeight = androidx.compose.ui.text.font.FontWeight.Bold
                            )
                        }
                    }
                }
                
                CustomTextField(
                    value = notes,
                    onValueChange = viewModel::onNotesChange,
                    label = "Notes",
                    placeholder = "Reason for variance",
                    leadingIcon = Icons.Default.Notes,
                    maxLines = 3,
                    singleLine = false
                )
                
                if (error != null) {
                    Text(text = error!!, color = PremiumError, style = MaterialTheme.typography.bodySmall)
                }
                
                Spacer(modifier = Modifier.weight(1f))
                
                CustomButton(
                    text = "Confirm Opname",
                    onClick = viewModel::confirmOpname,
                    isLoading = isLoading,
                    modifier = Modifier.fillMaxWidth(),
                    icon = Icons.Default.CheckCircle,
                    containerColor = if (variance == 0) PremiumSuccess else PremiumWarning
                )
            }
        }
    }
}
