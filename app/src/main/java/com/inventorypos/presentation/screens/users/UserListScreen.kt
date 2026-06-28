package com.inventorypos.presentation.screens.users

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
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
fun UserListScreen(
    navController: NavController,
    viewModel: UserListViewModel = hiltViewModel()
) {
    val users by viewModel.users.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()

    Scaffold(
        topBar = {
            CustomTopBar(
                title = "Users",
                subtitle = "${users.size} users",
                onBackClick = { navController.popBackStack() }
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = { navController.navigate(Screen.UserAdd.route) },
                containerColor = PremiumGold,
                contentColor = PremiumDarkBackground
            ) {
                Icon(Icons.Default.Add, "Add User")
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
            } else if (users.isEmpty()) {
                EmptyState(
                    icon = Icons.Default.People,
                    title = "No Users",
                    message = "Add your first user"
                )
            } else {
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(16.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    items(users) { user ->
                        UserCard(
                            user = user,
                            onClick = { navController.navigate(Screen.UserDetail.createRoute(user.id)) },
                            onEdit = { navController.navigate(Screen.UserEdit.createRoute(user.id)) },
                            onPermission = { navController.navigate(Screen.UserPermission.createRoute(user.id)) }
                        )
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UserCard(
    user: com.inventorypos.data.local.entity.UserEntity, // PERUBAHAN PENTING: Gunakan UserEntity
    onClick: () -> Unit,
    onEdit: () -> Unit,
    onPermission: () -> Unit
) {
    Card(
        onClick = onClick,
        modifier = Modifier.fillMaxWidth(),
        shape = MaterialTheme.shapes.large,
        colors = CardDefaults.cardColors(containerColor = PremiumDarkSurface)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = androidx.compose.ui.Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = user.fullName,
                    style = MaterialTheme.typography.titleMedium,
                    color = PremiumTextPrimary
                )
                Text(
                    text = "@${user.username} • ${user.role.name}", // .name untuk enum
                    style = MaterialTheme.typography.bodySmall,
                    color = PremiumTextMuted
                )
            }
            Row {
                IconButton(onClick = onPermission) {
                    Icon(Icons.Default.Security, "Permissions", tint = PremiumWarning)
                }
                IconButton(onClick = onEdit) {
                    Icon(Icons.Default.Edit, "Edit", tint = PremiumInfo)
                }
            }
        }
    }
}
