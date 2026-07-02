package com.inventorypos.presentation.screens.inventory.stock

import android.widget.Toast
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.inventorypos.domain.model.Product
import com.inventorypos.presentation.components.common.*
import com.inventorypos.presentation.theme.*

@Composable
fun StockOpnameScreen(
    navController: NavController,
    viewModel: StockOpnameViewModel = hiltViewModel()
) {
    val products by viewModel.displayedProducts.collectAsState()
    val searchQuery by viewModel.searchQuery.collectAsState()
    val opnameInputs by viewModel.opnameInputs.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()
    val isSuccess by viewModel.isSuccess.collectAsState()

    val context = LocalContext.current

    LaunchedEffect(isSuccess) {
        if (isSuccess) {
            Toast.makeText(context, "Opname berhasil! Stok telah disesuaikan.", Toast.LENGTH_LONG).show()
            navController.popBackStack()
        }
    }

    Scaffold(
        topBar = {
            CustomTopBar(
                title = "Stock Opname",
                subtitle = "Hitung dan sesuaikan fisik barang",
                onBackClick = { navController.popBackStack() }
            )
        },
        bottomBar = {
            // Hanya muncul jika ada minimal 1 barang yang di-opname
            if (opnameInputs.isNotEmpty()) {
                Surface(
                    color = PremiumDarkSurface,
                    shadowElevation = 16.dp,
                    modifier = Modifier.navigationBarsPadding() // Mencegah tertutup navigasi OS
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth().padding(16.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Text(
                                text = "${opnameInputs.size} Barang Dihitung",
                                style = MaterialTheme.typography.titleMedium,
                                color = PremiumTextPrimary
                            )
                            Text(
                                text = "Siap untuk disesuaikan",
                                style = MaterialTheme.typography.bodySmall,
                                color = PremiumInfo
                            )
                        }
                        CustomButton(
                            text = "Simpan Opname",
                            onClick = viewModel::finalizeOpname,
                            icon = Icons.Default.Save,
                            isLoading = isLoading
                        )
                    }
                }
            }
        },
        containerColor = PremiumDarkBackground
    ) { padding ->
        Column(modifier = Modifier.fillMaxSize().padding(padding)) {
            
            // Search Bar untuk mode pencarian manual/scanner barcode
            Box(modifier = Modifier.padding(16.dp)) {
                CustomTextField(
                    value = searchQuery,
                    onValueChange = viewModel::onSearchQueryChange,
                    placeholder = "Cari nama barang atau scan SKU/Barcode...",
                    leadingIcon = Icons.Default.Search,
                    modifier = Modifier.fillMaxWidth()
                )
            }

            if (isLoading && products.isEmpty()) {
                LoadingIndicator()
            } else if (products.isEmpty()) {
                EmptyState(
                    icon = Icons.Default.Inventory,
                    title = "Tidak ada barang",
                    message = "Barang tidak ditemukan atau database kosong."
                )
            } else {
                LazyColumn(
                    contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp),
                    modifier = Modifier.fillMaxSize()
                ) {
                    items(products) { product ->
                        val currentInput = opnameInputs[product.id]
                        OpnameItemCard(
                            product = product,
                            physicalCount = currentInput,
                            onCountChange = { qty -> viewModel.updatePhysicalCount(product.id, qty) },
                            onClear = { viewModel.clearInput(product.id) }
                        )
                    }
                    item { Spacer(modifier = Modifier.height(80.dp)) } // Ruang ekstra untuk BottomBar
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun OpnameItemCard(
    product: Product,
    physicalCount: Int?, // Null jika belum diisi
    onCountChange: (Int) -> Unit,
    onClear: () -> Unit
) {
    val isCounted = physicalCount != null
    
    // Warna berubah menjadi kehijauan jika sudah diisi, menandakan "Selesai dihitung"
    val containerColor = if (isCounted) PremiumSuccess.copy(alpha = 0.1f) else PremiumDarkSurface
    val borderColor = if (isCounted) PremiumSuccess.copy(alpha = 0.5f) else PremiumDarkSurface

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = containerColor),
        border = BorderStroke(1.dp, borderColor)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Top
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(product.name, style = MaterialTheme.typography.titleMedium, color = PremiumTextPrimary)
                    Text("SKU: ${product.sku}", style = MaterialTheme.typography.bodySmall, color = PremiumTextMuted)
                }
                if (isCounted) {
                    Icon(Icons.Default.CheckCircle, contentDescription = "Counted", tint = PremiumSuccess)
                }
            }

            Spacer(modifier = Modifier.height(12.dp))
            Divider(color = PremiumTextMuted.copy(alpha = 0.2f))
            Spacer(modifier = Modifier.height(12.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Info Sistem
                Column {
                    Text("Stok Sistem", style = MaterialTheme.typography.labelSmall, color = PremiumTextMuted)
                    Text("${product.stock} ${product.unit}", style = MaterialTheme.typography.titleMedium, color = PremiumGold)
                }

                // Info Selisih (Muncul jika ada input fisik)
                if (isCounted) {
                    val diff = physicalCount!! - product.stock
                    val (diffColor, diffText) = when {
                        diff > 0 -> PremiumInfo to "+$diff (Lebih)"
                        diff < 0 -> PremiumError to "$diff (Hilang)"
                        else -> PremiumTextSecondary to "Sesuai"
                    }
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text("Selisih", style = MaterialTheme.typography.labelSmall, color = PremiumTextMuted)
                        Text(diffText, style = MaterialTheme.typography.titleSmall, color = diffColor, fontWeight = FontWeight.Bold)
                    }
                }

                // Input Fisik Real
                Row(verticalAlignment = Alignment.CenterVertically) {
                    if (isCounted) {
                        IconButton(onClick = onClear, modifier = Modifier.size(32.dp)) {
                            Icon(Icons.Default.Close, contentDescription = "Batal", tint = PremiumError, modifier = Modifier.size(16.dp))
                        }
                    }
                    OutlinedTextField(
                        value = physicalCount?.toString() ?: "",
                        onValueChange = { onCountChange(it.toIntOrNull() ?: 0) },
                        placeholder = { Text("?", color = PremiumTextMuted, textAlign = TextAlign.Center, modifier = Modifier.fillMaxWidth()) },
                        modifier = Modifier.width(90.dp),
                        textStyle = androidx.compose.ui.text.TextStyle(textAlign = TextAlign.Center, color = PremiumTextPrimary, fontWeight = FontWeight.Bold),
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = PremiumSuccess,
                            unfocusedBorderColor = PremiumTextMuted
                        ),
                        singleLine = true
                    )
                }
            }
        }
    }
}
