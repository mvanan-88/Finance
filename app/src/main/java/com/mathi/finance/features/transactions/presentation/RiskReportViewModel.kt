package com.mathi.finance.features.transactions.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mathi.finance.features.transactions.domain.model.RiskTransaction
import com.mathi.finance.features.transactions.domain.repository.TransactionRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class RiskReportViewModel(
    private val repository: TransactionRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(RiskReportUiState())
    val uiState: StateFlow<RiskReportUiState> = _uiState.asStateFlow()

    init {
        fetchRiskTransactions()
    }

    fun fetchRiskTransactions() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }
            repository.fetchRiskTransactions()
                .onSuccess { transactions ->
                    _uiState.update { 
                        it.copy(
                            transactions = transactions,
                            isLoading = false,
                            error = null
                        ) 
                    }
                }
                .onFailure { error ->
                    _uiState.update { 
                        it.copy(
                            isLoading = false,
                            error = error.localizedMessage ?: "Failed to fetch risk reports"
                        ) 
                    }
                }
        }
    }
}

data class RiskReportUiState(
    val transactions: List<RiskTransaction> = emptyList(),
    val isLoading: Boolean = false,
    val error: String? = null
)
