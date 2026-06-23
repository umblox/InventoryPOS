package com.inventorypos.presentation.screens.dashboard.widgets

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.inventorypos.presentation.components.common.GradientCard
import com.inventorypos.presentation.theme.*

@Composable
fun StockAlertCard(
    lowStockCount: Int,
    outOfStockCount: Int,
    modifier: Modifier = Modifier
) {
    GradientCard(
        modifier = modifier,
        gradient = androidx.compose.ui.graphics.Brush.verticalGradient(
            colors = listOf(PremiumError.copy(alpha = 0.15f), CardGradientEnd)
        )
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                Text(
                    text = "Stock Alerts",
                    style = MaterialTheme.typography.labelMedium,
                    color = PremiumTextSecondary
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "$lowStockCount items",
                    style = MaterialTheme.typography.headlineSmall,
                    color = PremiumError,
                    fontWeight = FontWeight.Bold
                )
                if (outOfStockCount > 0) {
                    Text(
                        text = "$outOfStockCount out of stock",
                        style = MaterialTheme.typography.bodySmall,
                        color = PremiumError.copy(alpha = 0.8f)
                    )
                }
            }
            
            Box(
                modifier = Modifier
                    .size(48.dp)
                    .clip(RoundedCornerShape(14.dp))
                    .background(PremiumError.copy(alpha = 0.2f)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    Icons.Default.Warning,
                    contentDescription = null,
                    tint = PremiumError,
                    modifier = Modifier.size(24.dp)
                )
            }
        }
    }
}
