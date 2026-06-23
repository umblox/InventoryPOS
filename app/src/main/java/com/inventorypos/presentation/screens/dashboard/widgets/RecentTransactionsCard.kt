package com.inventorypos.presentation.screens.dashboard.widgets

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ReceiptLong
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.inventorypos.presentation.components.common.GradientCard
import com.inventorypos.presentation.theme.*
import java.text.SimpleDateFormat
import java.util.*

data class RecentTransaction(
    val id: Long,
    val transactionNumber: String,
    val amount: Double,
    val time: Date,
    val status: String
)

@Composable
fun RecentTransactionsCard(
    transactions: List<RecentTransaction>,
    onViewAll: () -> Unit,
    onTransactionClick: (Long) -> Unit,
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
                    text = "Recent Transactions",
                    style = MaterialTheme.typography.titleMedium,
                    color = PremiumTextPrimary,
                    fontWeight = FontWeight.SemiBold
                )
                TextButton(onClick = onViewAll) {
                    Text("View All", color = PremiumGold)
                }
            }
            
            Spacer(modifier = Modifier.height(12.dp))
            
            if (transactions.isEmpty()) {
                Text(
                    text = "No transactions today",
                    style = MaterialTheme.typography.bodyMedium,
                    color = PremiumTextMuted
                )
            } else {
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    transactions.take(5).forEach { transaction ->
                        TransactionItem(
                            transaction = transaction,
                            onClick = { onTransactionClick(transaction.id) }
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun TransactionItem(
    transaction: RecentTransaction,
    onClick: () -> Unit
) {
    val timeFormat = SimpleDateFormat("HH:mm", Locale.getDefault())
    
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp))
            .background(PremiumDarkBackground.copy(alpha = 0.5f))
            .padding(12.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Row(
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(40.dp)
                    .clip(RoundedCornerShape(10.dp))
                    .background(PremiumGold.copy(alpha = 0.15f)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    Icons.Default.ReceiptLong,
                    contentDescription = null,
                    tint = PremiumGold,
                    modifier = Modifier.size(20.dp)
                )
            }
            Column {
                Text(
                    text = transaction.transactionNumber,
                    style = MaterialTheme.typography.bodyMedium,
                    color = PremiumTextPrimary,
                    fontWeight = FontWeight.Medium
                )
                Row(
                    horizontalArrangement = Arrangement.spacedBy(4.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        Icons.Default.Schedule,
                        contentDescription = null,
                        tint = PremiumTextMuted,
                        modifier = Modifier.size(12.dp)
                    )
                    Text(
                        text = timeFormat.format(transaction.time),
                        style = MaterialTheme.typography.labelSmall,
                        color = PremiumTextMuted
                    )
                }
            }
        }
        Text(
            text = "Rp ${String.format("%,.0f", transaction.amount)}",
            style = MaterialTheme.typography.labelLarge,
            color = PremiumGold,
            fontWeight = FontWeight.SemiBold
        )
    }
}
