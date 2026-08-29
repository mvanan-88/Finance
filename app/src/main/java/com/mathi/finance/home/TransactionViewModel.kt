package com.mathi.finance.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mathi.finance.dataconnect.FinanceConnector
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class TransactionViewModel : ViewModel() {
    private val _transactions = MutableStateFlow<List<Transaction>>(emptyList())
    val transactions: StateFlow<List<Transaction>> = _transactions.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()
    val connector = FinanceConnector.instance

    fun loadTransactions(userId: String) {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                val result = connector.listTransactions.execute(userId)
                _transactions.value = result.data.transactions
            } catch (e: Exception) {
                e.printStackTrace()
            }
            _isLoading.value = false
        }
    }

    fun addTransaction(amount: Double, description: String, category: String, userId: String) {
        viewModelScope.launch {
            _isLoading.value = true
            connector.addTransaction.execute(amount, description, category, userId)

            _isLoading.value = false
        }
    }
}
