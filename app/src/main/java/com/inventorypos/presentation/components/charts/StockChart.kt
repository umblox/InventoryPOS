package com.inventorypos.presentation.components.charts

import androidx.compose.foundation.layout.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.inventorypos.presentation.theme.PremiumTextMuted
import com.inventorypos.presentation.theme.PremiumTextPrimary

@Composable
fun StockChart(
    data: List<Pair<String, Int>>,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier.fillMaxWidth().height(200.dp)) {
        Text(
            text = "Stock Chart",
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
