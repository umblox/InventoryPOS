package com.inventorypos.presentation.screens.dashboard.widgets

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
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

data class TopProduct(
    val name: String,
    val sales: Int,
    val revenue: Double
)

@Composable
fun TopProductsCard(
    products: List<TopProduct> = emptyList(),
    modifier: Modifier = Modifier
) {
    GradientCard(modifier = modifier) {
        Column {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Top Products",
                    style = MaterialTheme.typography.titleMedium,
                    color = PremiumTextPrimary,
                    fontWeight = FontWeight.SemiBold
                )
                Box(
                    modifier = Modifier
                        .size(32.dp)
                        .clip(RoundedCornerShape(8.dp))
                        .background(PremiumGold.copy(alpha = 0.15f)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        Icons.Default.Star,
                        contentDescription = null,
                        tint = PremiumGold,
                        modifier = Modifier.size(16.dp)
                    )
                }
            }
            
            Spacer(modifier = Modifier.height(16.dp))
            
            if (products.isEmpty()) {
                Text(
                    text = "No sales data yet",
                    style = MaterialTheme.typography.bodyMedium,
                    color = PremiumTextMuted
                )
            } else {
                val maxSales = products.maxOfOrNull { it.sales } ?: 1
                
                Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    products.take(5).forEach { product ->
                        TopProductItem(
                            product = product,
                            maxSales = maxSales
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun TopProductItem(product: TopProduct, maxSales: Int) {
    Column {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = product.name,
                style = MaterialTheme.typography.bodyMedium,
                color = PremiumTextPrimary,
                modifier = Modifier.weight(1f)
            )
            Text(
                text = "${product.sales} sold",
                style = MaterialTheme.typography.labelSmall,
                color = PremiumTextMuted
            )
        }
        Spacer(modifier = Modifier.height(4.dp))
        LinearProgressIndicator(
            progress = product.sales.toFloat() / maxSales,
            modifier = Modifier
                .fillMaxWidth()
                .height(6.dp)
                .clip(RoundedCornerShape(3.dp)),
            color = PremiumGold,
            trackColor = PremiumDarkSurfaceVariant
        )
    }
}
