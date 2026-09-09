package com.mathi.finance.features.transactions.presentation

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Notes
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.mathi.finance.core.util.DateUtils
import com.mathi.finance.features.transactions.domain.model.TransactionSummary
import com.mathi.finance.ui.presentation.AppBar
import com.mathi.finance.ui.presentation.EmptyState
import org.koin.androidx.compose.koinViewModel
import java.util.Locale

@Composable
fun TransactionDetailScreen(
    transaction: TransactionSummary,
    viewModel: TransactionViewModel = koinViewModel(),
    onBack: () -> Unit
) {
    var showLedger by remember { mutableStateOf(false) }

    if (showLedger) {
        BorrowerLedgerScreen(
            transaction = transaction,
            onBack = { showLedger = false }
        )
        return
    }

    val contactName = transaction.name
    val interestRate = transaction.interest_rate
    val instalmentTenure = transaction.tenure
    var amountCollected by remember { mutableStateOf("") }
    var note by remember { mutableStateOf("") }
    var isManualPaymentEnabled by remember { mutableStateOf(false) }
    var showForecloseDialog by remember { mutableStateOf(false) }
    val uiState by viewModel.uiState.collectAsState()

    val terms = uiState.paymentHistory.size

    val currentPaid = remember(transaction.amount_paid, uiState.paymentHistory) {
        if (uiState.paymentHistory.isNotEmpty()) {
            uiState.paymentHistory.sumOf { it.amount_paid.toDouble() }.toFloat()
        } else {
            transaction.amount_paid
        }
    }
    val remainingBalance = if (interestRate != null && interestRate > 0) {
        transaction.amount
    }else{
        transaction.amount - currentPaid
    }

    val amountToBePaid = if (interestRate != null && interestRate > 0) {
        transaction.amount * (interestRate.toFloat() / 100f)
    } else if (instalmentTenure != null && instalmentTenure > 0) {
        transaction.amount / instalmentTenure
    } else {
        transaction.amount
    }

    LaunchedEffect(transaction.id) {
        viewModel.fetchPaymentHistory(transaction.id)
    }

    Scaffold(
        topBar = {
            AppBar(
                title = "Transaction Details", onBack = onBack
            )
        }, containerColor = Color.Transparent
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Amount Highlight Card
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(24.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.primaryContainer
                    ),
                    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(24.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = "Total Principal",
                            style = MaterialTheme.typography.labelLarge,
                            color = MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.7f)
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = "₹${transaction.amount}",
                            style = MaterialTheme.typography.headlineLarge,
                            fontWeight = FontWeight.Black,
                            color = MaterialTheme.colorScheme.onPrimaryContainer
                        )
                    }
                }
            }

            // Info Details Card
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(20.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.surface
                    ),
                    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(20.dp),
                        verticalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(
                                text = contactName,
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.onSurface
                            )
                            Text(
                                text = if (interestRate != null && interestRate > 0) {
                                    "Interest: $interestRate%"
                                } else if (instalmentTenure != null && instalmentTenure > 0) {
                                    "Tenure: $instalmentTenure weeks"
                                } else {
                                    "One-time"
                                },
                                style = MaterialTheme.typography.bodyMedium,
                                color = MaterialTheme.colorScheme.primary
                            )
                        }
                        HorizontalDivider(
                            color = MaterialTheme.colorScheme.outlineVariant.copy(
                                alpha = 0.5f
                            )
                        )
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(
                                text = "Start: ${DateUtils.formatSqlDate(transaction.created_at)}",
                                style = MaterialTheme.typography.bodyMedium,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                            Text(
                                text = "$terms Payments done",
                                style = MaterialTheme.typography.bodyMedium,
                                fontWeight = FontWeight.Medium,
                                color = MaterialTheme.colorScheme.secondary
                            )
                        }
                        
                        TextButton(
                            onClick = { showLedger = true },
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text("View Detailed Ledger")
                        }
                    }
                }
            }

            // Collect Cash Section
            if (!uiState.paymentCompleted || isManualPaymentEnabled) {
                item {
                    Text(
                        text = "Collect Payment",
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(top = 8.dp)
                    )
                }

                item {
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(16.dp),
                        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                        border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant)
                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp)
                        ) {
                            Text(
                                text = "Expected Amount",
                                style = MaterialTheme.typography.labelMedium,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                            Text(
                                text = "₹${
                                    String.format(
                                        Locale.getDefault(),
                                        "%.2f",
                                        amountToBePaid
                                    )
                                }",
                                style = MaterialTheme.typography.headlineMedium,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.primary
                            )

                            Spacer(modifier = Modifier.height(16.dp))

                            OutlinedTextField(
                                value = amountCollected,
                                onValueChange = { amountCollected = it },
                                label = { Text("Amount Collected") },
                                prefix = { Text("₹") },
                                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                                modifier = Modifier.fillMaxWidth(),
                                shape = RoundedCornerShape(12.dp)
                            )

                            Spacer(modifier = Modifier.height(12.dp))

                            OutlinedTextField(
                                value = note,
                                onValueChange = { note = it },
                                label = { Text("Notes (Optional)") },
                                leadingIcon = {
                                    Icon(
                                        Icons.AutoMirrored.Filled.Notes,
                                        contentDescription = null
                                    )
                                },
                                modifier = Modifier.fillMaxWidth(),
                                shape = RoundedCornerShape(12.dp)
                            )

                            Spacer(modifier = Modifier.height(20.dp))

                            Button(
                                onClick = {
                                    if (amountCollected.isNotBlank()) {
                                        viewModel.makePayment(transaction.id, amountCollected, note)
                                        amountCollected = ""
                                        note = ""
                                        isManualPaymentEnabled = false
                                    }
                                },
                                modifier = Modifier.fillMaxWidth(),
                                shape = RoundedCornerShape(12.dp),
                                enabled = amountCollected.isNotBlank() && !uiState.isLoading
                            ) {
                                Text("Submit Payment", modifier = Modifier.padding(vertical = 4.dp))
                            }

                            if (remainingBalance > 0) {
                                Spacer(modifier = Modifier.height(8.dp))
                                OutlinedButton(
                                    onClick = { showForecloseDialog = true },
                                    modifier = Modifier.fillMaxWidth(),
                                    shape = RoundedCornerShape(12.dp),
                                    colors = ButtonDefaults.outlinedButtonColors(
                                        contentColor = MaterialTheme.colorScheme.error
                                    ),
                                    enabled = !uiState.isLoading
                                ) {
                                    Text(
                                        "Foreclose Loan (₹$remainingBalance)",
                                        modifier = Modifier.padding(vertical = 4.dp)
                                    )
                                }
                            }
                        }
                    }
                }
            } else {
                item {
                    Button(
                        onClick = { isManualPaymentEnabled = true },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Text("Make Payment")
                    }
                }
            }

            // History Header
            if (uiState.paymentHistory.isNotEmpty()) {
                item {
                    Text(
                        text = "Payment History",
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(top = 8.dp)
                    )
                }

                items(uiState.paymentHistory) { payment ->
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(16.dp),
                        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column {
                                Text(
                                    text = "₹${payment.amount_paid}",
                                    style = MaterialTheme.typography.titleMedium,
                                    fontWeight = FontWeight.Bold,
                                    color = MaterialTheme.colorScheme.primary
                                )
                                if (!payment.notes.isNullOrBlank()) {
                                    Text(
                                        text = payment.notes,
                                        style = MaterialTheme.typography.bodySmall,
                                        color = MaterialTheme.colorScheme.onSurfaceVariant
                                    )
                                }
                            }
                            Text(
                                text = DateUtils.formatSqlDate(payment.created_at),
                                style = MaterialTheme.typography.labelMedium,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }
                }
            } else if (!uiState.isLoading) {
                item {
                    EmptyState(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(24.dp),
                        message = "No payment history found",
                        actionText = "",
                        onActionClick = {}
                    )
                }
            }
        }
    }

    if (showForecloseDialog) {
        AlertDialog(
            onDismissRequest = { showForecloseDialog = false },
            title = { Text("Foreclose Loan") },
            text = { Text("Are you sure you want to foreclose this loan by paying the remaining balance of ₹$remainingBalance?") },
            confirmButton = {
                Button(
                    onClick = {
                        viewModel.makePayment(transaction.id, remainingBalance.toString(), "Foreclosure")
                        showForecloseDialog = false
                        isManualPaymentEnabled = false
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.error)
                ) {
                    Text("Confirm Foreclosure")
                }
            },
            dismissButton = {
                TextButton(onClick = { showForecloseDialog = false }) {
                    Text("Cancel")
                }
            }
        )
    }
}
