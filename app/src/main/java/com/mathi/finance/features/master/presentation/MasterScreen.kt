package com.mathi.finance.features.master.presentation

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
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
import com.mathi.finance.features.master.domain.model.master_data
import com.mathi.finance.ui.presentation.AppBar

@Composable
fun MasterScreen(onSignOut: () -> Unit) {
    var selectedId by remember { mutableStateOf<String?>(null) }
    
    if (selectedId == null) {
        MasterListScreen(onItemSelected = { selectedId = it }, onSignOut = onSignOut)
    } else {
        when (selectedId) {
            "1" -> TransactionTypeScreen(onBack = { selectedId = null })
            "2" -> InterestRateScreen(onBack = { selectedId = null })
            "3" -> InstalmentScreen(onBack = { selectedId = null })
        }
    }
}

@Composable
fun MasterListScreen(onItemSelected: (String) -> Unit, onSignOut: () -> Unit) {
    var list = ArrayList<master_data>()
    var md = master_data(id = "1",master="Transaction Type")
    list.add(md)
    md = master_data(id = "2",master="Interest Rates")
    list.add(md)
    md = master_data(id = "3",master="Instalment Tenures")
    list.add(md)
    Scaffold(
        topBar = { AppBar("Master Data", onSignOut = onSignOut) },
        containerColor = Color.Transparent
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {

            LazyColumn(modifier = Modifier.fillMaxHeight()) {
                items(items = list, itemContent = { item ->
                    Card(
                        elevation = CardDefaults.cardElevation(defaultElevation = 10.dp),
                        modifier = Modifier
                            .padding(all = 16.dp)
                            .fillMaxWidth()
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable(onClick = { onItemSelected(item.id) }),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(text = item.master, modifier = Modifier.padding(all = 16.dp))
                            Icon(Icons.Default.ArrowCircleRight, contentDescription = null,modifier = Modifier.padding(all = 16.dp))
                        }
                    }
                })
            }
        }

    }
}
