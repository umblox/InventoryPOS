package com.inventorypos.presentation.components.charts

import androidx.compose.foundation.layout.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.inventorypos.presentation.theme.PremiumGold
import com.inventorypos.presentation.theme.PremiumTextMuted
import com.inventorypos.presentation.theme.PremiumTextPrimary

@Composable
fun SalesChart(
    data: List<Pair<String, Double>>,
    modifier: Modifier = Modifier
) {
    // Placeholder for chart implementation
    // You can integrate Vico or other chart library here
    Column(modifier = modifier.fillMaxWidth().height(200.dp)) {
        Text(
            text = "Sales Chart",
            style = MaterialTheme.typography.titleSmall,
            color = PremiumTextPrimary
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = "Chart data: ${data.size} points",
            style = MaterialTheme.typography.bodySmall,
            color = PremiumTextMuted
        )
    }
}
