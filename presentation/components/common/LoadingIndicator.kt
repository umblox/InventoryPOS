package com.inventorypos.presentation.components.common

import androidx.compose.foundation.layout.*
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.inventorypos.presentation.theme.PremiumGold
import com.inventorypos.presentation.theme.PremiumTextSecondary

@Composable
fun LoadingIndicator(
    message: String = "Loading...",
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            CircularProgressIndicator(
                color = PremiumGold,
                strokeWidth = 3.dp,
                modifier = Modifier.size(48.dp)
            )
            Text(
                text = message,
                style = MaterialTheme.typography.bodyMedium,
                color = PremiumTextSecondary
            )
        }
    }
}

@Composable
fun SmallLoadingIndicator(
    modifier: Modifier = Modifier
) {
    CircularProgressIndicator(
        color = PremiumGold,
        strokeWidth = 2.dp,
        modifier = modifier.size(24.dp)
    )
}
