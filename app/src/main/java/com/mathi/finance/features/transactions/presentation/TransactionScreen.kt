package com.mathi.finance.features.transactions.presentation

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.mathi.finance.features.transactions.domain.model.TransactionType
import com.mathi.finance.ui.presentation.AppBar

@Composable
fun TransactionScreen(
    modifier: Modifier = Modifier,
) {
val list = listOf<String>("Collect","Closed Transactions","New")
    Scaffold(
        topBar = { AppBar("Transactions") }
    ) {innerPadding->
        Column(modifier = modifier
            .fillMaxSize()
            .padding(innerPadding)) {


        }
    }
}