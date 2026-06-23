package com.inventorypos.presentation.screens.inventory.category

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DeleteForever
import androidx.compose.runtime.*
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.inventorypos.presentation.components.common.CustomButton
import com.inventorypos.presentation.components.common.PremiumAlertDialog
import com.inventorypos.presentation.navigation.Screen
import com.inventorypos.presentation.theme.PremiumError

@Composable
fun CategoryDeleteDialog(
    navController: NavController,
    categoryId: Long,
    viewModel: CategoryDeleteViewModel = hiltViewModel()
) {
    LaunchedEffect(categoryId) { viewModel.loadCategory(categoryId) }
    
    val category by viewModel.category.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()
    val isDeleting by viewModel.isDeleting.collectAsState()
    val isDeleted by viewModel.isDeleted.collectAsState()
    
    LaunchedEffect(isDeleted) {
        if (isDeleted) {
            navController.popBackStack(Screen.CategoryList.route, inclusive = false)
        }
    }
    
    PremiumAlertDialog(
        onDismissRequest = { navController.popBackStack() },
        icon = Icons.Default.DeleteForever,
        iconColor = PremiumError,
        title = "Delete Category?",
        text = {
            if (isLoading) {
                androidx.compose.material3.CircularProgressIndicator()
            } else {
                androidx.compose.material3.Text(
                    text = "Delete \"${category?.name ?: "this category"}\"? Products in this category will be uncategorized.",
                    color = com.inventorypos.presentation.theme.PremiumTextSecondary
                )
            }
        },
        confirmButton = {
            CustomButton(
                text = "Delete",
                onClick = { viewModel.deleteCategory(categoryId) },
                isLoading = isDeleting,
                containerColor = PremiumError
            )
        },
        dismissButton = {
            androidx.compose.material3.OutlinedButton(
                onClick = { navController.popBackStack() }
            ) {
                androidx.compose.material3.Text("Cancel")
            }
        }
    )
}
