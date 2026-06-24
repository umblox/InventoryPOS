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
fun StockOpnameScreen(
    navController: NavController,
    viewModel: StockOpnameViewModel = hiltViewModel()
) {
    val productSearch by viewModel.productSearch.collectAsState()
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
                placeholder = "Search product",
                leadingIcon = Icons.Default.Search
            )
            
            CustomTextField(
                value = systemStock,
                onValueChange = { /* Read only */ },
                label = "System Stock",
                placeholder = "Auto-filled",
                leadingIcon = Icons.Default.Computer,
                readOnly = true
            )
            
            CustomTextField(
                value = physicalStock,
                onValueChange = viewModel::onPhysicalStockChange,
                label = "Physical Stock *",
                placeholder = "Enter actual count",
                leadingIcon = Icons.Default.FactCheck,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
            )
            
            // Variance display
            if (systemStock.isNotBlank() && physicalStock.isNotBlank()) {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = MaterialTheme.shapes.large,
                    colors = CardDefaults.cardColors(
                        containerColor = if (variance == 0) PremiumSuccess.copy(alpha = 0.1f) 
                        else PremiumWarning.copy(alpha = 0.1f)
                    )
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text("Variance", color = PremiumTextSecondary)
                        Text(
                            "${if (variance >= 0) "+" else ""}$variance",
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
            
            Spacer(modifier = Modifier.height(8.dp))
            
            CustomButton(
                text = "Confirm Opname",
                onClick = viewModel::confirmOpname,
                isLoading = isLoading,
                modifier = Modifier.fillMaxWidth(),
                icon = Icons.Default.CheckCircle
            )
        }
    }
}
