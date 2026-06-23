package com.inventorypos.presentation.screens.auth

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.inventorypos.presentation.components.common.*
import com.inventorypos.presentation.navigation.Screen
import com.inventorypos.presentation.theme.*

@Composable
fun RegisterScreen(
    navController: NavController,
    viewModel: RegisterViewModel = hiltViewModel()
) {
    val fullName by viewModel.fullName.collectAsState()
    val username by viewModel.username.collectAsState()
    val email by viewModel.email.collectAsState()
    val password by viewModel.password.collectAsState()
    val confirmPassword by viewModel.confirmPassword.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()
    val isSuccess by viewModel.isSuccess.collectAsState()
    val error by viewModel.error.collectAsState()
    
    LaunchedEffect(isSuccess) {
        if (isSuccess) {
            navController.popBackStack()
        }
    }
    
    Scaffold(
        topBar = {
            CustomTopBar(
                title = "Create Account",
                subtitle = "Fill in your details",
                onBackClick = { navController.popBackStack() }
            )
        },
        containerColor = PremiumDarkBackground
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(padding)
                .padding(horizontal = 24.dp, vertical = 16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            CustomTextField(
                value = fullName,
                onValueChange = viewModel::onFullNameChange,
                label = "Full Name",
                placeholder = "Enter your full name",
                leadingIcon = Icons.Default.Person
            )
            
            CustomTextField(
                value = username,
                onValueChange = viewModel::onUsernameChange,
                label = "Username",
                placeholder = "Choose a username",
                leadingIcon = Icons.Default.AccountCircle
            )
            
            CustomTextField(
                value = email,
                onValueChange = viewModel::onEmailChange,
                label = "Email",
                placeholder = "Enter your email",
                leadingIcon = Icons.Default.Email,
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Email,
                    imeAction = ImeAction.Next
                )
            )
            
            CustomTextField(
                value = password,
                onValueChange = viewModel::onPasswordChange,
                label = "Password",
                placeholder = "Create a password",
                leadingIcon = Icons.Default.Lock,
                isPassword = true
            )
            
            CustomTextField(
                value = confirmPassword,
                onValueChange = viewModel::onConfirmPasswordChange,
                label = "Confirm Password",
                placeholder = "Confirm your password",
                leadingIcon = Icons.Default.Lock,
                isPassword = true
            )
            
            if (error != null) {
                Text(
                    text = error!!,
                    style = MaterialTheme.typography.bodySmall,
                    color = PremiumError
                )
            }
            
            Spacer(modifier = Modifier.height(8.dp))
            
            CustomButton(
                text = "Create Account",
                onClick = viewModel::register,
                isLoading = isLoading,
                modifier = Modifier.fillMaxWidth()
            )
            
            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}
