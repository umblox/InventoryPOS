package com.inventorypos.presentation.screens.inventory.supplier

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.inventorypos.presentation.components.common.CustomButton
import com.inventorypos.presentation.navigation.Screen
import com.inventorypos.presentation.theme.*

@Composable
fun SupplierDeleteDialog(
    navController: NavController,
    supplierId: Long,
    viewModel: SupplierDeleteViewModel = hiltViewModel()
) {
    LaunchedEffect(supplierId) { viewModel.loadSupplier(supplierId) }
    
    val supplier by viewModel.supplier.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()
    val isDeleting by viewModel.isDeleting.collectAsState()
    val isDeleted by viewModel.isDeleted.collectAsState()
    
    LaunchedEffect(isDeleted) {
        if (isDeleted) {
            navController.popBackStack(Screen.SupplierList.route, inclusive = false)
        }
    }
    
    AlertDialog(
        onDismissRequest = { navController.popBackStack() },
        containerColor = PremiumDarkSurface,
        shape = RoundedCornerShape(24.dp),
        icon = {
            Box(
                modifier = Modifier.size(64.dp).clip(RoundedCornerShape(20.dp)).background(PremiumError.copy(alpha = 0.15f)),
                contentAlignment = Alignment.Center
            ) {
                Icon(Icons.Default.Warning, contentDescription = "Warning", tint = PremiumError, modifier = Modifier.size(32.dp))
            }
        },
        title = {
            Text(
                text = "Delete Supplier?",
                style = MaterialTheme.typography.headlineSmall,
                color = PremiumTextPrimary,
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth()
            )
        },
        text = {
            Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.fillMaxWidth()) {
                if (isLoading) {
                    CircularProgressIndicator(color = PremiumGold)
                } else {
                    Text(
                        text = "Are you sure you want to delete \"${supplier?.name ?: "this supplier"}\"?",
                        style = MaterialTheme.typography.bodyMedium, color = PremiumTextSecondary, textAlign = TextAlign.Center
                    )
                }
            }
        },
        confirmButton = {
            CustomButton(
                text = "Delete", onClick = { viewModel.deleteSupplier(supplierId) },
                isLoading = isDeleting, containerColor = PremiumError, contentColor = PremiumTextPrimary
            )
        },
        dismissButton = {
            OutlinedButton(
                onClick = { navController.popBackStack() },
                colors = ButtonDefaults.outlinedButtonColors(contentColor = PremiumTextSecondary)
            ) {
                Text("Cancel")
            }
        }
    )
}

