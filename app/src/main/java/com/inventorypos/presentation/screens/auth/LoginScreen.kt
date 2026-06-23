package com.inventorypos.presentation.screens.auth

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.inventorypos.presentation.components.common.*
import com.inventorypos.presentation.navigation.Screen
import com.inventorypos.presentation.theme.*

@Composable
fun LoginScreen(
    navController: NavController,
    viewModel: LoginViewModel = hiltViewModel()
) {
    val username by viewModel.username.collectAsState()
    val password by viewModel.password.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()
    val isSuccess by viewModel.isSuccess.collectAsState()
    val error by viewModel.error.collectAsState()
    val isBiometricAvailable by viewModel.isBiometricAvailable.collectAsState()
    
    val keyboardController = LocalSoftwareKeyboardController.current
    
    LaunchedEffect(isSuccess) {
        if (isSuccess) {
            navController.navigate(Screen.Dashboard.route) {
                popUpTo(Screen.Login.route) { inclusive = true }
            }
        }
    }
    
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(PremiumDarkBackground)
    ) {
        // Background decoration
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(300.dp)
                .clip(RoundedCornerShape(bottomStart = 40.dp, bottomEnd = 40.dp))
                .background(
                    brush = androidx.compose.ui.graphics.Brush.verticalGradient(
                        colors = listOf(
                            PremiumGold.copy(alpha = 0.15f),
                            PremiumDarkBackground
                        )
                    )
                )
        )
        
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(80.dp))
            
            // Logo
            Box(
                modifier = Modifier
                    .size(90.dp)
                    .clip(RoundedCornerShape(24.dp))
                    .background(PremiumGold.copy(alpha = 0.2f)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.Store,
                    contentDescription = "Logo",
                    tint = PremiumGold,
                    modifier = Modifier.size(44.dp)
                )
            }
            
            Spacer(modifier = Modifier.height(24.dp))
            
            Text(
                text = "Welcome Back",
                style = MaterialTheme.typography.headlineMedium,
                color = PremiumTextPrimary,
                fontWeight = FontWeight.Bold
            )
            
            Text(
                text = "Sign in to manage your business",
                style = MaterialTheme.typography.bodyMedium,
                color = PremiumTextMuted
            )
            
            Spacer(modifier = Modifier.height(40.dp))
            
            // Login Form
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(24.dp),
                colors = CardDefaults.cardColors(containerColor = PremiumDarkSurface),
                elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
            ) {
                Column(
                    modifier = Modifier.padding(24.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    CustomTextField(
                        value = username,
                        onValueChange = viewModel::onUsernameChange,
                        label = "Username",
                        placeholder = "Enter your username",
                        leadingIcon = Icons.Default.Person,
                        keyboardOptions = KeyboardOptions(
                            imeAction = ImeAction.Next
                        )
                    )
                    
                    CustomTextField(
                        value = password,
                        onValueChange = viewModel::onPasswordChange,
                        label = "Password",
                        placeholder = "Enter your password",
                        leadingIcon = Icons.Default.Lock,
                        isPassword = true,
                        keyboardOptions = KeyboardOptions(
                            keyboardType = KeyboardType.Password,
                            imeAction = ImeAction.Done
                        ),
                        keyboardActions = KeyboardActions(
                            onDone = {
                                keyboardController?.hide()
                                viewModel.login()
                            }
                        )
                    )
                    
                    // Error message
                    AnimatedVisibility(visible = error != null) {
                        Text(
                            text = error ?: "",
                            style = MaterialTheme.typography.bodySmall,
                            color = PremiumError,
                            textAlign = TextAlign.Center,
                            modifier = Modifier.fillMaxWidth()
                        )
                    }
                    
                    // Login Button
                    CustomButton(
                        text = "Sign In",
                        onClick = {
                            keyboardController?.hide()
                            viewModel.login()
                        },
                        isLoading = isLoading,
                        modifier = Modifier.fillMaxWidth()
                    )
                    
                    // Biometric Login
                    if (isBiometricAvailable) {
                        TextButton(
                            onClick = { viewModel.loginWithBiometric() },
                            modifier = Modifier.fillMaxWidth(),
                            colors = ButtonDefaults.textButtonColors(
                                contentColor = PremiumGold
                            )
                        ) {
                            Row(
                                horizontalArrangement = Arrangement.spacedBy(8.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Icon(
                                    Icons.Default.Fingerprint,
                                    contentDescription = null,
                                    modifier = Modifier.size(20.dp)
                                )
                                Text("Use Fingerprint")
                            }
                        }
                    }
                }
            }
            
            Spacer(modifier = Modifier.height(24.dp))
            
            // Register link
            Row(
                horizontalArrangement = Arrangement.spacedBy(4.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Don't have an account?",
                    style = MaterialTheme.typography.bodyMedium,
                    color = PremiumTextMuted
                )
                TextButton(
                    onClick = { navController.navigate(Screen.Register.route) },
                    colors = ButtonDefaults.textButtonColors(
                        contentColor = PremiumGold
                    )
                ) {
                    Text("Sign Up")
                }
            }
            
            Spacer(modifier = Modifier.height(32.dp))
        }
    }
}
