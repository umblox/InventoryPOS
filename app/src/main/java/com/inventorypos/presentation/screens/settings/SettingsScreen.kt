package com.inventorypos.presentation.screens.settings

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.inventorypos.presentation.components.common.CustomTopBar
import com.inventorypos.presentation.navigation.Screen
import com.inventorypos.presentation.theme.*

@Composable
fun SettingsScreen(navController: NavController) {
    val settings = listOf(
        SettingItem("Store Profile", "Business name, address, tax", Icons.Default.Store, Screen.StoreProfile.route),
        SettingItem("Printer", "Bluetooth thermal printer", Icons.Default.Print, Screen.PrinterSettings.route),
        SettingItem("Backup & Restore", "Data backup options", Icons.Default.Backup, Screen.BackupRestore.route),
        SettingItem("About", "App version & info", Icons.Default.Info, Screen.About.route)
    )

    Scaffold(
        topBar = {
            CustomTopBar(
                title = "Settings",
                subtitle = "App configuration",
                showBackButton = false
            )
        },
        containerColor = PremiumDarkBackground
    ) { padding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            items(settings.size) { index ->
                val item = settings[index]
                Card(
                    onClick = { navController.navigate(item.route) },
                    modifier = Modifier.fillMaxWidth(),
                    shape = MaterialTheme.shapes.large,
                    colors = CardDefaults.cardColors(containerColor = PremiumDarkSurface)
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(20.dp),
                        horizontalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        Icon(item.icon, contentDescription = item.title, tint = PremiumGold)
                        Column(modifier = Modifier.weight(1f)) {
                            Text(text = item.title, style = MaterialTheme.typography.titleMedium, color = PremiumTextPrimary)
                            Text(text = item.description, style = MaterialTheme.typography.bodySmall, color = PremiumTextMuted)
                        }
                        Icon(Icons.Default.ChevronRight, null, tint = PremiumTextMuted)
                    }
                }
            }
        }
    }
}

data class SettingItem(val title: String, val description: String, val icon: androidx.compose.ui.graphics.vector.ImageVector, val route: String)
