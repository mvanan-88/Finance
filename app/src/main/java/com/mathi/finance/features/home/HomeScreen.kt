package com.mathi.finance.features.home

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.material.icons.automirrored.filled.TrendingDown
import androidx.compose.material.icons.automirrored.filled.TrendingUp
import androidx.compose.material.icons.filled.Flag
import androidx.compose.material.icons.filled.Insights
import androidx.compose.material.icons.filled.PieChart
import androidx.compose.material.icons.filled.QrCodeScanner
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
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
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.mathi.finance.features.transactions.domain.model.TransactionSummary
import com.mathi.finance.features.transactions.presentation.TransactionDetailScreen
import com.mathi.finance.ui.components.SmsReminderButton
import com.mathi.finance.ui.components.WhatsAppReminderButton
import com.mathi.finance.ui.presentation.AppBar
import org.koin.androidx.compose.koinViewModel
import java.util.Locale

@Composable
fun HomeScreen(
    onSignOut: () -> Unit,
    viewModel: HomeViewModel = koinViewModel()
) {
    LaunchedEffect(Unit) {
        viewModel.fetchIncomeExpense()
        viewModel.fetchUrgentActions()
    }
    val uiState by viewModel.uiState.collectAsState(HomeUIState())
    val colorScheme = MaterialTheme.colorScheme

    var selectedTransaction by remember { mutableStateOf<TransactionSummary?>(null) }

    if (selectedTransaction != null) {
        TransactionDetailScreen(
            transaction = selectedTransaction!!,
            onBack = { selectedTransaction = null }
        )
        return
    }

    val actions = remember(colorScheme) {
        listOf(
            QuickActionItem("Scan Receipt", Icons.Default.QrCodeScanner, colorScheme.secondary),
            QuickActionItem("Send Money", Icons.AutoMirrored.Filled.Send, colorScheme.tertiary),
            QuickActionItem("Budget", Icons.Default.PieChart, colorScheme.primary),
            QuickActionItem("Insight", Icons.Default.Insights, colorScheme.onSurfaceVariant)
        )
    }

    Scaffold(
        topBar = { AppBar("Dashboard", onSignOut = onSignOut) },
        containerColor = Color.Transparent
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background)
                .padding(innerPadding),
            contentPadding = PaddingValues(start = 16.dp, end = 16.dp, top = 16.dp, bottom = 32.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Summary Card (Uncommented and standardized)
            item {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(180.dp),
                    shape = RoundedCornerShape(24.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primary),
                    elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(24.dp)
                    ) {
                        Column(modifier = Modifier.align(Alignment.TopStart)) {
                            Text(
                                text = "Total Balance",
                                style = MaterialTheme.typography.bodyMedium,
                                color = MaterialTheme.colorScheme.onPrimary.copy(alpha = 0.8f)
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(
                                text = "₹${
                                    String.format(
                                        Locale.getDefault(),
                                        "%.2f",
                                        uiState.summary?.lended ?: 0f
                                    )
                                }",
                                style = MaterialTheme.typography.headlineLarge,
                                color = MaterialTheme.colorScheme.onPrimary,
                                fontWeight = FontWeight.Black
                            )
                        }

                        Row(
                            modifier = Modifier.align(Alignment.BottomStart),
                            horizontalArrangement = Arrangement.spacedBy(16.dp)
                        ) {
                            SummaryMini(
                                label = "Income",
                                value = "+₹2,100",
                                icon = Icons.AutoMirrored.Filled.TrendingUp
                            )
                            SummaryMini(
                                label = "Expense",
                                value = "-₹850",
                                icon = Icons.AutoMirrored.Filled.TrendingDown
                            )
                        }
                    }
                }
            }

            item {
                RecoveryProgressCard(
                    totalOutstanding = uiState.summary?.lended ?: 0f,
                    totalRecovered = uiState.summary?.recovered ?: 0f
                )
            }

            item {
                LoanStatusPieChart(
                    activeCount = uiState.summary?.activeCount ?: 0,
                    completedCount = uiState.summary?.inactiveCount ?: 0
                )
            }

            item {
                TopDebtorChart(debtors = uiState.topDebtors)
            }

            item {
                Text(
                    text = "Urgent Actions",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(top = 8.dp)
                )
            }

            if (uiState.urgentActions.isNotEmpty()) {
                items(uiState.urgentActions) { action ->
                    UrgentActionItem(
                        transaction = action,
                        onClick = { selectedTransaction = action }
                    )
                }
            } else {
                item {
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(16.dp),
                        colors = CardDefaults.cardColors(
                            containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f)
                        )
                    ) {
                        Text(
                            text = "No urgent actions found. You're all caught up!",
                            modifier = Modifier.padding(16.dp),
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            }

            item {
                Text(
                    text = "Quick Actions",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(top = 8.dp)
                )
            }

            // Grid-like layout for Quick Actions using chunks in LazyColumn
            val actionRows = actions.chunked(2)
            itemsIndexed(actionRows) { _, rowItems ->
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    rowItems.forEach { action ->
                        Box(modifier = Modifier.weight(1f)) {
                            ActionCard(action)
                        }
                    }
                    if (rowItems.size < 2) {
                        Spacer(modifier = Modifier.weight(1f))
                    }
                }
            }

            item {
                Spacer(modifier = Modifier.height(16.dp))
            }
        }
    }
}

