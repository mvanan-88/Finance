package com.mathi.finance.features.transactions.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mathi.finance.features.transactions.domain.model.TransactionSummary
import com.mathi.finance.features.transactions.domain.repository.TransactionRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class CollectionLogViewModel(
    private val repository: TransactionRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(CollectionLogUiState())
    val uiState: StateFlow<CollectionLogUiState> = _uiState.asStateFlow()

    init {
        fetchLogs()
    }

    fun fetchLogs() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }
            repository.fetchCollectionLogs()
                .onSuccess { logs ->
                    _uiState.update {
                        it.copy(
                            logs = logs.sortedByDescending { log -> log.created_at },
                            isLoading = false,
                            error = null
                        )
                    }
                }
                .onFailure { error ->
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            error = error.localizedMessage ?: "Failed to fetch logs"
                        )
                    }
                }
        }
    }
}

data class CollectionLogUiState(
    val logs: List<TransactionSummary> = emptyList(),
    val isLoading: Boolean = false,
    val error: String? = null
)
