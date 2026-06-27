package com.inventorypos.presentation.screens.profile

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
import com.inventorypos.presentation.navigation.Screen
import com.inventorypos.presentation.theme.*

@Composable
fun DetailCard(
    icon: androidx.compose.ui.graphics.vector.ImageVector? = null,
    title: String,
    value: String
) {
    androidx.compose.material3.Card(
        modifier = androidx.compose.ui.Modifier.fillMaxWidth().padding(vertical = 4.dp),
        colors = androidx.compose.material3.CardDefaults.cardColors(
            containerColor = androidx.compose.material3.MaterialTheme.colorScheme.surfaceVariant
        )
    ) {
        androidx.compose.foundation.layout.Row(
            modifier = androidx.compose.ui.Modifier.padding(16.dp),
            verticalAlignment = androidx.compose.ui.Alignment.CenterVertically
        ) {
            if (icon != null) {
                androidx.compose.material3.Icon(
                    imageVector = icon,
                    contentDescription = null,
                    tint = com.inventorypos.presentation.theme.PremiumGold,
                    modifier = androidx.compose.ui.Modifier.size(24.dp)
                )
                androidx.compose.foundation.layout.Spacer(modifier = androidx.compose.ui.Modifier.width(16.dp))
            }
            androidx.compose.foundation.layout.Column {
                androidx.compose.material3.Text(text = title, style = androidx.compose.material3.MaterialTheme.typography.labelMedium)
                androidx.compose.material3.Text(text = value, style = androidx.compose.material3.MaterialTheme.typography.bodyLarge)
            }
        }
    }
}

@Composable
fun ProfileScreen(
    navController: NavController,
    viewModel: ProfileViewModel = hiltViewModel()
) {
    // Memasang user dummy (Administrator / Pemilik) secara langsung
    // Password "123456" sudah di-encode ke bentuk MD5
    val dummyUser = com.inventorypos.data.local.entity.UserEntity(
        id = 1L,
        username = "administrator",
        passwordHash = "e10adc3949ba59abbe56e057f20f883e", 
        fullName = "Administrator (Owner)",
        role = com.inventorypos.data.local.entity.UserRole.SUPER_ADMIN,
        isActive = true,
        createdAt = java.util.Date()
    )

    Scaffold(
        topBar = {
            CustomTopBar(
                title = "My Profile",
                subtitle = "Account settings",
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
                    DetailCard(Icons.Default.Person, "Name", dummyUser.fullName)
                    DetailCard(Icons.Default.AccountCircle, "Username", dummyUser.username)
                    DetailCard(Icons.Default.Security, "Role", dummyUser.role.name) // .name untuk Enum
                }
            }

            CustomButton(
                text = "Change Password",
                onClick = { navController.navigate(Screen.ChangePassword.route) },
                modifier = Modifier.fillMaxWidth(),
                icon = Icons.Default.Lock
            )

            // Asumsi Anda punya komponen CustomOutlinedButton, jika error, kita bisa ganti dengan OutlinedButton bawaan
            CustomOutlinedButton(
                text = "Logout",
                onClick = { viewModel.logout() },
                modifier = Modifier.fillMaxWidth(),
                icon = Icons.Default.Logout
            )
        }
    }
}
