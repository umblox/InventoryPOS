package com.inventorypos.presentation.screens.splash

import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Store
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.inventorypos.presentation.navigation.Screen
import com.inventorypos.presentation.theme.*
import kotlinx.coroutines.delay

@Composable
fun SplashScreen(
    navController: NavController,
    viewModel: SplashViewModel = hiltViewModel() // <--- INJEKSI VIEWMODEL
) {
    val isLoggedIn by viewModel.isLoggedIn.collectAsState() // <--- BACA STATUS LOGIN
    var startAnimation by remember { mutableStateOf(false) }
    
    val scale by animateFloatAsState(
        targetValue = if (startAnimation) 1f else 0.3f,
        animationSpec = tween(durationMillis = 1000, easing = EaseOutBack),
        label = "scale"
    )
    val alpha by animateFloatAsState(
        targetValue = if (startAnimation) 1f else 0f,
        animationSpec = tween(durationMillis = 1500),
        label = "alpha"
    )
    
    LaunchedEffect(isLoggedIn) {
        startAnimation = true
        delay(2500)
        
        navController.navigate(
            if (isLoggedIn) Screen.Dashboard.route else Screen.Login.route
        ) {
            popUpTo(Screen.Splash.route) { inclusive = true }
        }
    }
    
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(PremiumDarkBackground),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(24.dp),
            modifier = Modifier.alpha(alpha) // <--- PERBAIKAN WARNING ALPHA
        ) {
            // Logo
            Box(
                modifier = Modifier
                    .size(120.dp)
                    .clip(RoundedCornerShape(28.dp))
                    .background(
                        brush = androidx.compose.ui.graphics.Brush.linearGradient(
                            colors = listOf(PremiumGold.copy(alpha = 0.3f), PremiumGold.copy(alpha = 0.1f))
                        )
                    )
                    .scale(scale),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.Store,
                    contentDescription = "Logo",
                    tint = PremiumGold,
                    modifier = Modifier.size(56.dp)
                )
            }
            
            // App Name
            AnimatedVisibility(
                visible = startAnimation,
                enter = fadeIn(tween(1000, delayMillis = 500)) + slideInVertically(tween(1000, delayMillis = 500)) { it / 2 }
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text("Inventory", style = MaterialTheme.typography.displaySmall, color = PremiumTextPrimary, fontWeight = FontWeight.Bold)
                    Text("POS", style = MaterialTheme.typography.displaySmall, color = PremiumGold, fontWeight = FontWeight.Bold)
                }
            }
            
            // Tagline
            AnimatedVisibility(visible = startAnimation, enter = fadeIn(tween(800, delayMillis = 1200))) {
                Text("Premium Business Solution", style = MaterialTheme.typography.bodyMedium, color = PremiumTextMuted)
            }
            
            // Loading indicator
            AnimatedVisibility(visible = startAnimation, enter = fadeIn(tween(600, delayMillis = 1500))) {
                Spacer(modifier = Modifier.height(32.dp))
                androidx.compose.material3.CircularProgressIndicator(color = PremiumGold, strokeWidth = 2.dp, modifier = Modifier.size(32.dp))
            }
        }
    }
}
