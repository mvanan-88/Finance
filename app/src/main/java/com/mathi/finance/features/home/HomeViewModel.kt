package com.mathi.finance.features.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mathi.finance.core.network.SupabaseClient
import com.mathi.finance.core.prefs.PreferenceManager
import com.mathi.finance.features.transactions.presentation.TransactionViewModel
import io.github.jan.supabase.postgrest.from
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class HomeViewModel(
    preferenceManager: PreferenceManager,
    private val transactionViewModel: TransactionViewModel
) : ViewModel() {
    val currentUser = preferenceManager.getUserId()
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
        if(currentUser == -1) return
        viewModelScope.launch {
            try {
                val result = SupabaseClient.client.from("dashboard_summary")
                    .select {
                        filter {
                            eq("created_by", currentUser)
                        }
                    }
                    .decodeSingleOrNull<HomeDashboardBasicData>()
                _uiState.update { it.copy( summary =  result) }
            } catch (e: Exception) {
                _uiState.update { it.copy(error = e.localizedMessage) }
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
