package com.mathi.finance

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ReceiptLong
import androidx.compose.material.icons.filled.ContactPage
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.ReceiptLong
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.mathi.finance.core.theme.MyFinanceTheme
import com.mathi.finance.features.auth.presentation.LoginScreen
import com.mathi.finance.features.auth.presentation.LoginViewModel
import com.mathi.finance.features.contacts.presentation.ContactScreen
import com.mathi.finance.features.home.HomeScreen
import com.mathi.finance.features.master.presentation.MasterScreen
import com.mathi.finance.features.transactions.presentation.TransactionScreen
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
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(
                                brush = Brush.verticalGradient(
                                    colors = listOf(
                                        MaterialTheme.colorScheme.background,
                                        MaterialTheme.colorScheme.primary.copy(alpha = 0.1f)
                                    )
                                )
                            )
                    ) {
                        Scaffold(
                            modifier = Modifier.fillMaxSize(),
                            containerColor = Color.Transparent,
                            bottomBar = {
                                NavigationBar(
                                    containerColor = MaterialTheme.colorScheme.surface,
                                    tonalElevation = 8.dp
                                ) {
                                    NavigationBarItem(
                                        selected = currentScreen == Screen.Home,
                                        onClick = { currentScreen = Screen.Home },
                                        icon = {
                                            Icon(
                                                Icons.Filled.Home,
                                                contentDescription = null
                                            )
                                        },
                                        label = { Text("Home") }
                                    )
                                    NavigationBarItem(
                                        selected = currentScreen == Screen.Transactions,
                                        onClick = { currentScreen = Screen.Transactions },
                                        icon = {
                                            Icon(
                                                Icons.AutoMirrored.Filled.ReceiptLong,
                                                contentDescription = null
                                            )
                                        },
                                        label = { Text("Transactions") }
                                    )
                                    NavigationBarItem(
                                        selected = currentScreen == Screen.Contacts,
                                        onClick = { currentScreen = Screen.Contacts },
                                        icon = {
                                            Icon(
                                                Icons.Default.ContactPage,
                                                contentDescription = null
                                            )
                                        },
                                        label = { Text("Contacts") }
                                    )
                                    NavigationBarItem(
                                        selected = currentScreen == Screen.Master,
                                        onClick = { currentScreen = Screen.Master },
                                        icon = {
                                            Icon(
                                                Icons.Default.Settings,
                                                contentDescription = null
                                            )
                                        },
                                        label = { Text("Master SetUp") }
                                    )
                                }
                            }
                        ) { innerPadding ->
                            Box(modifier = Modifier.padding(innerPadding)) {
                                when (currentScreen) {
                                    Screen.Transactions -> TransactionScreen(onSignOut = { loginViewModel.logout() })
                                    Screen.Contacts -> ContactScreen()
                                    Screen.Master -> MasterScreen(onSignOut = { loginViewModel.logout() })
                                    Screen.Home -> HomeScreen(onSignOut = { loginViewModel.logout() })
                                }
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
