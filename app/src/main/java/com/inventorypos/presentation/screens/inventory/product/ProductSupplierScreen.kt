package com.inventorypos.presentation.screens.inventory.product

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.inventorypos.domain.model.Supplier
import com.inventorypos.domain.model.SupplierOffer
import com.inventorypos.presentation.components.common.*
import com.inventorypos.presentation.theme.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProductSupplierScreen(
    navController: NavController,
    productId: Long,
    viewModel: ProductSupplierViewModel = hiltViewModel()
) {
    LaunchedEffect(productId) { viewModel.loadSuppliers(productId) }

    val suppliers by viewModel.suppliers.collectAsState()
    val allSuppliers by viewModel.allSuppliers.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()
    
    val showAddDialog by viewModel.showAddDialog.collectAsState()
    val showEditDialog by viewModel.showEditDialog.collectAsState()
    val editingSupplier by viewModel.editingSupplier.collectAsState()

    Scaffold(
        topBar = {
            CustomTopBar(
                title = "Product Suppliers",
                subtitle = "Manage suppliers & prices",
                onBackClick = { navController.popBackStack() }
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = { viewModel.onShowAddDialog() },
                containerColor = PremiumGold,
                contentColor = PremiumDarkBackground
            ) {
                Icon(Icons.Default.Add, "Add Supplier")
            }
        },
        containerColor = PremiumDarkBackground
    ) { padding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
        ) {
            if (isLoading) {
                LoadingIndicator()
            } else if (suppliers.isEmpty()) {
                EmptyState(
                    icon = Icons.Default.LocalShipping,
                    title = "No Suppliers",
                    message = "Add supplier for this product"
                )
            } else {
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(16.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    items(suppliers) { offer ->
                        SupplierOfferCard(
                            offer = offer,
                            onSetPrimary = { viewModel.setPrimary(productId, offer.supplierId) },
                            onEdit = { viewModel.onShowEditDialog(offer) }, // Panggilan onEdit sudah aman
                            onDelete = { viewModel.removeSupplier(productId, offer.supplierId) }
                        )
                    }
                }
            }
        }

        // Dialog Tambah Supplier
        if (showAddDialog) {
            AddSupplierDialog(
                availableSuppliers = allSuppliers,
                onDismiss = { viewModel.onDismissDialog() },
                onConfirm = { supplierId, buyPrice, isPrimary ->
                    viewModel.addSupplier(productId, supplierId, buyPrice, isPrimary)
                }
            )
        }

        // Dialog Edit Harga Supplier
        if (showEditDialog && editingSupplier != null) {
            EditSupplierPriceDialog(
                offer = editingSupplier!!,
                onDismiss = { viewModel.onDismissEditDialog() },
                onConfirm = { newPrice ->
                    viewModel.updateSupplierPrice(productId, editingSupplier!!.supplierId, newPrice)
                }
            )
        }
    }
}

