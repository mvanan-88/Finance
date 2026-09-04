package com.mathi.finance.features.home.domain.repository

import com.mathi.finance.features.home.HomeDashboardBasicData
import com.mathi.finance.features.home.domain.model.TransactionDashboardSummary
import com.mathi.finance.features.transactions.domain.model.TransactionSummary

interface HomeRepository {
    suspend fun fetchDashboardSummary(): Result<HomeDashboardBasicData?>
    suspend fun fetchTransactionDashboardSummary(): Result<TransactionDashboardSummary?>
    suspend fun fetchUrgentActions(): Result<List<TransactionSummary>>
}
