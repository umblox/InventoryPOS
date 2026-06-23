package com.inventorypos.presentation.screens.pos.payment

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.inventorypos.presentation.components.common.*
import com.inventorypos.presentation.theme.*
import com.inventorypos.utils.CurrencyFormatter

@Composable
fun QrisPaymentScreen(
    navController: NavController,
    viewModel: QrisPaymentViewModel = hiltViewModel()
) {
    val totalAmount by viewModel.totalAmount.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()
    val isSuccess by viewModel.isSuccess.collectAsState()

    LaunchedEffect(isSuccess) { if (isSuccess) navController.popBackStack() }

    Scaffold(
        topBar = {
            CustomTopBar(
                title = "QRIS Payment",
                subtitle = "Scan QR code",
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
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Text(
                text = "Total: ${CurrencyFormatter.format(totalAmount)}",
                style = MaterialTheme.typography.headlineSmall,
                color = PremiumGold
            )

            Box(
                modifier = Modifier
                    .size(250.dp)
                    .padding(16.dp),
                contentAlignment = Alignment.Center
            ) {
                if (isLoading) {
                    SmallLoadingIndicator()
                } else {
                    Icon(
                        Icons.Default.QrCode,
                        contentDescription = "QR Code",
                        tint = PremiumTextPrimary,
                        modifier = Modifier.fillMaxSize()
                    )
                }
            }

            Text(
                text = "Show this QR to customer",
                style = MaterialTheme.typography.bodyMedium,
                color = PremiumTextSecondary
            )

            Spacer(modifier = Modifier.weight(1f))

            CustomButton(
                text = "Payment Received",
                onClick = viewModel::confirmPayment,
                modifier = Modifier.fillMaxWidth(),
                icon = Icons.Default.CheckCircle
            )
        }
    }
}
