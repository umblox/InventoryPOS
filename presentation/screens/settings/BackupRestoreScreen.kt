package com.inventorypos.presentation.screens.settings

import androidx.compose.foundation.layout.*
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
fun BackupRestoreScreen(
    navController: NavController,
    viewModel: BackupRestoreViewModel = hiltViewModel()
) {
    val isBackingUp by viewModel.isBackingUp.collectAsState()
    val isRestoring by viewModel.isRestoring.collectAsState()
    val lastBackup by viewModel.lastBackup.collectAsState()

    Scaffold(
        topBar = {
            CustomTopBar(
                title = "Backup & Restore",
                subtitle = "Data management",
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
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = MaterialTheme.shapes.large,
                colors = CardDefaults.cardColors(containerColor = PremiumDarkSurface)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(20.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Text(text = "Backup", style = MaterialTheme.typography.titleMedium, color = PremiumTextPrimary)
                    Text(text = "Export all data to file", style = MaterialTheme.typography.bodySmall, color = PremiumTextMuted)
                    if (lastBackup != null) {
                        Text(text = "Last backup: $lastBackup", style = MaterialTheme.typography.labelSmall, color = PremiumTextMuted)
                    }
                    CustomButton(
                        text = "Create Backup",
                        onClick = { viewModel.backup() },
                        isLoading = isBackingUp,
                        modifier = Modifier.fillMaxWidth(),
                        icon = Icons.Default.Backup
                    )
                }
            }

            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = MaterialTheme.shapes.large,
                colors = CardDefaults.cardColors(containerColor = PremiumDarkSurface)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(20.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Text(text = "Restore", style = MaterialTheme.typography.titleMedium, color = PremiumTextPrimary)
                    Text(text = "Import data from backup file", style = MaterialTheme.typography.bodySmall, color = PremiumTextMuted)
                    CustomButton(
                        text = "Restore from File",
                        onClick = { viewModel.restore() },
                        isLoading = isRestoring,
                        modifier = Modifier.fillMaxWidth(),
                        icon = Icons.Default.Restore
                    )
                }
            }
        }
    }
}
