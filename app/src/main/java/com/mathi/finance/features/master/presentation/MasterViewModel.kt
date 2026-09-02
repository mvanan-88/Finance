package com.mathi.finance.features.master.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mathi.finance.core.network.SupabaseClient
import com.mathi.finance.features.master.domain.model.InterestRates
import com.mathi.finance.features.master.domain.model.TransactionType
import com.mathi.finance.features.master.domain.model.instalment_data
import io.github.jan.supabase.postgrest.from
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class MasterViewModel : ViewModel() {

    private val _uiState = MutableStateFlow(MasterUIState())
    val listState: StateFlow<MasterUIState> = _uiState.asStateFlow()


    fun fetchTransactions() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }
            try {
                val result = SupabaseClient.client.from("transaction_type")
                    .select()
                    .decodeList<TransactionType>()
                _uiState.update {
                   it.copy(
                       transactionList = result as ArrayList<TransactionType>,
                       isLoading = false
                   )
                }
            } catch (e: Exception) {
                _uiState.update { it.copy(isLoading = false, error = e.localizedMessage) }
            }
        }
    }

    fun addTransaction(transaction: TransactionType) {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }
            try {
                SupabaseClient.client.from("transaction_type")
                    .insert(transaction)
                fetchTransactions()
            } catch (e: Exception) {
                _uiState.update { it.copy(isLoading = false, error = e.localizedMessage) }
            }
        }
    }

    fun updateTransaction(transaction: TransactionType) {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }
            try {
                SupabaseClient.client.from("transaction_type")
                    .update(transaction) {
                        filter {
                            eq("id", transaction.id ?: 0)
                        }
                    }
                fetchTransactions()
            } catch (e: Exception) {
                _uiState.update { it.copy(isLoading = false, error = e.localizedMessage) }
            }
        }
    }
    fun fetchInterests() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }
            try {
                val result = SupabaseClient.client.from("interest_rates")
                    .select()
                    .decodeList<InterestRates>()
                _uiState.update {
                   it.copy(
                       interestList = result as ArrayList<InterestRates>,
                       isLoading = false
                   )
                }
            } catch (e: Exception) {
                _uiState.update { it.copy(isLoading = false, error = e.localizedMessage) }
            }
        }
    }

    fun addInterest(interest: InterestRates) {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }
            try {
                SupabaseClient.client.from("interest_rates")
                    .insert(interest)
                fetchInterests()
            } catch (e: Exception) {
                _uiState.update { it.copy(isLoading = false, error = e.localizedMessage) }
            }
        }
    }

    fun updateInterest(interest: InterestRates) {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }
            try {
                SupabaseClient.client.from("interest_rates")
                    .update(interest) {
                        filter {
                            eq("id", interest.id ?: 0)
                        }
                    }
                fetchInterests()
            } catch (e: Exception) {
                _uiState.update { it.copy(isLoading = false, error = e.localizedMessage) }
            }
        }
    }
    fun fetchInstalment() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }
            try {
                val result = SupabaseClient.client.from("installment_tenure")
                    .select()
                    .decodeList<instalment_data>()
                _uiState.update {
                   it.copy(
                       instalmentList = result as ArrayList<instalment_data>,
                       isLoading = false
                   )
                }
            } catch (e: Exception) {
                _uiState.update { it.copy(isLoading = false, error = e.localizedMessage) }
            }
        }
    }

    fun addInstalment(interest: instalment_data) {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }
            try {
                SupabaseClient.client.from("installment_tenure")
                    .insert(interest)
                fetchInstalment()
            } catch (e: Exception) {
                _uiState.update { it.copy(isLoading = false, error = e.localizedMessage) }
            }
        }
    }

    fun updateInstalment(interest: instalment_data) {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }
            try {
                SupabaseClient.client.from("installment_tenure")
                    .update(interest) {
                        filter {
                            eq("id", interest.id ?: 0)
                        }
                    }
                fetchInstalment()
            } catch (e: Exception) {
                _uiState.update { it.copy(isLoading = false, error = e.localizedMessage) }
            }
        }
    }
}