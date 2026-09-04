package com.mathi.finance.features.transactions.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mathi.finance.features.transactions.domain.model.PaymentsModel
import com.mathi.finance.features.transactions.domain.model.TransactionSummary
import com.mathi.finance.features.transactions.domain.repository.TransactionRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class BorrowerLedgerViewModel(
    private val repository: TransactionRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(BorrowerLedgerUiState())
    val uiState: StateFlow<BorrowerLedgerUiState> = _uiState.asStateFlow()

    fun loadLedger(transaction: TransactionSummary) {
        _uiState.update { it.copy(transaction = transaction, isLoading = true) }
        viewModelScope.launch {
            repository.fetchPaymentHistory(transaction.id)
                .onSuccess { history ->
                    _uiState.update { 
                        it.copy(
                            paymentHistory = history.sortedByDescending { it.created_at },
                            isLoading = false,
                            error = null
                        ) 
                    }
                }
                .onFailure { error ->
                    _uiState.update { 
                        it.copy(
                            isLoading = false,
                            error = error.localizedMessage ?: "Failed to fetch ledger"
                        ) 
                    }
                }
        }
    }
}

data class BorrowerLedgerUiState(
    val transaction: TransactionSummary? = null,
    val paymentHistory: List<PaymentsModel> = emptyList(),
    val isLoading: Boolean = false,
    val error: String? = null
)
