package com.mathi.finance.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class TransactionViewModel : ViewModel() {
    private val _transactions = MutableStateFlow<List<Transaction>>(emptyList())
    val transactions: StateFlow<List<Transaction>> = _transactions.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    fun loadTransactions(userId: String) {
        viewModelScope.launch {
            _isLoading.value = true
            // TODO: Use generated Data Connect SDK here
//             val result = connector.listTransactions.execute(userId)
//             _transactions.value = result.data.transactions.map { ... }
            
            // For now, using mock data until SDK is generated
            _transactions.value = listOf(
                Transaction(amount = 50.0, description = "Lunch", category = "Food", userId = userId),
                Transaction(amount = 20.0, description = "Bus", category = "Transport", userId = userId)
            )
            _isLoading.value = false
        }
    }

    fun addTransaction(amount: Double, description: String, category: String, userId: String) {
        viewModelScope.launch {
            _isLoading.value = true

            val newTransaction = Transaction(amount = amount, description = description, category = category, userId = userId)
            _transactions.value = _transactions.value + newTransaction
            _isLoading.value = false
        }
    }
}
