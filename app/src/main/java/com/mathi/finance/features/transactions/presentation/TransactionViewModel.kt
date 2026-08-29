package com.mathi.finance.features.transactions.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mathi.finance.core.network.SupabaseClient
import com.mathi.finance.features.transactions.domain.model.TransactionType
import io.github.jan.supabase.postgrest.from
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class TransactionViewModel : ViewModel() {
    private val _transactions = MutableStateFlow<List<TransactionType>>(emptyList())
    val transactions: StateFlow<List<TransactionType>> = _transactions

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error

    fun fetchTransactions() {
        viewModelScope.launch {
            try {
                val result = SupabaseClient.client.from("transaction_type")
                    .select()
                    .decodeList<TransactionType>()
                _transactions.value = result
            } catch (e: Exception) {
                _error.value = e.localizedMessage
            }
        }
    }

    fun addTransaction(transaction: TransactionType) {
        viewModelScope.launch {
            try {
                SupabaseClient.client.from("transactions")
                    .insert(transaction)
                fetchTransactions()
            } catch (e: Exception) {
                _error.value = e.localizedMessage
            }
        }
    }
}
