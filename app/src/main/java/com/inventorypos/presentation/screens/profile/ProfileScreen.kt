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
    Card(
        modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp),
        colors = CardDefaults.cardColors(containerColor = PremiumDarkSurface)
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = androidx.compose.ui.Alignment.CenterVertically
        ) {
            if (icon != null) {
                Icon(imageVector = icon, contentDescription = null, tint = PremiumGold, modifier = Modifier.size(24.dp))
                Spacer(modifier = Modifier.width(16.dp))
            }
            Column {
                Text(text = title, style = MaterialTheme.typography.labelMedium, color = PremiumTextSecondary)
                Text(text = value, style = MaterialTheme.typography.bodyLarge, color = PremiumTextPrimary)
            }
        }
    }
}

@Composable
fun ProfileScreen(
    navController: NavController,
    viewModel: ProfileViewModel = hiltViewModel()
) {
    // Tarik data asli dari ViewModel (BUKAN DUMMY LAGI)
    val user by viewModel.user.collectAsState()

    Scaffold(
        topBar = {
            CustomTopBar(title = "My Profile", subtitle = "Account settings", onBackClick = { navController.popBackStack() })
        },
        containerColor = PremiumDarkBackground
    ) { padding ->
        Column(
            modifier = Modifier.fillMaxSize().padding(padding).padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            if (user != null) {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = MaterialTheme.shapes.large,
                    colors = CardDefaults.cardColors(containerColor = PremiumDarkSurface)
                ) {
                    Column(
                        modifier = Modifier.fillMaxWidth().padding(20.dp),
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        DetailCard(Icons.Default.Person, "Name", user!!.fullName)
                        DetailCard(Icons.Default.AccountCircle, "Username", user!!.username)
                        DetailCard(Icons.Default.Security, "Role", user!!.role.name)
                    }
                }
            } else {
                CircularProgressIndicator(color = PremiumGold) // Loading jika data belum masuk
            }

            CustomButton(
                text = "Change Password",
                onClick = { navController.navigate(Screen.ChangePassword.route) },
                modifier = Modifier.fillMaxWidth(),
                icon = Icons.Default.Lock
            )

            CustomOutlinedButton(
                text = "Logout",
                onClick = { 
                    viewModel.logout() 
                    // PERBAIKAN LOGOUT: Navigasi ke Login dan hapus semua history layar sebelumnya
                    navController.navigate(Screen.Login.route) {
                        popUpTo(0) { inclusive = true }
                    }
                },
                modifier = Modifier.fillMaxWidth(),
                icon = Icons.Default.Logout
            )
        }
    }
}