@Composable
fun UrgentActionItem(transaction: TransactionSummary, onClick: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp)
            .clickable { onClick() },
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.errorContainer.copy(alpha = 0.1f)
        ),
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.error.copy(alpha = 0.2f))
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.weight(1f)) {
                    Icon(
                        imageVector = Icons.Default.Flag,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.error,
                        modifier = Modifier.size(24.dp)
                    )
                    Spacer(modifier = Modifier.width(12.dp))
                    Column {
                        Text(
                            text = transaction.name,
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = "Overdue by ${transaction.days_difference} days",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.error
                        )
                    }
                }
                val amountToBePaid = if (transaction.interest_rate != null && (transaction.interest_rate > 0)) {
                    transaction.amount * (transaction.interest_rate.toFloat() / 100f)
                } else if (transaction.tenure != null && transaction.tenure > 0) {
                    transaction.amount / transaction.tenure
                } else {
                    transaction.amount
                }
                Text(
                    text = "₹$amountToBePaid",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Black,
                    color = MaterialTheme.colorScheme.error
                )
            }
            
            Spacer(modifier = Modifier.height(12.dp))
            
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                WhatsAppReminderButton(
                    phoneNumber = "+919791580355",
                    message = "Hi ${transaction.name}, this is a reminder for your upcoming payment of ₹${transaction.amount}. Please clear it at your earliest.",
                    modifier = Modifier.weight(1f),
                    buttonText = "WhatsApp"
                )
                SmsReminderButton(
                    phoneNumber = "+919791580355",
                    message = "Friendly reminder: Your payment of ₹${transaction.amount} is overdue.",
                    modifier = Modifier.weight(1f),
                    buttonText = "SMS"
                )
            }
        }
    }
}

@Composable
fun SummaryMini(label: String, value: String, icon: ImageVector) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = MaterialTheme.colorScheme.onPrimary,
            modifier = Modifier.size(16.dp)
        )
        Spacer(modifier = Modifier.width(4.dp))
        Column {
            Text(
                text = label,
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.onPrimary.copy(alpha = 0.7f)
            )
            Text(
                text = value,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onPrimary,
                fontWeight = FontWeight.Bold
            )
        }
    }
}

data class QuickActionItem(val title: String, val icon: ImageVector, val color: Color)

@Composable
fun ActionCard(action: QuickActionItem) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(100.dp),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            horizontalAlignment = Alignment.Start,
            verticalArrangement = Arrangement.Center
        ) {
            Icon(
                imageVector = action.icon,
                contentDescription = action.title,
                tint = action.color,
                modifier = Modifier.size(28.dp)
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = action.title,
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = FontWeight.SemiBold
            )
        }
    }
}
