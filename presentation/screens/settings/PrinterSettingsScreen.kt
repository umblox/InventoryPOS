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
fun PrinterSettingsScreen(
    navController: NavController,
    viewModel: PrinterSettingsViewModel = hiltViewModel()
) {
    val isBluetoothEnabled by viewModel.isBluetoothEnabled.collectAsState()
    val pairedDevices by viewModel.pairedDevices.collectAsState()
    val selectedDevice by viewModel.selectedDevice.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()

    Scaffold(
        topBar = {
            CustomTopBar(
                title = "Printer Settings",
                subtitle = "Bluetooth thermal printer",
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
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = androidx.compose.ui.Alignment.CenterVertically
            ) {
                Text(text = "Bluetooth", style = MaterialTheme.typography.bodyLarge, color = PremiumTextPrimary)
                Switch(
                    checked = isBluetoothEnabled,
                    onCheckedChange = { viewModel.toggleBluetooth() },
                    colors = SwitchDefaults.colors(checkedThumbColor = PremiumGold, checkedTrackColor = PremiumGold.copy(alpha = 0.5f))
                )
            }

            if (isBluetoothEnabled) {
                Text(text = "Paired Devices", style = MaterialTheme.typography.titleSmall, color = PremiumTextPrimary)
                if (isLoading) {
                    SmallLoadingIndicator()
                } else if (pairedDevices.isEmpty()) {
                    Text(text = "No paired devices", color = PremiumTextMuted)
                } else {
                    pairedDevices.forEach { device ->
                        Card(
                            onClick = { viewModel.selectDevice(device) },
                            modifier = Modifier.fillMaxWidth(),
                            shape = MaterialTheme.shapes.large,
                            colors = CardDefaults.cardColors(
                                containerColor = if (device == selectedDevice) PremiumGold.copy(alpha = 0.2f) else PremiumDarkSurface
                            )
                        ) {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(16.dp),
                                horizontalArrangement = Arrangement.spacedBy(12.dp),
                                verticalAlignment = androidx.compose.ui.Alignment.CenterVertically
                            ) {
                                Icon(Icons.Default.Print, null, tint = PremiumGold)
                                Text(text = device, style = MaterialTheme.typography.bodyLarge, color = PremiumTextPrimary, modifier = Modifier.weight(1f))
                                if (device == selectedDevice) {
                                    Icon(Icons.Default.CheckCircle, null, tint = PremiumSuccess)
                                }
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(8.dp))

                CustomButton(
                    text = "Test Print",
                    onClick = { viewModel.testPrint() },
                    modifier = Modifier.fillMaxWidth()
                )
            }
        }
    }
}
