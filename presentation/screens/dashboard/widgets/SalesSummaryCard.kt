package com.inventorypos.presentation.screens.dashboard.widgets

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.TrendingUp
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.inventorypos.presentation.components.common.GradientCard
import com.inventorypos.presentation.theme.*

@Composable
fun SalesSummaryCard(
    todaySales: Double,
    yesterdaySales: Double,
    modifier: Modifier = Modifier
) {
    val percentChange = if (yesterdaySales > 0) {
        ((todaySales - yesterdaySales) / yesterdaySales * 100).toInt()
    } else 0
    
    GradientCard(modifier = modifier) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                Text(
                    text = "Today's Sales",
                    style = MaterialTheme.typography.labelMedium,
                    color = PremiumTextSecondary
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "Rp ${String.format("%,.0f", todaySales)}",
                    style = MaterialTheme.typography.headlineSmall,
                    color = PremiumTextPrimary,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(4.dp))
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(6.dp))
                            .background(
                                if (percentChange >= 0) PremiumSuccess.copy(alpha = 0.2f)
                                else PremiumError.copy(alpha = 0.2f)
                            )
                            .padding(horizontal = 8.dp, vertical = 2.dp)
                    ) {
                        Text(
                            text = "${if (percentChange >= 0) "+" else ""}$percentChange%",
                            style = MaterialTheme.typography.labelSmall,
                            color = if (percentChange >= 0) PremiumSuccess else PremiumError
                        )
                    }
                    Text(
                        text = "vs yesterday",
                        style = MaterialTheme.typography.bodySmall,
                        color = PremiumTextMuted
                    )
                }
            }
            
            Box(
                modifier = Modifier
                    .size(48.dp)
                    .clip(RoundedCornerShape(14.dp))
                    .background(PremiumGold.copy(alpha = 0.15f)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    Icons.Default.TrendingUp,
                    contentDescription = null,
                    tint = PremiumGold,
                    modifier = Modifier.size(24.dp)
                )
            }
        }
    }
}
