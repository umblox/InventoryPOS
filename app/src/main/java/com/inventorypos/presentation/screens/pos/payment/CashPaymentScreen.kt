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
fun CashPaymentScreen(
    navController: NavController,
    viewModel: CashPaymentViewModel = hiltViewModel()
) {
    val totalAmount by viewModel.totalAmount.collectAsState()
    val amountPaid by viewModel.amountPaid.collectAsState()
    val change by viewModel.change.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()
    val isSuccess by viewModel.isSuccess.collectAsState()

    LaunchedEffect(isSuccess) { if (isSuccess) navController.popBackStack() }

    Scaffold(
        topBar = {
            CustomTopBar(
                title = "Cash Payment",
                subtitle = "Pay with cash",
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
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(20.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Text(text = "Total Amount", style = MaterialTheme.typography.bodyMedium, color = PremiumTextSecondary)
                    Text(
                        text = CurrencyFormatter.format(totalAmount),
                        style = MaterialTheme.typography.headlineMedium,
                        color = PremiumGold,
                        fontWeight = androidx.compose.ui.text.font.FontWeight.Bold
                    )
                }
            }

            CustomTextField(
                value = amountPaid,
                onValueChange = viewModel::onAmountPaidChange,
                label = "Amount Paid *",
                placeholder = "Enter amount received",
                leadingIcon = Icons.Default.AttachMoney,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
            )

            if (change >= 0) {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = MaterialTheme.shapes.large,
                    colors = CardDefaults.cardColors(containerColor = PremiumSuccess.copy(alpha = 0.1f))
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(20.dp),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(text = "Change", style = MaterialTheme.typography.bodyLarge, color = PremiumTextPrimary)
                        Text(
                            text = CurrencyFormatter.format(change),
                            style = MaterialTheme.typography.titleLarge,
                            color = PremiumSuccess,
                            fontWeight = androidx.compose.ui.text.font.FontWeight.Bold
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.weight(1f))

            CustomButton(
                text = "Complete Payment",
                onClick = viewModel::completePayment,
                isLoading = isLoading,
                modifier = Modifier.fillMaxWidth(),
                icon = Icons.Default.CheckCircle
            )
        }
    }
}
