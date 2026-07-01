package com.inventorypos.presentation.screens.inventory.product

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.inventorypos.domain.model.Category
import com.inventorypos.domain.model.Supplier
import com.inventorypos.presentation.components.common.*
import com.inventorypos.presentation.theme.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProductAddScreen(
    navController: NavController,
    viewModel: ProductAddViewModel = hiltViewModel()
) {
    val name by viewModel.name.collectAsState()
    val sku by viewModel.sku.collectAsState()
    val categoryId by viewModel.categoryId.collectAsState()
    val supplierId by viewModel.supplierId.collectAsState() // <--- TANGKAP STATE SUPPLIER
    val buyPrice by viewModel.buyPrice.collectAsState()
    val sellPrice by viewModel.sellPrice.collectAsState()
    val stock by viewModel.stock.collectAsState()
    val minStock by viewModel.minStock.collectAsState()
    val description by viewModel.description.collectAsState()
    
    val categories by viewModel.categories.collectAsState()
    val suppliers by viewModel.suppliers.collectAsState() // <--- TANGKAP LIST SUPPLIER
    
    val isLoading by viewModel.isLoading.collectAsState()
    val isSuccess by viewModel.isSuccess.collectAsState()
    val error by viewModel.error.collectAsState()
    
    val context = LocalContext.current
    
    LaunchedEffect(isSuccess) {
        if (isSuccess) {
            Toast.makeText(context, "Produk berhasil ditambahkan!", Toast.LENGTH_SHORT).show()
            navController.popBackStack()
        }
    }
    
    Scaffold(
        topBar = {
            CustomTopBar(
                title = "Add New Product",
                subtitle = "Fill in product details",
                onBackClick = { navController.popBackStack() }
            )
        },
        containerColor = PremiumDarkBackground
    ) { padding ->
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
                
                SectionHeader(icon = Icons.Default.Info, title = "Basic Information")
                
                CustomTextField(
                    value = name,
                    onValueChange = viewModel::onNameChange,
                    label = "Product Name *",
                    placeholder = "Enter product name",
                    leadingIcon = Icons.Default.Label,
                    keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next)
                )
                
                CustomTextField(
                    value = sku,
                    onValueChange = viewModel::onSkuChange,
                    label = "SKU / Barcode *",
                    placeholder = "Enter SKU or scan barcode",
                    leadingIcon = Icons.Default.QrCode,
                    trailingIcon = Icons.Default.DocumentScanner,
                    onTrailingClick = { /* Open barcode scanner */ },
                    keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next)
                )
                
                CategoryDropdown(
                    categories = categories,
                    selectedCategoryId = categoryId,
                    onCategorySelected = viewModel::onCategoryChange
                )
                
                SectionHeader(icon = Icons.Default.AttachMoney, title = "Pricing & Supplier")
                
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    CustomTextField(
                        value = buyPrice,
                        onValueChange = viewModel::onBuyPriceChange,
                        label = "Buy Price *",
                        placeholder = "0",
                        leadingIcon = Icons.Default.ShoppingCart,
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number, imeAction = ImeAction.Next),
                        modifier = Modifier.weight(1f)
                    )
                    CustomTextField(
                        value = sellPrice,
                        onValueChange = viewModel::onSellPriceChange,
                        label = "Sell Price *",
                        placeholder = "0",
                        leadingIcon = Icons.Default.Sell,
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number, imeAction = ImeAction.Next),
                        modifier = Modifier.weight(1f)
                    )
                }

                // <--- DROPDOWN UNTUK SUPPLIER DITAMBAHKAN DI SINI
                SupplierDropdown(
                    suppliers = suppliers,
                    selectedSupplierId = supplierId,
                    onSupplierSelected = viewModel::onSupplierChange
                )
                
                SectionHeader(icon = Icons.Default.Inventory, title = "Stock Management")
                
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    CustomTextField(
                        value = stock,
                        onValueChange = viewModel::onStockChange,
                        label = "Initial Stock *",
                        placeholder = "0",
                        leadingIcon = Icons.Default.AddBox,
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number, imeAction = ImeAction.Next),
                        modifier = Modifier.weight(1f)
                    )
                    CustomTextField(
                        value = minStock,
                        onValueChange = viewModel::onMinStockChange,
                        label = "Min Stock Alert",
                        placeholder = "5",
                        leadingIcon = Icons.Default.Warning,
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number, imeAction = ImeAction.Next),
                        modifier = Modifier.weight(1f)
                    )
                }
                
                CustomTextField(
                    value = description,
                    onValueChange = viewModel::onDescriptionChange,
                    label = "Description",
                    placeholder = "Product description (optional)",
                    leadingIcon = Icons.Default.Description,
                    maxLines = 4,
                    singleLine = false
                )
                
                Spacer(modifier = Modifier.height(24.dp))
                
                CustomButton(
                    text = "Save Product",
                    onClick = viewModel::saveProduct,
                    isLoading = isLoading,
                    modifier = Modifier.fillMaxWidth()
                )
                
                if (error != null) {
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = error!!,
                        style = MaterialTheme.typography.bodyMedium,
                        color = PremiumError
                    )
                }
                
                Spacer(modifier = Modifier.height(32.dp))
            }
        }
    }
}

