package com.mathi.finance.features.transactions.presentation

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.mathi.finance.features.contacts.domain.model.Contact
import com.mathi.finance.features.master.domain.model.InterestRates
import com.mathi.finance.features.master.domain.model.TransactionType
import com.mathi.finance.features.transactions.domain.model.PerPersonTransaction
import com.mathi.finance.ui.presentation.AppBar
import kotlinx.coroutines.launch
import org.koin.androidx.compose.koinViewModel
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

/**
 * Main transaction screen displaying a list of recorded transactions and providing
 * functionality to create new transactions via a bottom sheet form.
 *
 * @param onSignOut Callback invoked when user requests sign out from the top app bar.
 * @param modifier Optional [Modifier] for screen-level layout adjustments.
 * @param viewModel The [TransactionViewModel] instance providing UI state and transaction actions.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TransactionScreen(
    onSignOut: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: TransactionViewModel = koinViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    val scope = rememberCoroutineScope()
    val sheetState = rememberModalBottomSheetState()
    var showBottomSheet by remember { mutableStateOf(false) }

    // Form States
    var selectedContact by remember { mutableStateOf<Contact?>(null) }
    var selectedType by remember { mutableStateOf<TransactionType?>(null) }
    var selectedInterestRate by remember { mutableStateOf<InterestRates?>(null) }
    var selectedInstalment by remember { mutableStateOf<com.mathi.finance.features.master.domain.model.instalment_data?>(null) }
    var tenureInput by remember { mutableStateOf("") }
    var amountInput by remember { mutableStateOf("") }
    
    var showContactDialog by remember { mutableStateOf(false) }
    var typesExpanded by remember { mutableStateOf(false) }
    var interestExpanded by remember { mutableStateOf(false) }
    var instalmentExpanded by remember { mutableStateOf(false) }


    Scaffold(
        topBar = { AppBar("Transactions", onSignOut = onSignOut) },
        containerColor = Color.Transparent,
        floatingActionButton = {
            FloatingActionButton(
                onClick = {
                    showBottomSheet = true
                }
            ) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = "New Transaction"
                )
            }
        }
    ) { innerPadding ->
        Column(
            modifier = modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            if (uiState.isLoading) {
                LinearProgressIndicator(modifier = Modifier.fillMaxWidth())
            }

            LazyColumn(modifier = Modifier.weight(1f)) {
                items(uiState.transactions) { transaction ->
                    val contactName = transaction.contact?.name ?: "Unknown"
                    val typeName = transaction.transactionType?.name ?: "Unknown"
                    val interestRate = transaction.interestRate?.rate
                    val instalmentTenure = transaction.instalment_tenure?.tenure

                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp, vertical = 8.dp),
                        shape = RoundedCornerShape(16.dp),
                        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                    ) {
                        Column(modifier = Modifier.padding(20.dp)) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Column {
                                    Text(
                                        text = contactName,
                                        style = MaterialTheme.typography.titleMedium,
                                        fontWeight = FontWeight.Bold
                                    )
                                    Text(
                                        text = typeName,
                                        style = MaterialTheme.typography.bodySmall,
                                        color = MaterialTheme.colorScheme.onSurfaceVariant
                                    )
                                }
                                Column(horizontalAlignment = Alignment.End) {
                                    Text(
                                        text = "$${transaction.amount}",
                                        style = MaterialTheme.typography.titleLarge,
                                        color = MaterialTheme.colorScheme.primary,
                                        fontWeight = FontWeight.Black
                                    )
                                    if (interestRate != null) {
                                        Text(
                                            text = "Int: $interestRate%",
                                            style = MaterialTheme.typography.labelSmall,
                                            color = MaterialTheme.colorScheme.secondary
                                        )
                                    } else if (instalmentTenure != null) {
                                        Text(
                                            text = if (transaction.transactionType?.name == "Installment") "Tenure: ${instalmentTenure} weeks" else "Tenure: ${instalmentTenure} days",
                                            style = MaterialTheme.typography.labelSmall,
                                            color = MaterialTheme.colorScheme.secondary
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }

        if (showBottomSheet) {
            ModalBottomSheet(
                onDismissRequest = { showBottomSheet = false },
                sheetState = sheetState
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                        .padding(bottom = 32.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    Text("Add Transaction", style = MaterialTheme.typography.headlineSmall)

                    // Contact Selector (opens Searchable Dialog)
                    OutlinedTextField(
                        value = selectedContact?.name ?: "",
                        onValueChange = {},
                        readOnly = true,
                        label = { Text("To") },
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { showContactDialog = true },
                        enabled = false,
                        colors = OutlinedTextFieldDefaults.colors(
                            disabledTextColor = MaterialTheme.colorScheme.onSurface,
                            disabledLabelColor = MaterialTheme.colorScheme.onSurfaceVariant,
                            disabledBorderColor = MaterialTheme.colorScheme.outline,
                            disabledLeadingIconColor = MaterialTheme.colorScheme.onSurfaceVariant,
                            disabledTrailingIconColor = MaterialTheme.colorScheme.onSurfaceVariant,
                        )
                    )

                    // Transaction Type Dropdown
                    ExposedDropdownMenuBox(
                        expanded = typesExpanded,
                        onExpandedChange = { typesExpanded = !typesExpanded },
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        TextField(
                            value = selectedType?.name ?: "",
                            onValueChange = {},
                            readOnly = true,
                            label = { Text("Transaction Type") },
                            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = typesExpanded) },
                            modifier = Modifier
                                .menuAnchor(ExposedDropdownMenuAnchorType.PrimaryNotEditable)
                                .fillMaxWidth()
                        )
                        ExposedDropdownMenu(
                            expanded = typesExpanded,
                            onDismissRequest = { typesExpanded = false }
                        ) {
                            uiState.transactionTypes.forEach { type ->
                                DropdownMenuItem(
                                    text = { Text(type.name) },
                                    onClick = {
                                        selectedType = type
                                        typesExpanded = false
                                        // Reset interest if type changes
                                        selectedInterestRate = null
                                    }
                                )
                            }
                        }
                    }

                    // Interest Rate or Tenure Field
                    if (selectedType != null) {
                        if (selectedType?.id == 2) {
                            // Interest Rate Dropdown
                            ExposedDropdownMenuBox(
                                expanded = interestExpanded,
                                onExpandedChange = { interestExpanded = !interestExpanded },
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                TextField(
                                    value = if (selectedInterestRate != null) "${selectedInterestRate?.interest_rate}%" else "",
                                    onValueChange = {},
                                    readOnly = true,
                                    label = { Text("Interest Rate") },
                                    trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = interestExpanded) },
                                    modifier = Modifier
                                        .menuAnchor(ExposedDropdownMenuAnchorType.PrimaryNotEditable)
                                        .fillMaxWidth()
                                )
                                ExposedDropdownMenu(
                                    expanded = interestExpanded,
                                    onDismissRequest = { interestExpanded = false }
                                ) {
                                    uiState.interestRates.forEach { rate ->
                                        DropdownMenuItem(
                                            text = { Text("${rate.interest_rate}%") },
                                            onClick = {
                                                selectedInterestRate = rate
                                                interestExpanded = false
                                            }
                                        )
                                    }
                                }
                            }
                        } else if (selectedType?.id == 1) {
                            // Instalment Dropdown
                            ExposedDropdownMenuBox(
                                expanded = instalmentExpanded,
                                onExpandedChange = { instalmentExpanded = !instalmentExpanded },
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                TextField(
                                    value = if (selectedInstalment != null) "${selectedInstalment?.tenure} weeks" else "",
                                    onValueChange = {},
                                    readOnly = true,
                                    label = { Text("Instalment Tenure") },
                                    trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = instalmentExpanded) },
                                    modifier = Modifier
                                        .menuAnchor(ExposedDropdownMenuAnchorType.PrimaryNotEditable)
                                        .fillMaxWidth()
                                )
                                ExposedDropdownMenu(
                                    expanded = instalmentExpanded,
                                    onDismissRequest = { instalmentExpanded = false }
                                ) {
                                    uiState.instalmentList.forEach { item ->
                                        DropdownMenuItem(
                                            text = { Text("${item.tenure} weeks") },
                                            onClick = {
                                                selectedInstalment = item
                                                instalmentExpanded = false
                                            }
                                        )
                                    }
                                }
                            }
                        } else {
                            // Tenure TextField for other transaction types
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
                        }
                    }

                    // Amount TextField
                    TextField(
                        value = amountInput,
                        onValueChange = { input ->
                            if (input.isEmpty() || input.toFloatOrNull() != null || input == ".") {
                                amountInput = input
                            }
                        },
                        label = { Text("Amount") },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                        modifier = Modifier.fillMaxWidth()
                    )

                    Button(
                        onClick = {
                            val amount = amountInput.toFloatOrNull() ?: 0f
                            val interestId = if (selectedType?.id == 2) selectedInterestRate?.id else null
                            val instalmentId = if (selectedType?.id == 1) selectedInstalment?.id else null
                            val tenure = when {
                                selectedType?.id == 2 -> null
                                selectedType?.id == 1 -> selectedInstalment?.tenure
                                else -> tenureInput.toIntOrNull()
                            }
                            val contactId = selectedContact?.id
                            val typeId = selectedType?.id
                            val currentUserId = viewModel.currentUserId

                            if (amount > 0 && contactId != null && typeId != null && currentUserId != -1) {
                                val sdf = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS", Locale.getDefault())
                                val currentDate = sdf.format(Date())

                                val transaction = PerPersonTransaction(
                                    created_at = currentDate,
                                    contact_id = contactId,
                                    transaction_type_id = typeId,
                                    interest_rate_id = interestId,
                                    amount = amount,
                                    instalment_tenure_id = instalmentId,
                                    created_by = currentUserId
                                )
                                viewModel.addTransaction(transaction)
                                
                                scope.launch { sheetState.hide() }.invokeOnCompletion {
                                    if (!sheetState.isVisible) {
                                        showBottomSheet = false
                                        // Reset form
                                        selectedContact = null
                                        selectedType = null
                                        selectedInterestRate = null
                                        selectedInstalment = null
                                        amountInput = ""
                                        tenureInput = ""
                                    }
                                }
                            }
                        },
                        modifier = Modifier.fillMaxWidth(),
                        enabled = amountInput.isNotEmpty() && selectedContact != null && selectedType != null && 
                                (selectedType?.id != 2 || selectedInterestRate != null) &&
                                (selectedType?.id != 1 || selectedInstalment != null) &&
                                (selectedType?.id == 2 || selectedType?.id == 1 || tenureInput.isNotEmpty())
                    ) {
                        Text("Submit")
                    }
                }
            }
        }
    }

    if (showContactDialog) {
        SearchableContactDialog(
            contacts = uiState.contacts,
            onContactSelected = {
                selectedContact = it
                showContactDialog = false
            },
            onDismiss = { showContactDialog = false }
        )
    }
}

/**
 * A modal dialog allowing users to search and select a contact from the list.
 *
 * Supports filtering contacts by name or phone number.
 *
 * @param contacts Full list of available [Contact] entities to filter and display.
 * @param onContactSelected Callback triggered with the chosen [Contact].
 * @param onDismiss Callback invoked when the user dismisses the dialog.
 */
