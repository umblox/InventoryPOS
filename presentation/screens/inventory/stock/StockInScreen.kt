package com.inventorypos.presentation.screens.inventory.stock

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
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
fun StockInScreen(
    navController: NavController,
    viewModel: StockInViewModel = hiltViewModel()
) {
    val productSearch by viewModel.productSearch.collectAsState()
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
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(padding)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            CustomTextField(
                value = productSearch,
                onValueChange = viewModel::onProductSearchChange,
                label = "Product *",
                placeholder = "Search product by name or SKU",
                leadingIcon = Icons.Default.Search,
                trailingIcon = Icons.Default.QrCodeScanner,
                onTrailingClick = { /* Scan barcode */ }
            )
            
            CustomTextField(
                value = quantity,
                onValueChange = viewModel::onQuantityChange,
                label = "Quantity *",
                placeholder = "Enter quantity received",
                leadingIcon = Icons.Default.AddBox,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
            )
            
            CustomTextField(
                value = notes,
                onValueChange = viewModel::onNotesChange,
                label = "Notes",
                placeholder = "Optional notes",
                leadingIcon = Icons.Default.Notes,
                maxLines = 3,
                singleLine = false
            )
            
            if (error != null) {
                Text(text = error!!, color = PremiumError, style = MaterialTheme.typography.bodySmall)
            }
            
            Spacer(modifier = Modifier.height(8.dp))
            
            CustomButton(
                text = "Confirm Stock In",
                onClick = viewModel::confirmStockIn,
                isLoading = isLoading,
                modifier = Modifier.fillMaxWidth(),
                icon = Icons.Default.CheckCircle
            )
        }
    }
}