@Composable
fun SectionHeader(icon: androidx.compose.ui.graphics.vector.ImageVector, title: String) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp)
    ) {
        Icon(icon, contentDescription = null, tint = PremiumGold, modifier = Modifier.size(24.dp))
        Spacer(modifier = Modifier.width(8.dp))
        Text(title, style = MaterialTheme.typography.titleMedium, color = PremiumTextPrimary)
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CategoryDropdown(
    categories: List<Category>,
    selectedCategoryId: Long?,
    onCategorySelected: (Long) -> Unit
) {
    var expanded by remember { mutableStateOf(false) }
    val selectedCategoryName = categories.find { it.id == selectedCategoryId }?.name ?: "Select Category"

    ExposedDropdownMenuBox(expanded = expanded, onExpandedChange = { expanded = !expanded }) {
        OutlinedTextField(
            value = selectedCategoryName,
            onValueChange = {},
            readOnly = true,
            label = { Text("Category", color = PremiumTextSecondary) },
            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
            modifier = Modifier.fillMaxWidth().menuAnchor(),
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = PremiumGold, unfocusedBorderColor = PremiumDarkSurface,
                focusedTextColor = PremiumTextPrimary, unfocusedTextColor = PremiumTextPrimary
            )
        )
        ExposedDropdownMenu(expanded = expanded, onDismissRequest = { expanded = false }, modifier = Modifier.background(PremiumDarkSurface)) {
            if (categories.isEmpty()) {
                DropdownMenuItem(text = { Text("No Categories Available", color = PremiumTextMuted) }, onClick = { expanded = false })
            } else {
                categories.forEach { category ->
                    DropdownMenuItem(
                        text = { Text(category.name, color = PremiumTextPrimary) },
                        onClick = { onCategorySelected(category.id); expanded = false }
                    )
                }
            }
        }
    }
}

// <--- KOMPONEN BARU UNTUK DROPDOWN SUPPLIER
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SupplierDropdown(
    suppliers: List<Supplier>,
    selectedSupplierId: Long?,
    onSupplierSelected: (Long) -> Unit
) {
    var expanded by remember { mutableStateOf(false) }
    val selectedSupplierName = suppliers.find { it.id == selectedSupplierId }?.name ?: "Select Primary Supplier (Optional)"

    ExposedDropdownMenuBox(expanded = expanded, onExpandedChange = { expanded = !expanded }) {
        OutlinedTextField(
            value = selectedSupplierName,
            onValueChange = {},
            readOnly = true,
            label = { Text("Primary Supplier", color = PremiumTextSecondary) },
            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
            modifier = Modifier.fillMaxWidth().menuAnchor(),
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = PremiumGold, unfocusedBorderColor = PremiumDarkSurface,
                focusedTextColor = PremiumTextPrimary, unfocusedTextColor = PremiumTextPrimary
            )
        )
        ExposedDropdownMenu(expanded = expanded, onDismissRequest = { expanded = false }, modifier = Modifier.background(PremiumDarkSurface)) {
            if (suppliers.isEmpty()) {
                DropdownMenuItem(text = { Text("No Suppliers Available", color = PremiumTextMuted) }, onClick = { expanded = false })
            } else {
                // Opsi tambahan untuk mengosongkan/membatalkan pilihan supplier
                DropdownMenuItem(
                    text = { Text("-- No Supplier --", color = PremiumTextMuted) },
                    onClick = { onSupplierSelected(-1L); expanded = false } // -1L atau null handling di luar
                )
                suppliers.forEach { supplier ->
                    DropdownMenuItem(
                        text = { Text(supplier.name, color = PremiumTextPrimary) },
                        onClick = { onSupplierSelected(supplier.id); expanded = false }
                    )
                }
            }
        }
    }
}
