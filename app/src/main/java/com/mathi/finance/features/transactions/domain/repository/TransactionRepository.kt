package com.mathi.finance.features.transactions.domain.repository

import com.mathi.finance.features.contacts.domain.model.Contact
import com.mathi.finance.features.master.domain.model.InterestRates
import com.mathi.finance.features.master.domain.model.TransactionType
import com.mathi.finance.features.master.domain.model.instalment_data
import com.mathi.finance.features.transactions.domain.model.PaymentsModel
import com.mathi.finance.features.transactions.domain.model.PerPersonTransaction
import com.mathi.finance.features.transactions.domain.model.TransactionSummary

interface TransactionRepository {
    suspend fun fetchTransactions(): Result<List<TransactionSummary>>
    suspend fun fetchContacts(): Result<List<Contact>>
    suspend fun fetchTransactionTypes(): Result<List<TransactionType>>
    suspend fun fetchInterestRates(): Result<List<InterestRates>>
    suspend fun fetchInstalments(): Result<List<instalment_data>>
    suspend fun addTransaction(transaction: PerPersonTransaction): Result<Unit>
    suspend fun makePayment(loanId: Int, amountPaid: Float, notes: String): Result<Unit>
    suspend fun fetchPaymentHistory(loanId: Int): Result<List<PaymentsModel>>
}
