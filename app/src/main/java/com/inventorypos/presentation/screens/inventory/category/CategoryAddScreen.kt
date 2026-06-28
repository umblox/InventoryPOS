package com.inventorypos.presentation.screens.inventory.category

import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.inventorypos.presentation.components.common.*
import com.inventorypos.presentation.theme.*

@Composable
fun CategoryAddScreen(
    navController: NavController,
    viewModel: CategoryAddViewModel = hiltViewModel()
) {
    val name by viewModel.name.collectAsState()
    val description by viewModel.description.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()
    val isSuccess by viewModel.isSuccess.collectAsState()
    val error by viewModel.error.collectAsState()
    
    val context = LocalContext.current
    
    LaunchedEffect(isSuccess) {
        if (isSuccess) {
            Toast.makeText(context, "Kategori berhasil disimpan!", Toast.LENGTH_SHORT).show()
            navController.popBackStack()
        }
    }
    
    Scaffold(
        topBar = {
            CustomTopBar(
                title = "Add Category",
                subtitle = "Create new category",
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
                value = name,
                onValueChange = viewModel::onNameChange,
                label = "Category Name *",
                placeholder = "Enter category name",
                leadingIcon = Icons.Default.Label
            )
            
            CustomTextField(
                value = description,
                onValueChange = viewModel::onDescriptionChange,
                label = "Description",
                placeholder = "Optional description",
                leadingIcon = Icons.Default.Description,
                maxLines = 3,
                singleLine = false
            )
            
            if (error != null) {
                Text(text = error!!, color = PremiumError, style = MaterialTheme.typography.bodySmall)
            }
            
            Spacer(modifier = Modifier.height(8.dp))
            
            CustomButton(
                text = "Save Category",
                onClick = viewModel::saveCategory,
                isLoading = isLoading,
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}
