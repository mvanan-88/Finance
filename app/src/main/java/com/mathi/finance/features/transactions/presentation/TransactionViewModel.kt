package com.mathi.finance.features.transactions.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mathi.finance.core.network.SupabaseClient
import com.mathi.finance.core.prefs.PreferenceManager
import com.mathi.finance.features.contacts.domain.model.Contact
import com.mathi.finance.features.master.domain.model.InterestRates
import com.mathi.finance.features.master.domain.model.TransactionType
import com.mathi.finance.features.master.domain.model.instalment_data
import com.mathi.finance.features.transactions.domain.model.PerPersonTransaction
import com.mathi.finance.features.transactions.domain.model.TransactionDetails
import io.github.jan.supabase.postgrest.from
import io.github.jan.supabase.postgrest.query.Columns
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class TransactionUIState(
    val isLoading: Boolean = false,
    val transactions: List<TransactionDetails> = emptyList(),
    val contacts: List<Contact> = emptyList(),
    val transactionTypes: List<TransactionType> = emptyList(),
    val interestRates: List<InterestRates> = emptyList(),
    val instalmentList: List<instalment_data> = emptyList(),
    val error: String? = null
)

class TransactionViewModel(
    private val preferenceManager: PreferenceManager
) : ViewModel() {
    private val _uiState = MutableStateFlow(TransactionUIState())
    val uiState: StateFlow<TransactionUIState> = _uiState.asStateFlow()
    val currentUserId = preferenceManager.getUserId()
    init {
        fetchInitialData()
    }

    private fun fetchInitialData() {
        fetchTransactions()
        fetchContacts()
        fetchTransactionTypes()
        fetchInterestRates()
        fetchInstalments()
    }

    fun fetchTransactions() {
        
        if (currentUserId == -1) return

        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }
            try {
                // Postgrest Join: fetches related data from linked tables
                val result = SupabaseClient.client.from("per_person_transaction")
                    .select(columns = Columns.raw("*, transaction_type(name), interest_rates(interest_rate), installment_tenure(tenure), contacts(name)")) {
                        filter {
                            eq("created_by", currentUserId)
                        }
                    }
                    .decodeList<TransactionDetails>()
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
                    .select {
                        filter {
                            eq("created_by", currentUserId)
                        }
                    }
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

    fun fetchInstalments() {
        viewModelScope.launch {
            try {
                val result = SupabaseClient.client.from("installment_tenure")
                    .select {
                        filter {
                            eq("status", 1)
                        }
                    }
                    .decodeList<instalment_data>()
                _uiState.update { it.copy(instalmentList = result) }
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
}
