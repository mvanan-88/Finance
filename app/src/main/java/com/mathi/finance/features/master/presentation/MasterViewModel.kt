package com.mathi.finance.features.master.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mathi.finance.features.master.domain.model.InterestRates
import com.mathi.finance.features.master.domain.model.TransactionType
import com.mathi.finance.features.master.domain.model.instalment_data
import com.mathi.finance.features.master.domain.repository.MasterRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class MasterViewModel(private val masterRepository: MasterRepository) : ViewModel() {

    private val _uiState = MutableStateFlow(MasterUIState())
    val listState: StateFlow<MasterUIState> = _uiState.asStateFlow()

    fun fetchTransactions() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }
            masterRepository.fetchTransactionTypes()
                .onSuccess { result ->
                    _uiState.update {
                        it.copy(
                            transactionList = result as ArrayList<TransactionType>,
                            isLoading = false
                        )
                    }
                }
                .onFailure { e ->
                    _uiState.update { it.copy(isLoading = false, error = e.localizedMessage) }
                }
        }
    }

    fun addTransaction(transaction: TransactionType) {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }
            masterRepository.addTransactionType(transaction)
                .onSuccess { fetchTransactions() }
                .onFailure { e ->
                    _uiState.update { it.copy(isLoading = false, error = e.localizedMessage) }
                }
        }
    }

    fun updateTransaction(transaction: TransactionType) {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }
            masterRepository.updateTransactionType(transaction)
                .onSuccess { fetchTransactions() }
                .onFailure { e ->
                    _uiState.update { it.copy(isLoading = false, error = e.localizedMessage) }
                }
        }
    }

    fun fetchInterests() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }
            masterRepository.fetchInterestRates()
                .onSuccess { result ->
                    _uiState.update {
                        it.copy(
                            interestList = result as ArrayList<InterestRates>,
                            isLoading = false
                        )
                    }
                }
                .onFailure { e ->
                    _uiState.update { it.copy(isLoading = false, error = e.localizedMessage) }
                }
        }
    }

    fun addInterest(interest: InterestRates) {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }
            masterRepository.addInterestRate(interest)
                .onSuccess { fetchInterests() }
                .onFailure { e ->
                    _uiState.update { it.copy(isLoading = false, error = e.localizedMessage) }
                }
        }
    }

    fun updateInterest(interest: InterestRates) {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }
            masterRepository.updateInterestRate(interest)
                .onSuccess { fetchInterests() }
                .onFailure { e ->
                    _uiState.update { it.copy(isLoading = false, error = e.localizedMessage) }
                }
        }
    }

    fun fetchInstalment() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }
            masterRepository.fetchInstalmentTenures()
                .onSuccess { result ->
                    _uiState.update {
                        it.copy(
                            instalmentList = result as ArrayList<instalment_data>,
                            isLoading = false
                        )
                    }
                }
                .onFailure { e ->
                    _uiState.update { it.copy(isLoading = false, error = e.localizedMessage) }
                }
        }
    }

    fun addInstalment(interest: instalment_data) {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }
            masterRepository.addInstalmentTenure(interest)
                .onSuccess { fetchInstalment() }
                .onFailure { e ->
                    _uiState.update { it.copy(isLoading = false, error = e.localizedMessage) }
                }
        }
    }

    fun updateInstalment(interest: instalment_data) {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }
            masterRepository.updateInstalmentTenure(interest)
                .onSuccess { fetchInstalment() }
                .onFailure { e ->
                    _uiState.update { it.copy(isLoading = false, error = e.localizedMessage) }
                }
        }
    }
}
