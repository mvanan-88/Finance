package com.mathi.finance.features.master.presentation

import com.mathi.finance.features.master.domain.model.InterestRates
import com.mathi.finance.features.master.domain.model.TransactionType
import com.mathi.finance.features.master.domain.model.instalment_data

data class MasterUIState(
    val isLoading: Boolean = false,
    val transactionList: List<TransactionType> = emptyList(),
    val interestList: List<InterestRates> = emptyList(),
    val instalmentList: List<instalment_data> = emptyList(),
    val error: String? = null
)
