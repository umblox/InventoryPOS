package com.inventorypos.presentation.screens.inventory.product

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.inventorypos.presentation.components.common.*
import com.inventorypos.presentation.theme.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProductEditScreen(
    navController: NavController,
    productId: Long,
    viewModel: ProductEditViewModel = hiltViewModel()
) {
    // Load product data
    LaunchedEffect(productId) {
        viewModel.loadProduct(productId)
    }
    
    val product by viewModel.product.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()
    val isSaving by viewModel.isSaving.collectAsState()
    val isSuccess by viewModel.isSuccess.collectAsState()
    
    // PERBAIKAN 1: Memantau nilai 'name' menggunakan collectAsState
    val name by viewModel.name.collectAsState()
    
    // Memantau nilai 'error' (Untuk menyelesaikan warning "Variable 'error' is never used")
    val error by viewModel.error.collectAsState()
    
    LaunchedEffect(isSuccess) {
        if (isSuccess) {
            navController.popBackStack()
        }
    }
    
    Scaffold(
        topBar = {
            CustomTopBar(
                title = "Edit Product",
                subtitle = "ID: #$productId",
                onBackClick = { navController.popBackStack() }
            )
        },
        containerColor = PremiumDarkBackground
    ) { padding ->
        if (isLoading) {
            LoadingIndicator()
        } else {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .verticalScroll(rememberScrollState())
                        .padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    // Same fields as Add, but pre-filled with product data
                    ProductImagePicker(
                        currentImage = product?.imageUrl,
                        onImageSelected = viewModel::onImageSelected
                    )
                    
                    SectionHeader(
                        icon = Icons.Default.Info,
                        title = "Basic Information"
                    )
                    
                    // PERBAIKAN 2: Gunakan 'name' yang sudah di-collect, BUKAN viewModel.name.value
                    CustomTextField(
                        value = name,
                        onValueChange = viewModel::onNameChange,
                        label = "Product Name",
                        leadingIcon = Icons.Default.Label
                    )
                    
                    // ... (same fields as Add, pre-filled)
                    
                    // PERBAIKAN 3: Menampilkan error jika ada, agar variabel error terpakai
                    if (error != null) {
                        Text(
                            text = error!!,
                            style = MaterialTheme.typography.bodyMedium,
                            color = PremiumError,
                            modifier = Modifier.fillMaxWidth()
                        )
                    }
                    
                    Spacer(modifier = Modifier.height(24.dp))
                    
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        CustomButton(
                            text = "Update",
                            onClick = { viewModel.updateProduct(productId) },
                            isLoading = isSaving,
                            modifier = Modifier.weight(1f)
                        )
                        OutlinedButton(
                            onClick = { navController.popBackStack() },
                            modifier = Modifier.weight(1f),
                            colors = ButtonDefaults.outlinedButtonColors(
                                contentColor = PremiumTextSecondary
                            )
                        ) {
                            Text("Cancel")
                        }
                    }
                }
            }
        }
    }
}
