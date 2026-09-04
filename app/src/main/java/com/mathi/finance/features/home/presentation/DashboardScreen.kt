package com.mathi.finance.features.home.presentation

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.mathi.finance.features.home.domain.model.TransactionDashboardSummary
import com.mathi.finance.ui.presentation.AppBar
import org.koin.androidx.compose.koinViewModel
import java.util.Locale

@Composable
fun DashboardScreen(
    onSignOut: () -> Unit,
    viewModel: DashboardViewModel = koinViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()

    Scaffold(
        topBar = { AppBar("Performance Dashboard", onSignOut = onSignOut) }
    ) { padding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
        ) {
            if (uiState.isLoading) {
                CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
            }

            uiState.summary?.let { summary ->
                DashboardGrid(summary)
            }

            uiState.error?.let { error ->
                Text(
                    text = error,
                    color = MaterialTheme.colorScheme.error,
                    modifier = Modifier
                        .align(Alignment.Center)
                        .padding(16.dp)
                )
            }
        }
    }
}

@Composable
fun DashboardGrid(summary: TransactionDashboardSummary) {
    val netOutstanding = summary.lended - summary.recovered

    LazyVerticalGrid(
        columns = GridCells.Fixed(2),
        contentPadding = PaddingValues(16.dp),
        horizontalArrangement = Arrangement.spacedBy(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
        modifier = Modifier.fillMaxSize()
    ) {
        item {
            MetricCard(
                title = "Total Lended",
                value = "₹${String.format(Locale.getDefault(), "%.2f", summary.lended)}",
                color = Color(0xFF2196F3) // Blue
            )
        }
        item {
            MetricCard(
                title = "Total Recovered",
                value = "₹${String.format(Locale.getDefault(), "%.2f", summary.recovered)}",
                color = Color(0xFF4CAF50) // Green
            )
        }
        item {
            MetricCard(
                title = "Net Outstanding",
                value = "₹${String.format(Locale.getDefault(), "%.2f", netOutstanding)}",
                color = if (netOutstanding > 0) MaterialTheme.colorScheme.error else MaterialTheme.colorScheme.primary
            )
        }
        item {
            MetricCard(
                title = "Loan Status",
                value = "${summary.activeCount} Active / ${summary.inactiveCount} Done",
                color = MaterialTheme.colorScheme.secondary
            )
        }
    }
}

@Composable
fun MetricCard(
    title: String,
    value: String,
    color: Color
) {
    ElevatedCard(
        modifier = Modifier
            .fillMaxWidth()
            .height(140.dp),
        colors = CardDefaults.elevatedCardColors(
            containerColor = color.copy(alpha = 0.1f)
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.Start
        ) {
            Text(
                text = title,
                style = MaterialTheme.typography.labelLarge,
                color = color
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = value,
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Black,
                color = color
            )
        }
    }
}
