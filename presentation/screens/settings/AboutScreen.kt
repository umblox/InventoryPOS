package com.inventorypos.presentation.screens.settings

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.inventorypos.presentation.components.common.CustomTopBar
import com.inventorypos.presentation.theme.*

@Composable
fun AboutScreen(navController: NavController) {
    Scaffold(
        topBar = {
            CustomTopBar(
                title = "About",
                subtitle = "App information",
                onBackClick = { navController.popBackStack() }
            )
        },
        containerColor = PremiumDarkBackground
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Icon(
                Icons.Default.Store,
                contentDescription = null,
                tint = PremiumGold,
                modifier = Modifier.size(80.dp)
            )
            Text(
                text = "Inventory POS",
                style = MaterialTheme.typography.headlineMedium,
                color = PremiumTextPrimary
            )
            Text(
                text = "Premium v1.0.0",
                style = MaterialTheme.typography.bodyLarge,
                color = PremiumGold
            )
            Text(
                text = "A complete inventory and point of sale solution for your business.",
                style = MaterialTheme.typography.bodyMedium,
                color = PremiumTextSecondary,
                textAlign = androidx.compose.ui.text.style.TextAlign.Center
            )
            Divider(color = PremiumDarkSurfaceVariant, modifier = Modifier.padding(vertical = 16.dp))
            Text(
                text = "Developed with ❤",
                style = MaterialTheme.typography.bodySmall,
                color = PremiumTextMuted
            )
        }
    }
}