@Composable
fun SearchableContactDialog(
    contacts: List<Contact>,
    onContactSelected: (Contact) -> Unit,
    onDismiss: () -> Unit
) {
    var searchQuery by remember { mutableStateOf("") }
    val filteredContacts = remember(searchQuery, contacts) {
        if (searchQuery.isBlank()) {
            contacts
        } else {
            contacts.filter {
                it.name.contains(searchQuery, ignoreCase = true) ||
                (it.phoneNumber?.contains(searchQuery) ?: false)
            }
        }
    }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Select Contact") },
        text = {
            Column(modifier = Modifier.fillMaxWidth()) {
                OutlinedTextField(
                    value = searchQuery,
                    onValueChange = { searchQuery = it },
                    label = { Text("Search Name or Number") },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true
                )
                Spacer(modifier = Modifier.height(16.dp))
                LazyColumn(modifier = Modifier.heightIn(max = 400.dp)) {
                    items(filteredContacts) { contact ->
                        ListItem(
                            headlineContent = { Text(contact.name) },
                            supportingContent = { contact.phoneNumber?.let { Text(it) } },
                            modifier = Modifier.clickable { onContactSelected(contact) }
                        )
                        HorizontalDivider()
                    }
                }
            }
        },
        confirmButton = {
            TextButton(onClick = onDismiss) { Text("Cancel") }
        }
    )
}
