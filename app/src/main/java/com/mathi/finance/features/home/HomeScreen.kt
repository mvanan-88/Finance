package com.mathi.finance.features.home

import androidx.compose.foundation.background
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
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.material.icons.automirrored.filled.TrendingDown
import androidx.compose.material.icons.automirrored.filled.TrendingUp
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
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.mathi.finance.ui.presentation.AppBar
import org.koin.androidx.compose.koinViewModel
import java.util.Locale

@Composable
fun HomeScreen(
    onSignOut: () -> Unit,
    viewModel: HomeViewModel = koinViewModel()
) {
    val uiState by viewModel.uiState.collectAsState(HomeUIState())
    val colorScheme = MaterialTheme.colorScheme

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
            contentPadding = PaddingValues(16.dp),
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
                                color = Color.White.copy(alpha = 0.8f)
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(
                                text = "₹${
                                    String.format(
                                        Locale.getDefault(),
                                        "%.2f",
                                        uiState.summary?.totalActiveLended ?: 0f
                                    )
                                }",
                                style = MaterialTheme.typography.headlineLarge,
                                color = Color.White,
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
                    totalOutstanding = uiState.summary?.totalActiveLended ?: 0f,
                    totalRecovered = uiState.summary?.totalActiveRecovered ?: 0f
                )
            }

            item {
                LoanStatusPieChart(
                    activeCount = uiState.summary?.activeLoans ?: 0,
                    completedCount = uiState.summary?.completedLoans ?: 0
                )
            }

            item {
                TopDebtorChart(debtors = uiState.topDebtors)
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
fun SummaryMini(label: String, value: String, icon: ImageVector) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = Color.White,
            modifier = Modifier.size(16.dp)
        )
        Spacer(modifier = Modifier.width(4.dp))
        Column {
            Text(
                text = label,
                style = MaterialTheme.typography.labelSmall,
                color = Color.White.copy(alpha = 0.7f)
            )
            Text(
                text = value,
                style = MaterialTheme.typography.bodySmall,
                color = Color.White,
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
