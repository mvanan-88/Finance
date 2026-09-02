package com.mathi.finance.features.master.presentation

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuAnchorType
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.mathi.finance.features.master.domain.model.instalment_data
import com.mathi.finance.ui.presentation.AppBar
import kotlinx.coroutines.launch
import org.koin.androidx.compose.koinViewModel
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun InstalmentScreen(
    onBack: () -> Unit,
    viewModel: MasterViewModel = koinViewModel()
) {
    val uiState by viewModel.listState.collectAsState()
    val scope = rememberCoroutineScope()
    val sheetState = rememberModalBottomSheetState()
    var showBottomSheet by remember { mutableStateOf(false) }

    // Form States
    var tenureInput by remember { mutableStateOf("") }
    var transactionStatus by remember { mutableIntStateOf(1) } // 1 for Active, 0 for Inactive
    var editingItem by remember { mutableStateOf<instalment_data?>(null) }
    var expanded by remember { mutableStateOf(false) }

    LaunchedEffect("") {
        viewModel.fetchInstalment()
    }

    Scaffold(
        topBar = { AppBar("Instalment Tenure", onBack = onBack) },
        floatingActionButton = {
            FloatingActionButton(
                onClick = {
                    editingItem = null
                    tenureInput = ""
                    transactionStatus = 1
                    showBottomSheet = true
                }
            ) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = "Add Item"
                )
            }
        },
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(top = innerPadding.calculateTopPadding()),
            contentAlignment = Alignment.Center
        ) {
            if (uiState.isLoading) {
                CircularProgressIndicator()
            }
            if (uiState.instalmentList.isNotEmpty()) {
                LazyColumn(modifier = Modifier.fillMaxHeight()) {
                    items(items = uiState.instalmentList, itemContent = { item ->
                        Card(
                            elevation = CardDefaults.cardElevation(defaultElevation = 10.dp),
                            modifier = Modifier
                                .padding(all = 16.dp)
                                .fillMaxWidth()
                        ) {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(text = item.tenure.toString(), modifier = Modifier.padding(all = 16.dp))
                                Row(
                                    modifier = Modifier.padding(all = 16.dp),
                                    horizontalArrangement = Arrangement.spacedBy(16.dp),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Box(
                                        modifier = Modifier
                                            .size(12.dp)
                                            .background(
                                                color = if (item.status == 1) Color.Green else Color.Red,
                                                shape = CircleShape
                                            )
                                    )
                                    Icon(
                                        Icons.Default.Edit,
                                        contentDescription = "Edit",
                                        modifier = Modifier
                                            .size(16.dp)
                                            .clickable {
                                                editingItem = item
                                                tenureInput = item.tenure.toString()
                                                transactionStatus = item.status
                                                showBottomSheet = true
                                            },
                                    )
                                }
                            }
                        }
                    })
                }
            }
        }

        if (showBottomSheet) {
            ModalBottomSheet(
                onDismissRequest = {
                    showBottomSheet = false
                    editingItem = null
                },
                sheetState = sheetState
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = if (editingItem == null) "Add Instalment Tenure" else "Edit Instalment Tenure",
                        style = MaterialTheme.typography.titleLarge
                    )
                    Spacer(modifier = Modifier.height(16.dp))

                    TextField(
                        value = tenureInput,
                        onValueChange = { input ->
                            if (input.isEmpty() || input.toIntOrNull() != null) {
                                tenureInput = input
                            }
                        },
                        label = { Text("Tenure (weeks)") },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        modifier = Modifier.fillMaxWidth()
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    // Status Dropdown
                    ExposedDropdownMenuBox(
                        expanded = expanded,
                        onExpandedChange = { expanded = !expanded },
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        TextField(
                            value = if (transactionStatus == 1) "Active" else "Inactive",
                            onValueChange = {},
                            readOnly = true,
                            label = { Text("Status") },
                            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
                            modifier = Modifier
                                .menuAnchor(ExposedDropdownMenuAnchorType.PrimaryNotEditable)
                                .fillMaxWidth()
                        )
                        ExposedDropdownMenu(
                            expanded = expanded,
                            onDismissRequest = { expanded = false }
                        ) {
                            DropdownMenuItem(
                                text = { Text("Active") },
                                onClick = {
                                    transactionStatus = 1
                                    expanded = false
                                }
                            )
                            DropdownMenuItem(
                                text = { Text("Inactive") },
                                onClick = {
                                    transactionStatus = 0
                                    expanded = false
                                }
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(24.dp))

                    Button(
                        onClick = {
                            val rate = tenureInput.toIntOrNull() ?: 0
                            if (rate > 0.0f) {
                                if (editingItem == null) {
                                    val sdf = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSXXX", Locale.getDefault())
                                    val currentDate = sdf.format(Date())
                                    val newType = instalment_data(
                                        tenure = rate,
                                        created_at = currentDate,
                                        status = transactionStatus,
                                    )
                                    viewModel.addInstalment(newType)
                                } else {
                                    val updatedType = editingItem!!.copy(
                                        tenure = rate,
                                        status = transactionStatus
                                    )
                                    viewModel.updateInstalment(updatedType)
                                }

                                scope.launch { sheetState.hide() }.invokeOnCompletion {
                                    if (!sheetState.isVisible) {
                                        showBottomSheet = false
                                        editingItem = null
                                    }
                                }
                            }
                        },
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text("Submit")
                    }
                    Spacer(modifier = Modifier.height(32.dp))
                }
            }
        }
    }
}