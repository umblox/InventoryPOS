package com.inventorypos.presentation.screens.inventory.category

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.inventorypos.presentation.components.common.*
import com.inventorypos.presentation.theme.*

@Composable
fun CategoryEditScreen(
    navController: NavController,
    categoryId: Long,
    viewModel: CategoryEditViewModel = hiltViewModel()
) {
    LaunchedEffect(categoryId) { viewModel.loadCategory(categoryId) }
    
    val name by viewModel.name.collectAsState()
    val description by viewModel.description.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()
    val isSaving by viewModel.isSaving.collectAsState()
    val isSuccess by viewModel.isSuccess.collectAsState()
    val error by viewModel.error.collectAsState()
    
    LaunchedEffect(isSuccess) { if (isSuccess) navController.popBackStack() }
    
    Scaffold(
        topBar = {
            CustomTopBar(
                title = "Edit Category",
                subtitle = "ID: #$categoryId",
                onBackClick = { navController.popBackStack() }
            )
        },
        containerColor = PremiumDarkBackground
    ) { padding ->
        if (isLoading) {
            LoadingIndicator()
        } else {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState())
                    .padding(padding)
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                CustomTextField(
                    value = name,
                    onValueChange = viewModel::onNameChange,
                    label = "Category Name *",
                    leadingIcon = Icons.Default.Label
                )
                
                CustomTextField(
                    value = description,
                    onValueChange = viewModel::onDescriptionChange,
                    label = "Description",
                    leadingIcon = Icons.Default.Description,
                    maxLines = 3,
                    singleLine = false
                )
                
                if (error != null) {
                    Text(text = error!!, color = PremiumError, style = MaterialTheme.typography.bodySmall)
                }
                
                Spacer(modifier = Modifier.height(8.dp))
                
                CustomButton(
                    text = "Update Category",
                    onClick = { viewModel.updateCategory(categoryId) },
                    isLoading = isSaving,
                    modifier = Modifier.fillMaxWidth()
                )
            }
        }
    }
}
