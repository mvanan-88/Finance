package com.mathi.finance.features.transactions.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mathi.finance.features.contacts.domain.model.Contact
import com.mathi.finance.features.master.domain.model.InterestRates
import com.mathi.finance.features.master.domain.model.TransactionType
import com.mathi.finance.features.master.domain.model.instalment_data
import com.mathi.finance.features.transactions.domain.model.PaymentsModel
import com.mathi.finance.features.transactions.domain.model.PerPersonTransaction
import com.mathi.finance.features.transactions.domain.model.TransactionSummary
import com.mathi.finance.features.transactions.domain.repository.TransactionRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class TransactionUIState(
    val isLoading: Boolean = false,
    val paymentCompleted: Boolean = false,
    val transactions: List<TransactionSummary> = emptyList(),
    val contacts: List<Contact> = emptyList(),
    val paymentHistory: List<PaymentsModel> = emptyList(),
    val transactionTypes: List<TransactionType> = emptyList(),
    val interestRates: List<InterestRates> = emptyList(),
    val instalmentList: List<instalment_data> = emptyList(),
    val error: String? = null
)

class TransactionViewModel(
    private val transactionRepository: TransactionRepository
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
        fetchInstalments()
    }

    fun fetchTransactions() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }
            transactionRepository.fetchTransactions()
                .onSuccess { result ->
                    _uiState.update { it.copy(transactions = result, isLoading = false) }
                }
                .onFailure { e ->
                    _uiState.update { it.copy(error = e.localizedMessage, isLoading = false) }
                }
        }
    }

    fun fetchContacts() {
        viewModelScope.launch {
            transactionRepository.fetchContacts()
                .onSuccess { result ->
                    _uiState.update { it.copy(contacts = result) }
                }
                .onFailure { e ->
                    _uiState.update { it.copy(error = e.localizedMessage) }
                }
        }
    }

    fun fetchTransactionTypes() {
        viewModelScope.launch {
            transactionRepository.fetchTransactionTypes()
                .onSuccess { result ->
                    _uiState.update { it.copy(transactionTypes = result) }
                }
                .onFailure { e ->
                    _uiState.update { it.copy(error = e.localizedMessage) }
                }
        }
    }

    fun fetchInterestRates() {
        viewModelScope.launch {
            transactionRepository.fetchInterestRates()
                .onSuccess { result ->
                    _uiState.update { it.copy(interestRates = result) }
                }
                .onFailure { e ->
                    _uiState.update { it.copy(error = e.localizedMessage) }
                }
        }
    }

    fun fetchInstalments() {
        viewModelScope.launch {
            transactionRepository.fetchInstalments()
                .onSuccess { result ->
                    _uiState.update { it.copy(instalmentList = result) }
                }
                .onFailure { e ->
                    _uiState.update { it.copy(error = e.localizedMessage) }
                }
        }
    }

    fun addTransaction(transaction: PerPersonTransaction) {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }
            transactionRepository.addTransaction(transaction)
                .onSuccess { fetchTransactions() }
                .onFailure { e ->
                    _uiState.update { it.copy(error = e.localizedMessage, isLoading = false) }
                }
        }
    }

    fun makePayment(id: Int, amount_paid: String, note: String) {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }
            transactionRepository.makePayment(id, amount_paid.toFloat(), note)
                .onSuccess {
                    fetchPaymentHistory(id)
                    _uiState.update { it.copy(paymentCompleted = true, isLoading = false) }
                }
                .onFailure { e ->
                    _uiState.update { it.copy(error = e.localizedMessage, isLoading = false) }
                }
        }
    }

    fun fetchPaymentHistory(loanId: Int) {
        viewModelScope.launch {
            transactionRepository.fetchPaymentHistory(loanId)
                .onSuccess { result ->
                    _uiState.update { it.copy(paymentHistory = result) }
                }
                .onFailure { e ->
                    _uiState.update { it.copy(error = e.localizedMessage) }
                }
        }
    }
}
