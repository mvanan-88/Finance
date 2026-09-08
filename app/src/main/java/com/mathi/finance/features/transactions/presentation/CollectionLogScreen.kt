package com.mathi.finance.features.transactions.presentation

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.mathi.finance.core.util.DateUtils
import com.mathi.finance.features.transactions.domain.model.TransactionSummary
import com.mathi.finance.ui.presentation.AppBar
import com.mathi.finance.ui.presentation.EmptyState
import org.koin.androidx.compose.koinViewModel
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun CollectionLogScreen(
    onSignOut: () -> Unit,
    viewModel: CollectionLogViewModel = koinViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    var selectedMonth by remember { mutableIntStateOf(Calendar.getInstance().get(Calendar.MONTH)) }
    var selectedYear by remember { mutableIntStateOf(Calendar.getInstance().get(Calendar.YEAR)) }

    // Group and Filter logs
    val groupedLogs = remember(uiState.logs, selectedMonth, selectedYear) {
        uiState.logs
            .filter { log ->
                val logDate = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).parse(log.created_at.take(10))
                val cal = Calendar.getInstance().apply { time = logDate!! }
                cal.get(Calendar.MONTH) == selectedMonth && cal.get(Calendar.YEAR) == selectedYear
            }
            .groupBy { it.created_at.take(10) }
    }

    Scaffold(
        topBar = { AppBar("Collection Audit", onSignOut = onSignOut) }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
        ) {
            MonthYearPicker(
                selectedMonth = selectedMonth,
                selectedYear = selectedYear,
                onMonthSelected = { selectedMonth = it },
                onYearSelected = { selectedYear = it }
            )

            if (uiState.isLoading) {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator()
                }
            } else if (uiState.logs.isEmpty()) {
                EmptyState(
                    message = "No collection logs found for this period.",
                    actionText = "Refresh",
                    onActionClick = { viewModel.fetchLogs() }
                )
            } else {
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(bottom = 32.dp)
                ) {
                    groupedLogs.forEach { (date, logs) ->
                        val dayTotal = logs.sumOf { it.amount_paid.toDouble() }
                        
                        stickyHeader {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .background(MaterialTheme.colorScheme.surfaceVariant)
                                    .padding(horizontal = 16.dp, vertical = 8.dp),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = DateUtils.formatSqlDate(date),
                                    style = MaterialTheme.typography.titleSmall,
                                    fontWeight = FontWeight.Bold,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                                Text(
                                    text = "Total: ₹${String.format(Locale.getDefault(), "%.0f", dayTotal)}",
                                    style = MaterialTheme.typography.labelLarge,
                                    fontWeight = FontWeight.Black,
                                    color = MaterialTheme.colorScheme.primary
                                )
                            }
                        }

                        items(logs) { log ->
                            CollectionLogItem(log)
                            HorizontalDivider(
                                modifier = Modifier.padding(horizontal = 16.dp),
                                color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.5f)
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun MonthYearPicker(
    selectedMonth: Int,
    selectedYear: Int,
    onMonthSelected: (Int) -> Unit,
    onYearSelected: (Int) -> Unit
) {
    val months = listOf("Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep", "Oct", "Nov", "Dec")
    val years = (2023..2030).toList()
    var expandedMonth by remember { mutableStateOf(false) }
    var expandedYear by remember { mutableStateOf(false) }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Box(modifier = Modifier.weight(1f)) {
            OutlinedButton(
                onClick = { expandedMonth = true },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(months[selectedMonth])
            }
            DropdownMenu(expanded = expandedMonth, onDismissRequest = { expandedMonth = false }) {
                months.forEachIndexed { index, name ->
                    DropdownMenuItem(text = { Text(name) }, onClick = {
                        onMonthSelected(index)
                        expandedMonth = false
                    })
                }
            }
        }
        Box(modifier = Modifier.weight(1f)) {
            OutlinedButton(
                onClick = { expandedYear = true },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(selectedYear.toString())
            }
            DropdownMenu(expanded = expandedYear, onDismissRequest = { expandedYear = false }) {
                years.forEach { year ->
                    DropdownMenuItem(text = { Text(year.toString()) }, onClick = {
                        onYearSelected(year)
                        expandedYear = false
                    })
                }
            }
        }
    }
}

@Composable
fun CollectionLogItem(log: TransactionSummary) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = log.name,
                style = MaterialTheme.typography.bodyLarge,
                fontWeight = FontWeight.SemiBold
            )
            if (!log.notes.isNullOrBlank()) {
                Text(
                    text = log.notes,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
        Text(
            text = "₹${String.format(Locale.getDefault(), "%.0f", log.amount_paid)}",
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold,
            color = Color(0xFF4CAF50)
        )
    }
}
