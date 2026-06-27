package com.inventorypos.presentation.screens.users

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
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
fun UserDetailScreen(
    navController: NavController,
    userId: Long,
    viewModel: UserDetailViewModel = hiltViewModel()
) {
    LaunchedEffect(userId) { viewModel.loadUser(userId) }

    val user by viewModel.user.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()

    Scaffold(
        topBar = {
            CustomTopBar(
                title = "User Detail",
                subtitle = "ID: #$userId",
                onBackClick = { navController.popBackStack() },
                actions = {
                    IconButton(onClick = { navController.navigate(Screen.UserEdit.createRoute(userId)) }) {
                        Icon(Icons.Default.Edit, "Edit", tint = PremiumGold)
                    }
                }
            )
        },
        containerColor = PremiumDarkBackground
    ) { padding ->
        if (isLoading) {
            LoadingIndicator()
        } else if (user == null) {
            ErrorState(message = "User not found")
        } else {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState())
                    .padding(padding)
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                DetailCard(Icons.Default.Person, "Full Name", user!!.fullName)
                DetailCard(Icons.Default.AccountCircle, "Username", user!!.username)
                DetailCard(Icons.Default.Security, "Role", user!!.role)
                if (!user!!.email.isNullOrBlank()) DetailCard(Icons.Default.Email, "Email", user!!.email!!)
                if (!user!!.phone.isNullOrBlank()) DetailCard(Icons.Default.Phone, "Phone", user!!.phone!!)
            }
        }
        @Composable
fun DetailCard(title: String, value: String) {
    androidx.compose.material3.Card(
        modifier = androidx.compose.ui.Modifier.fillMaxWidth().padding(vertical = 4.dp),
        colors = androidx.compose.material3.CardDefaults.cardColors(
            containerColor = androidx.compose.material3.MaterialTheme.colorScheme.surfaceVariant
        )
    ) {
        androidx.compose.foundation.layout.Column(modifier = androidx.compose.ui.Modifier.padding(16.dp)) {
            androidx.compose.material3.Text(text = title, style = androidx.compose.material3.MaterialTheme.typography.labelMedium)
            androidx.compose.material3.Text(text = value, style = androidx.compose.material3.MaterialTheme.typography.bodyLarge)
        }
    }
    }
  }
}
