package com.mathi.finance.features.transactions.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mathi.finance.core.network.SupabaseClient
import com.mathi.finance.core.prefs.PreferenceManager
import com.mathi.finance.features.contacts.domain.model.Contact
import com.mathi.finance.features.master.domain.model.InterestRates
import com.mathi.finance.features.master.domain.model.TransactionType
import com.mathi.finance.features.transactions.domain.model.PerPersonTransaction
import io.github.jan.supabase.postgrest.from
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class TransactionUIState(
    val isLoading: Boolean = false,
    val transactions: List<PerPersonTransaction> = emptyList(),
    val contacts: List<Contact> = emptyList(),
    val transactionTypes: List<TransactionType> = emptyList(),
    val interestRates: List<InterestRates> = emptyList(),
    val error: String? = null
)

class TransactionViewModel(
    private val preferenceManager: PreferenceManager
) : ViewModel() {
    private val _uiState = MutableStateFlow(TransactionUIState())
    val uiState: StateFlow<TransactionUIState> = _uiState.asStateFlow()

    init {
        fetchInitialData()
    }

    private fun fetchInitialData() {
        fetchTransactions()
        fetchContacts()
        fetchTransactionTypes()
        fetchInterestRates()
    }

    fun fetchTransactions() {
        val currentUserId = preferenceManager.getUserId()
        if (currentUserId == -1) return

        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }
            try {
                val result = SupabaseClient.client.from("per_person_transaction")
                    .select {
                        filter {
                            eq("created_by", currentUserId)
                        }
                    }
                    .decodeList<PerPersonTransaction>()
                _uiState.update { it.copy(transactions = result, isLoading = false) }
            } catch (e: Exception) {
                _uiState.update { it.copy(error = e.localizedMessage, isLoading = false) }
            }
        }
    }

    fun fetchContacts() {
        viewModelScope.launch {
            try {
                val result = SupabaseClient.client.from("contacts")
                    .select()
                    .decodeList<Contact>()
                _uiState.update { it.copy(contacts = result) }
            } catch (e: Exception) {
                _uiState.update { it.copy(error = e.localizedMessage) }
            }
        }
    }

    fun fetchTransactionTypes() {
        viewModelScope.launch {
            try {
                val result = SupabaseClient.client.from("transaction_type")
                    .select {
                        filter {
                            eq("status", 1)
                        }
                    }
                    .decodeList<TransactionType>()
                _uiState.update { it.copy(transactionTypes = result) }
            } catch (e: Exception) {
                _uiState.update { it.copy(error = e.localizedMessage) }
            }
        }
    }

    fun fetchInterestRates() {
        viewModelScope.launch {
            try {
                val result = SupabaseClient.client.from("interest_rates")
                    .select {
                        filter {
                            eq("status", 1)
                        }
                    }
                    .decodeList<InterestRates>()
                _uiState.update { it.copy(interestRates = result) }
            } catch (e: Exception) {
                _uiState.update { it.copy(error = e.localizedMessage) }
            }
        }
    }

    fun addTransaction(transaction: PerPersonTransaction) {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }
            try {
                SupabaseClient.client.from("per_person_transaction")
                    .insert(transaction)
                fetchTransactions()
            } catch (e: Exception) {
                _uiState.update { it.copy(error = e.localizedMessage, isLoading = false) }
            }
        }
    }
    
    fun getCurrentUserId() = preferenceManager.getUserId()
}
