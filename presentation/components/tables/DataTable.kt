package com.inventorypos.presentation.components.tables

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.inventorypos.presentation.theme.*

@Composable
fun <T> DataTable(
    columns: List<String>,
    items: List<T>,
    modifier: Modifier = Modifier,
    rowContent: @Composable (T) -> Unit
) {
    Column(modifier = modifier) {
        // Header
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(topStart = 12.dp, topEnd = 12.dp))
                .background(PremiumDarkSurfaceVariant)
                .padding(horizontal = 16.dp, vertical = 12.dp)
        ) {
            columns.forEach { column ->
                Text(
                    text = column,
                    style = MaterialTheme.typography.labelMedium,
                    color = PremiumTextSecondary,
                    fontWeight = FontWeight.SemiBold,
                    modifier = Modifier.weight(1f)
                )
            }
        }
        // Rows
        LazyColumn {
            items(items) { item ->
                rowContent(item)
                Divider(color = PremiumDarkBackground, thickness = 1.dp)
            }
        }
    }
}
