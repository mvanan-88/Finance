package com.mathi.finance

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ContactPage
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.ReceiptLong
import androidx.compose.material.icons.filled.Settings
import com.mathi.finance.core.theme.MyFinanceTheme
import com.mathi.finance.features.auth.presentation.LoginScreen
import com.mathi.finance.features.auth.presentation.LoginViewModel
import com.mathi.finance.features.contacts.presentation.ContactScreen
import com.mathi.finance.features.master.presentation.MasterScreen
import com.mathi.finance.features.transactions.presentation.TransactionScreen
import com.mathi.finance.features.home.HomeScreen
import org.koin.androidx.compose.koinViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MyFinanceTheme {
                val loginViewModel: LoginViewModel = koinViewModel()
                val authState by loginViewModel.uiState.collectAsState()
                var currentScreen by remember { mutableStateOf(Screen.Home) }

                if (authState.user == null) {
                    LoginScreen(
                        viewModel = loginViewModel,
                        onLoginSuccess = { /* State will update and show main app */ }
                    )
                } else {
                    Scaffold(
                        modifier = Modifier.fillMaxSize(),
                        bottomBar = {
                            NavigationBar {
                                NavigationBarItem(
                                    selected = currentScreen == Screen.Home,
                                    onClick = { currentScreen = Screen.Home },
                                    icon = { Icon(Icons.Filled.Home, contentDescription = null) },
                                    label = { Text("Home") }
                                )
                                NavigationBarItem(
                                    selected = currentScreen == Screen.Transactions,
                                    onClick = { currentScreen = Screen.Transactions },
                                    icon = { Icon(Icons.Default.ReceiptLong, contentDescription = null) },
                                    label = { Text("Transactions") }
                                )
                                NavigationBarItem(
                                    selected = currentScreen == Screen.Contacts,
                                    onClick = { currentScreen = Screen.Contacts },
                                    icon = { Icon(Icons.Default.ContactPage, contentDescription = null) },
                                    label = { Text("Contacts") }
                                )
                                NavigationBarItem(
                                    selected = currentScreen == Screen.Master,
                                    onClick = { currentScreen = Screen.Master },
                                    icon = { Icon(Icons.Default.Settings, contentDescription = null) },
                                    label = { Text("Master SetUp") }
                                )
                            }
                        }
                    ) { innerPadding ->
                        Box(modifier = Modifier.padding(innerPadding)) {
                            when (currentScreen) {
                                Screen.Transactions -> TransactionScreen()
                                Screen.Contacts -> ContactScreen()
                                Screen.Master -> MasterScreen()
                                Screen.Home -> HomeScreen()
                            }
                        }
                    }
                }
            }
        }
    }
}

enum class Screen {
    Transactions, Contacts, Master, Home
}
