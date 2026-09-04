package com.mathi.finance.features.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mathi.finance.features.home.domain.repository.HomeRepository
import com.mathi.finance.features.transactions.presentation.TransactionViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class HomeViewModel(
    private val homeRepository: HomeRepository,
    private val transactionViewModel: TransactionViewModel
) : ViewModel() {
    private val _uiState = MutableStateFlow(HomeUIState())
    val uiState: StateFlow<HomeUIState> = _uiState.asStateFlow()

    init {
        fetchIncomeExpense()
        observeTransactions()
    }

    private fun observeTransactions() {
        viewModelScope.launch {
            transactionViewModel.uiState.collect { state ->
                val topDebtors = state.transactions
                    .groupBy { it.name }
                    .map { (name, transactions) ->
                        Debtor(name, transactions.sumOf { it.amount.toDouble() }.toFloat())
                    }
                    .sortedByDescending { it.amount }
                    .take(5)

                _uiState.update { it.copy(topDebtors = topDebtors) }
            }
        }
    }

    fun fetchIncomeExpense() {
        viewModelScope.launch {
            homeRepository.fetchDashboardSummary()
                .onSuccess { result ->
                    _uiState.update { it.copy(summary = result) }
                }
                .onFailure { e ->
                    _uiState.update { it.copy(error = e.localizedMessage ?: "Unknown error") }
                }
        }
    }
}

data class HomeUIState(
    val summary: HomeDashboardBasicData? = null,
    val error: String = "",
    val topDebtors: List<Debtor> = emptyList()
)

data class Debtor(val name: String, val amount: Float)
