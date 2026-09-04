package com.mathi.finance.features.transactions.presentation

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.mathi.finance.features.transactions.domain.model.RiskTransaction
import com.mathi.finance.ui.presentation.AppBar
import com.mathi.finance.ui.presentation.EmptyState
import org.koin.androidx.compose.koinViewModel

@Composable
fun RiskReportScreen(
    onSignOut: () -> Unit,
    viewModel: RiskReportViewModel = koinViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    var selectedTabIndex by remember { mutableIntStateOf(0) }
    val categories = listOf("High", "Medium", "Low")

    val filteredTransactions = remember(selectedTabIndex, uiState.transactions) {
        uiState.transactions.filter { 
            it.riskLevel.equals(categories[selectedTabIndex], ignoreCase = true) 
        }
    }

    Scaffold(
        topBar = { AppBar("Risk Assessment", onSignOut = onSignOut) }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
        ) {
            TabRow(
                selectedTabIndex = selectedTabIndex,
                containerColor = MaterialTheme.colorScheme.surface,
                contentColor = MaterialTheme.colorScheme.primary
            ) {
                categories.forEachIndexed { index, title ->
                    Tab(
                        selected = selectedTabIndex == index,
                        onClick = { selectedTabIndex = index },
                        text = {
                            Text(
                                text = title,
                                color = if (selectedTabIndex == index) {
                                    when (index) {
                                        0 -> Color.Red
                                        1 -> Color(0xFFFFA000) // Amber
                                        else -> Color(0xFF4CAF50) // Green
                                    }
                                } else MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    )
                }
            }

            if (uiState.isLoading) {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator()
                }
            } else if (filteredTransactions.isEmpty()) {
                EmptyState(
                    message = "No ${categories[selectedTabIndex]} risk transactions found.",
                    actionText = "Refresh",
                    onActionClick = { viewModel.fetchRiskTransactions() }
                )
            } else {
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(16.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    items(filteredTransactions) { transaction ->
                        RiskTransactionItem(transaction)
                    }
                }
            }
        }
    }
}

@Composable
fun RiskTransactionItem(transaction: RiskTransaction) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Row(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = transaction.name,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "Principal: ₹${transaction.amount}",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }

            Column(horizontalAlignment = Alignment.End) {
                AssistChip(
                    onClick = { },
                    label = { 
                        Text(
                            text = "${transaction.daysDifference} days overdue",
                            style = MaterialTheme.typography.labelSmall
                        ) 
                    },
                    leadingIcon = {
                        Icon(
                            imageVector = Icons.Default.Warning,
                            contentDescription = null,
                            modifier = Modifier.size(16.dp),
                            tint = when (transaction.riskLevel.lowercase()) {
                                "high" -> Color.Red
                                "medium" -> Color(0xFFFFA000)
                                else -> Color(0xFF4CAF50)
                            }
                        )
                    },
                    colors = AssistChipDefaults.assistChipColors(
                        labelColor = MaterialTheme.colorScheme.onSurface
                    )
                )
            }
        }
    }
}
