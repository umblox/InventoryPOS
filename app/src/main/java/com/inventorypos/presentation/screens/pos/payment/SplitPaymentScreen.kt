package com.inventorypos.presentation.screens.pos.payment

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.inventorypos.presentation.components.common.*
import com.inventorypos.presentation.theme.*
import com.inventorypos.utils.CurrencyFormatter

@Composable
fun SplitPaymentScreen(
    navController: NavController,
    viewModel: SplitPaymentViewModel = hiltViewModel()
) {
    val totalAmount by viewModel.totalAmount.collectAsState()
    val cashAmount by viewModel.cashAmount.collectAsState()
    val cardAmount by viewModel.cardAmount.collectAsState()
    val remaining by viewModel.remaining.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()
    val isSuccess by viewModel.isSuccess.collectAsState()

    LaunchedEffect(isSuccess) { if (isSuccess) navController.popBackStack() }

    Scaffold(
        topBar = {
            CustomTopBar(
                title = "Split Payment",
                subtitle = "Multiple payment methods",
                onBackClick = { navController.popBackStack() }
            )
        },
        containerColor = PremiumDarkBackground
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = MaterialTheme.shapes.large,
                colors = CardDefaults.cardColors(containerColor = PremiumDarkSurface)
            ) {
                Column(
                    modifier = Modifier.padding(20.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Text(text = "Total Amount", color = PremiumTextSecondary)
                    Text(
                        text = CurrencyFormatter.format(totalAmount),
                        style = MaterialTheme.typography.headlineMedium,
                        color = PremiumGold,
                        fontWeight = androidx.compose.ui.text.font.FontWeight.Bold
                    )
                }
            }

            CustomTextField(
                value = cashAmount,
                onValueChange = viewModel::onCashAmountChange,
                label = "Cash Amount",
                placeholder = "0",
                leadingIcon = Icons.Default.Money,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
            )

            CustomTextField(
                value = cardAmount,
                onValueChange = viewModel::onCardAmountChange,
                label = "Card/QRIS Amount",
                placeholder = "0",
                leadingIcon = Icons.Default.CreditCard,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
            )

            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = MaterialTheme.shapes.large,
                colors = CardDefaults.cardColors(
                    containerColor = if (remaining <= 0) PremiumSuccess.copy(alpha = 0.1f) else PremiumWarning.copy(alpha = 0.1f)
                )
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(20.dp),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(text = "Remaining", color = PremiumTextPrimary)
                    Text(
                        text = CurrencyFormatter.format(remaining),
                        color = if (remaining <= 0) PremiumSuccess else PremiumWarning,
                        fontWeight = androidx.compose.ui.text.font.FontWeight.Bold
                    )
                }
            }

            Spacer(modifier = Modifier.weight(1f))

            CustomButton(
                text = "Complete Payment",
                onClick = viewModel::completePayment,
                isLoading = isLoading,
                enabled = remaining <= 0,
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}
