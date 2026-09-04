package com.mathi.finance.features.reports

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowCircleRight
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.mathi.finance.features.home.presentation.DashboardScreen
import com.mathi.finance.features.master.domain.model.master_data
import com.mathi.finance.features.transactions.presentation.CollectionLogScreen
import com.mathi.finance.features.transactions.presentation.RiskReportScreen
import com.mathi.finance.ui.presentation.AppBar

@Composable
fun ReportsScreen(onSignOut: () -> Unit) {
    var selectedId by remember { mutableStateOf<String?>(null) }

    if (selectedId == null) {
        ReportsListScreen(onItemSelected = { selectedId = it }, onSignOut = onSignOut)
    } else {
        when (selectedId) {
            "1" -> DashboardScreen(onSignOut = { selectedId = null })
            "2" -> RiskReportScreen(onSignOut = { selectedId = null })
            "3" -> CollectionLogScreen(onSignOut = { selectedId = null })
        }
    }
}

@Composable
fun ReportsListScreen(onItemSelected: (String) -> Unit, onSignOut: () -> Unit) {
    val list = listOf(
        master_data(id = "1", master = "Performance Analytics"),
        master_data(id = "2", master = "Risk Assessment"),
        master_data(id = "3", master = "Collection Audit")
    )

    Scaffold(
        topBar = { AppBar("Reports", onSignOut = onSignOut) },
        containerColor = Color.Transparent
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
            contentPadding = PaddingValues(bottom = 32.dp)
        ) {
            items(list) { item ->
                Card(
                    elevation = CardDefaults.cardElevation(defaultElevation = 10.dp),
                    modifier = Modifier
                        .padding(horizontal = 16.dp, vertical = 8.dp)
                        .fillMaxWidth()
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { onItemSelected(item.id) },
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(text = item.master, modifier = Modifier.padding(16.dp))
                        Icon(
                            Icons.Default.ArrowCircleRight,
                            contentDescription = null,
                            modifier = Modifier.padding(16.dp)
                        )
                    }
                }
            }
        }
    }
}
