package com.inventorypos.presentation.theme

import android.app.Activity
import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat
import androidx.compose.ui.graphics.Color

private val PremiumDarkColorScheme = darkColorScheme(
    primary = PremiumGold,
    onPrimary = Color(0xFF0A0E1A),
    primaryContainer = PremiumGoldDark,
    onPrimaryContainer = PremiumGoldLight,
    
    secondary = PremiumAccent,
    onSecondary = Color(0xFF0A0E1A),
    secondaryContainer = Color(0xFF003D2E),
    onSecondaryContainer = PremiumAccentLight,
    
    tertiary = PremiumInfo,
    onTertiary = Color(0xFF0A0E1A),
    
    background = PremiumDarkBackground,
    onBackground = PremiumTextPrimary,
    
    surface = PremiumDarkSurface,
    onSurface = PremiumTextPrimary,
    surfaceVariant = PremiumDarkSurfaceVariant,
    onSurfaceVariant = PremiumTextSecondary,
    
    error = PremiumError,
    onError = Color.White,
    
    outline = Color(0xFF2A3050),
    outlineVariant = Color(0xFF1E2338),
    
    scrim = Color(0xFF000000).copy(alpha = 0.7f)
)

@Composable
fun InventoryPOSTheme(
    darkTheme: Boolean = true, // Default dark for premium feel
    dynamicColor: Boolean = false, // Disable dynamic for consistent branding
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val context = LocalContext.current
            if (darkTheme) dynamicDarkColorScheme(context) 
            else dynamicLightColorScheme(context)
        }
        darkTheme -> PremiumDarkColorScheme
        else -> PremiumDarkColorScheme // Always dark for premium
    }
    
    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as Activity).window
            window.statusBarColor = colorScheme.background.toArgb()
            window.navigationBarColor = colorScheme.background.toArgb()
            WindowCompat.getInsetsController(window, view).isAppearanceLightStatusBars = false
            WindowCompat.getInsetsController(window, view).isAppearanceLightNavigationBars = false
        }
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = PremiumTypography,
        shapes = PremiumShapes,
        content = content
    )
}

