package com.mathi.finance.features.auth.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mathi.finance.core.network.SupabaseClient
import com.mathi.finance.core.prefs.PreferenceManager
import com.mathi.finance.features.auth.domain.model.User
import io.github.jan.supabase.postgrest.from
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class LoginViewModel(private val preferenceManager: PreferenceManager) : ViewModel() {

    private val _uiState = MutableStateFlow(LoginUIState())
    val uiState: StateFlow<LoginUIState> = _uiState.asStateFlow()

    init {
        checkAutoLogin()
    }

    private fun checkAutoLogin() {
        val savedUserId = preferenceManager.getUserId()
        if (savedUserId != -1) {
            viewModelScope.launch {
                _uiState.update { it.copy(isLoading = true) }
                try {
                    val user = SupabaseClient.client.from("users").select {
                        filter {
                            eq("id", savedUserId)
                        }
                    }.decodeSingleOrNull<User>()

                    if (user != null) {
                        _uiState.update { it.copy(user = user, isLoading = false) }
                    } else {
                        preferenceManager.clear()
                        _uiState.update { it.copy(isLoading = false) }
                    }
                } catch (e: Exception) {
                    _uiState.update { it.copy(isLoading = false) }
                }
            }
        }
    }

    fun login(username: String, password: String) {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, error = null) }
            try {
                // Authenticating against a custom 'users' table as requested
                val user = SupabaseClient.client.from("users").select {
                    filter {
                        eq("user_name", username)
                        eq("password", password)
                    }
                }.decodeSingleOrNull<User>()

                if (user != null) {
                    user.id?.let { preferenceManager.saveUserId(it) }
                    _uiState.update { it.copy(user = user, isLoading = false) }
                } else {
                    _uiState.update {
                        it.copy(
                            error = "Invalid username or password",
                            isLoading = false
                        )
                    }
                }
            } catch (e: Exception) {
                _uiState.update {
                    it.copy(
                        error = e.localizedMessage ?: "An error occurred",
                        isLoading = false
                    )
                }
            }
        }
    }

    fun logout() {
        viewModelScope.launch {
            preferenceManager.clear()
            _uiState.update { LoginUIState() }
        }
    }
}