@Composable
fun SupplierOfferCard(
    offer: SupplierOffer,
    onSetPrimary: () -> Unit,
    onEdit: () -> Unit, // <--- PERBAIKAN: Parameter onEdit ditambahkan ke deklarasi komponen
    onDelete: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = MaterialTheme.shapes.large,
        colors = CardDefaults.cardColors(
            containerColor = if (offer.isPrimary) PremiumGold.copy(alpha = 0.1f) else PremiumDarkSurface
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = offer.supplierName,
                        style = MaterialTheme.typography.titleMedium,
                        color = PremiumTextPrimary
                    )
                    if (offer.isPrimary) {
                        Spacer(modifier = Modifier.width(8.dp))
                        Surface(
                            color = PremiumGold.copy(alpha = 0.2f),
                            shape = MaterialTheme.shapes.small
                        ) {
                            Text(
                                text = "PRIMARY",
                                style = MaterialTheme.typography.labelSmall,
                                color = PremiumGold,
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 2.dp)
                            )
                        }
                    }
                }
                Text(
                    text = "Buy Price: Rp ${String.format("%,.0f", offer.buyPrice)}",
                    style = MaterialTheme.typography.bodyMedium,
                    color = PremiumTextSecondary
                )
            }
            Row {
                if (!offer.isPrimary) {
                    IconButton(onClick = onSetPrimary) {
                        Icon(Icons.Default.Star, "Set Primary", tint = PremiumGold)
                    }
                }
                // <--- PERBAIKAN: Tombol Edit ditambahkan ke UI
                IconButton(onClick = onEdit) {
                    Icon(Icons.Default.Edit, "Edit Price", tint = PremiumInfo)
                }
                IconButton(onClick = onDelete) {
                    Icon(Icons.Default.Delete, "Delete", tint = PremiumError)
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddSupplierDialog(
    availableSuppliers: List<Supplier>,
    onDismiss: () -> Unit,
    onConfirm: (supplierId: Long, buyPrice: Double, isPrimary: Boolean) -> Unit
) {
    var selectedSupplier by remember { mutableStateOf<Supplier?>(null) }
    var buyPrice by remember { mutableStateOf("") }
    var isPrimary by remember { mutableStateOf(false) }
    var expanded by remember { mutableStateOf(false) }

    AlertDialog(
        onDismissRequest = onDismiss,
        containerColor = PremiumDarkSurface,
        title = { Text("Add Supplier", color = PremiumGold) },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                ExposedDropdownMenuBox(
                    expanded = expanded,
                    onExpandedChange = { expanded = !expanded }
                ) {
                    OutlinedTextField(
                        value = selectedSupplier?.name ?: "Select Supplier",
                        onValueChange = {},
                        readOnly = true,
                        label = { Text("Supplier", color = PremiumTextSecondary) },
                        trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
                        modifier = Modifier.fillMaxWidth().menuAnchor(),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = PremiumGold,
                            unfocusedBorderColor = PremiumDarkSurface,
                            focusedTextColor = PremiumTextPrimary
                        )
                    )
                    ExposedDropdownMenu(
                        expanded = expanded,
                        onDismissRequest = { expanded = false },
                        modifier = Modifier.background(PremiumDarkSurface)
                    ) {
                        availableSuppliers.forEach { supplier ->
                            DropdownMenuItem(
                                text = { Text(supplier.name, color = PremiumTextPrimary) },
                                onClick = {
                                    selectedSupplier = supplier
                                    expanded = false
                                }
                            )
                        }
                    }
                }

                CustomTextField(
                    value = buyPrice,
                    onValueChange = { buyPrice = it },
                    label = "Buy Price",
                    placeholder = "0",
                    leadingIcon = Icons.Default.AttachMoney,
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
                )

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Switch(
                        checked = isPrimary,
                        onCheckedChange = { isPrimary = it },
                        colors = SwitchDefaults.colors(checkedThumbColor = PremiumGold)
                    )
                    Text("Set as Primary Supplier", color = PremiumTextPrimary)
                }
            }
        },
        confirmButton = {
            CustomButton(
                text = "Add",
                onClick = {
                    selectedSupplier?.let {
                        onConfirm(it.id, buyPrice.toDoubleOrNull() ?: 0.0, isPrimary)
                    }
                },
                enabled = selectedSupplier != null && buyPrice.isNotBlank()
            )
        },
        dismissButton = {
            OutlinedButton(onClick = onDismiss) {
                Text("Cancel", color = PremiumTextSecondary)
            }
        }
    )
}

// <--- PERBAIKAN: Komponen Dialog Edit Harga ditambahkan
@Composable
fun EditSupplierPriceDialog(
    offer: SupplierOffer,
    onDismiss: () -> Unit,
    onConfirm: (newPrice: Double) -> Unit
) {
    // Otomatis terisi dengan harga lama
    var buyPrice by remember { mutableStateOf(offer.buyPrice.toLong().toString()) }

    AlertDialog(
        onDismissRequest = onDismiss,
        containerColor = PremiumDarkSurface,
        title = { Text("Edit Price", color = PremiumGold) },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                Text(
                    text = "Supplier: ${offer.supplierName}",
                    style = MaterialTheme.typography.bodyMedium,
                    color = PremiumTextPrimary
                )
                CustomTextField(
                    value = buyPrice,
                    onValueChange = { buyPrice = it },
                    label = "New Buy Price",
                    placeholder = "0",
                    leadingIcon = Icons.Default.AttachMoney,
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
                )
            }
        },
        confirmButton = {
            CustomButton(
                text = "Save",
                onClick = {
                    onConfirm(buyPrice.toDoubleOrNull() ?: offer.buyPrice)
                },
                enabled = buyPrice.isNotBlank()
            )
        },
        dismissButton = {
            OutlinedButton(onClick = onDismiss) {
                Text("Cancel", color = PremiumTextSecondary)
            }
        }
    )
}
