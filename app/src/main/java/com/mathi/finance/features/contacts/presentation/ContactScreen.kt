package com.mathi.finance.features.contacts.presentation

import android.Manifest
import android.content.pm.PackageManager
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import org.koin.androidx.compose.koinViewModel
import com.mathi.finance.features.contacts.domain.model.Contact

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ContactScreen(
    viewModel: ContactViewModel = koinViewModel()
) {
    val contacts by viewModel.contacts.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()
    val syncStatus by viewModel.syncStatus.collectAsState()
    val context = LocalContext.current
    val snackbarHostState = remember { SnackbarHostState() }

    LaunchedEffect(syncStatus) {
        syncStatus?.let {
            snackbarHostState.showSnackbar(it)
        }
    }

    var hasPermission by remember {
        mutableStateOf(
            ContextCompat.checkSelfPermission(
                context,
                Manifest.permission.READ_CONTACTS
            ) == PackageManager.PERMISSION_GRANTED
        )
    }

    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission(),
        onResult = { isGranted ->
            hasPermission = isGranted
            if (isGranted) {
                viewModel.fetchContacts()
            }
        }
    )

    LaunchedEffect(hasPermission) {
        if (hasPermission) {
            viewModel.fetchContacts()
        }
    }

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) },
        topBar = {
            TopAppBar(
                title = { Text("Contacts") },
                actions = {
                    if (hasPermission) {
                        Button(onClick = { viewModel.syncContacts() }) {
                            Text("Sync")
                        }
                    }
                }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp)
        ) {
            if (!hasPermission) {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Button(onClick = { launcher.launch(Manifest.permission.READ_CONTACTS) }) {
                        Text("Grant Permission")
                    }
                }
            } else if (isLoading) {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator()
                }
            } else {
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(contacts) { contact ->
                        ContactItem(contact)
                    }
                }
            }
        }
    }
}

@Composable
fun ContactItem(contact: Contact) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(text = contact.name, style = MaterialTheme.typography.titleMedium)
            contact.phoneNumber?.let {
                Text(text = it, style = MaterialTheme.typography.bodyMedium)
            }
        }
    }
}
