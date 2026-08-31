package com.mathi.finance.features.master.presentation

import com.mathi.finance.features.master.domain.model.InterestRates
import com.mathi.finance.features.transactions.domain.model.TransactionType

data class MasterUIState(
    val isLoading: Boolean = false,
    val transactionList: List<TransactionType> = emptyList(),
    val interestList: List<InterestRates> = emptyList(),
    val error: String? = null
)
