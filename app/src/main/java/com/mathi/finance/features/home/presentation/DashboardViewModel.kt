package com.mathi.finance.features.home.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mathi.finance.features.home.domain.model.TransactionDashboardSummary
import com.mathi.finance.features.home.domain.repository.HomeRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class DashboardViewModel(
    private val repository: HomeRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(DashboardUiState())
    val uiState: StateFlow<DashboardUiState> = _uiState.asStateFlow()

    init {
        fetchDashboardData()
    }

    fun fetchDashboardData() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }
            repository.fetchTransactionDashboardSummary()
                .onSuccess { data ->
                    _uiState.update { 
                        it.copy(
                            summary = data,
                            isLoading = false,
                            error = null
                        ) 
                    }
                }
                .onFailure { error ->
                    _uiState.update { 
                        it.copy(
                            isLoading = false,
                            error = error.localizedMessage ?: "Failed to fetch dashboard data"
                        ) 
                    }
                }
        }
    }
}

data class DashboardUiState(
    val summary: TransactionDashboardSummary? = null,
    val isLoading: Boolean = false,
    val error: String? = null
)
