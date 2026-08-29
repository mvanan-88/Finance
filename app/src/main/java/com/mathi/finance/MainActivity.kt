package com.mathi.finance

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.mathi.finance.ui.theme.MyFinanceTheme
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Button
import androidx.compose.ui.unit.dp
import com.mathi.finance.home.HomeScreen
import com.mathi.finance.home.TransactionScreen

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val authManager = AuthManager(this)
        enableEdgeToEdge()
        setContent {
            MyFinanceTheme {
                val viewModel: AuthViewModel = viewModel(
                    factory = AuthViewModelFactory(authManager)
                )
                val authState by viewModel.authState.collectAsState()

                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    when (val state = authState) {
                        is AuthState.Success -> {
                            if (state.user != null) {
                                TransactionScreen(
                                    userId = state.user.uid
                                )
                            } else {
                                LoginScreen(
                                    authViewModel = viewModel,
                                    onLoginSuccess = { /* Handled by collectAsState */ }
                                )
                            }
                        }
                        else -> {
                            LoginScreen(
                                authViewModel = viewModel,
                                onLoginSuccess = { /* Handled by collectAsState */ }
                            )
                        }
                    }
                }
            }
        }
    }
}
