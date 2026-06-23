package com.inventorypos.presentation.components.common

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ErrorOutline
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.inventorypos.presentation.theme.PremiumError
import com.inventorypos.presentation.theme.PremiumTextMuted
import com.inventorypos.presentation.theme.PremiumTextSecondary

@Composable
fun ErrorState(
    message: String = "Something went wrong",
    onRetry: (() -> Unit)? = null,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(32.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Icon(
            imageVector = Icons.Default.ErrorOutline,
            contentDescription = null,
            tint = PremiumError.copy(alpha = 0.7f),
            modifier = Modifier.size(64.dp)
        )
        Spacer(modifier = Modifier.height(16.dp))
        Text(
            text = "Oops!",
            style = MaterialTheme.typography.titleMedium,
            color = PremiumTextSecondary
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = message,
            style = MaterialTheme.typography.bodyMedium,
            color = PremiumTextMuted,
            textAlign = TextAlign.Center
        )
        if (onRetry != null) {
            Spacer(modifier = Modifier.height(16.dp))
            IconButton(onClick = onRetry) {
                Icon(
                    Icons.Default.Refresh,
                    contentDescription = "Retry",
                    tint = PremiumError
                )
            }
        }
    }
}
