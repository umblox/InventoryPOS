package com.inventorypos.presentation.screens.inventory.product

import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
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
    LaunchedEffect(productId) { viewModel.loadProduct(productId) }
    
    val product by viewModel.product.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()
    val isSaving by viewModel.isSaving.collectAsState()
    val isSuccess by viewModel.isSuccess.collectAsState()
    val error by viewModel.error.collectAsState()
    
    // Field States
    val name by viewModel.name.collectAsState()
    val sku by viewModel.sku.collectAsState()
    val categoryId by viewModel.categoryId.collectAsState()
    val buyPrice by viewModel.buyPrice.collectAsState()
    val sellPrice by viewModel.sellPrice.collectAsState()
    val stock by viewModel.stock.collectAsState()
    val minStock by viewModel.minStock.collectAsState()
    val description by viewModel.description.collectAsState()
    
    // Categories List
    val categories by viewModel.categories.collectAsState()
    val context = LocalContext.current
    
    LaunchedEffect(isSuccess) {
        if (isSuccess) {
            Toast.makeText(context, "Produk berhasil diupdate!", Toast.LENGTH_SHORT).show()
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
            Box(modifier = Modifier.fillMaxSize().padding(padding)) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .verticalScroll(rememberScrollState())
                        .padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    // Placeholder (Pastikan komponen ini sudah ada di project Anda)
                    // ProductImagePicker(currentImage = product?.imageUrl, onImageSelected = viewModel::onImageSelected)
                    
                    SectionHeader(icon = Icons.Default.Info, title = "Basic Information")
                    
                    CustomTextField(
                        value = name,
                        onValueChange = viewModel::onNameChange,
                        label = "Product Name *",
                        leadingIcon = Icons.Default.Label,
                        keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next)
                    )
                    
                    CustomTextField(
                        value = sku,
                        onValueChange = viewModel::onSkuChange,
                        label = "SKU / Barcode *",
                        leadingIcon = Icons.Default.QrCode,
                        keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next)
                    )
                    
                    // Reusing CategoryDropdown from Add Screen (Pastikan fungsi ini bisa diakses, 
                    // jika tidak, letakkan copy fungsinya di file ini atau di komponen terpisah)
                    CategoryDropdown(
                        categories = categories,
                        selectedCategoryId = categoryId,
                        onCategorySelected = viewModel::onCategoryChange
                    )
                    
                    SectionHeader(icon = Icons.Default.AttachMoney, title = "Pricing")
                    
                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                        CustomTextField(
                            value = buyPrice, onValueChange = viewModel::onBuyPriceChange, label = "Buy Price *",
                            leadingIcon = Icons.Default.ShoppingCart, modifier = Modifier.weight(1f),
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number, imeAction = ImeAction.Next)
                        )
                        CustomTextField(
                            value = sellPrice, onValueChange = viewModel::onSellPriceChange, label = "Sell Price *",
                            leadingIcon = Icons.Default.Sell, modifier = Modifier.weight(1f),
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number, imeAction = ImeAction.Next)
                        )
                    }
                    
                    SectionHeader(icon = Icons.Default.Inventory, title = "Stock Management")
                    
                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                        CustomTextField(
                            value = stock, onValueChange = viewModel::onStockChange, label = "Stock *",
                            leadingIcon = Icons.Default.AddBox, modifier = Modifier.weight(1f),
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number, imeAction = ImeAction.Next)
                        )
                        CustomTextField(
                            value = minStock, onValueChange = viewModel::onMinStockChange, label = "Min Alert",
                            leadingIcon = Icons.Default.Warning, modifier = Modifier.weight(1f),
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number, imeAction = ImeAction.Next)
                        )
                    }
                    
                    CustomTextField(
                        value = description, onValueChange = viewModel::onDescriptionChange, label = "Description",
                        leadingIcon = Icons.Default.Description, maxLines = 4, singleLine = false
                    )
                    
                    if (error != null) {
                        Text(text = error!!, style = MaterialTheme.typography.bodyMedium, color = PremiumError, modifier = Modifier.fillMaxWidth())
                    }
                    
                    Spacer(modifier = Modifier.height(24.dp))
                    
                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                        CustomButton(
                            text = "Update", onClick = { viewModel.updateProduct(productId) },
                            isLoading = isSaving, modifier = Modifier.weight(1f)
                        )
                        OutlinedButton(
                            onClick = { navController.popBackStack() }, modifier = Modifier.weight(1f),
                            colors = ButtonDefaults.outlinedButtonColors(contentColor = PremiumTextSecondary)
                        ) {
                            Text("Cancel")
                        }
                    }
                    Spacer(modifier = Modifier.height(32.dp))
                }
            }
        }
    }
}
