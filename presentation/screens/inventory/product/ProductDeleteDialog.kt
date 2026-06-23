package com.inventorypos.presentation.screens.inventory.product

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DeleteForever
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
import com.inventorypos.presentation.theme.*

@Composable
fun ProductDeleteDialog(
    navController: NavController,
    productId: Long,
    viewModel: ProductDeleteViewModel = hiltViewModel()
) {
    LaunchedEffect(productId) {
        viewModel.loadProduct(productId)
    }
    
    val product by viewModel.product.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()
    val isDeleting by viewModel.isDeleting.collectAsState()
    val isDeleted by viewModel.isDeleted.collectAsState()
    
    LaunchedEffect(isDeleted) {
        if (isDeleted) {
            navController.popBackStack(Screen.ProductList.route, inclusive = false)
        }
    }
    
    AlertDialog(
        onDismissRequest = { navController.popBackStack() },
        containerColor = PremiumDarkSurface,
        shape = RoundedCornerShape(24.dp),
        icon = {
            Box(
                modifier = Modifier
                    .size(64.dp)
                    .clip(RoundedCornerShape(20.dp))
                    .background(PremiumError.copy(alpha = 0.15f)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    Icons.Default.Warning,
                    contentDescription = "Warning",
                    tint = PremiumError,
                    modifier = Modifier.size(32.dp)
                )
            }
        },
        title = {
            Text(
                text = "Delete Product?",
                style = MaterialTheme.typography.headlineSmall,
                color = PremiumTextPrimary,
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth()
            )
        },
        text = {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.fillMaxWidth()
            ) {
                if (isLoading) {
                    CircularProgressIndicator(color = PremiumGold)
                } else {
                    Text(
                        text = "Are you sure you want to delete \"${product?.name ?: "this product"}\"?",
                        style = MaterialTheme.typography.bodyMedium,
                        color = PremiumTextSecondary,
                        textAlign = TextAlign.Center
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "This action cannot be undone. All associated data will be permanently removed.",
                        style = MaterialTheme.typography.bodySmall,
                                               color = PremiumError.copy(alpha = 0.8f),
                        textAlign = TextAlign.Center
                    )
                }
            }
        },
        confirmButton = {
            CustomButton(
                text = "Delete Permanently",
                onClick = { viewModel.deleteProduct(productId) },
                isLoading = isDeleting,
                containerColor = PremiumError,
                contentColor = PremiumTextPrimary
            )
        },
        dismissButton = {
            OutlinedButton(
                onClick = { navController.popBackStack() },
                colors = ButtonDefaults.outlinedButtonColors(
                    contentColor = PremiumTextSecondary
                )
            ) {
                Text("Cancel")
            }
        }
    )
}

