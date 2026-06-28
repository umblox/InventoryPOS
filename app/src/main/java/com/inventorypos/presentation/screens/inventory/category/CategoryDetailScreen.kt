package com.inventorypos.presentation.screens.inventory.category

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Category
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.inventorypos.presentation.components.common.CustomTopBar
import com.inventorypos.presentation.theme.*

@Composable
fun CategoryDetailScreen(
    navController: NavController,
    categoryId: Long,
    viewModel: CategoryDetailViewModel = hiltViewModel()
) {
    val category by viewModel.category.collectAsState()

    Scaffold(
        topBar = {
            CustomTopBar(title = "Category Detail", onBackClick = { navController.popBackStack() })
        },
        containerColor = PremiumDarkBackground
    ) { padding ->
        category?.let { cat ->
            Column(modifier = Modifier.padding(padding).padding(16.dp)) {
                Card(colors = CardDefaults.cardColors(containerColor = PremiumDarkSurface)) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text("Name", style = MaterialTheme.typography.labelMedium, color = PremiumTextSecondary)
                        Text(cat.name, style = MaterialTheme.typography.headlineSmall, color = PremiumTextPrimary)
                        
                        Spacer(modifier = Modifier.height(16.dp))
                        
                        Text("Description", style = MaterialTheme.typography.labelMedium, color = PremiumTextSecondary)
                        Text(cat.description ?: "-", style = MaterialTheme.typography.bodyMedium, color = PremiumTextPrimary)
                    }
                }
            }
        } ?: Box(modifier = Modifier.fillMaxSize(), contentAlignment = androidx.compose.ui.Alignment.Center) {
            CircularProgressIndicator(color = PremiumGold)
        }
    }
}
