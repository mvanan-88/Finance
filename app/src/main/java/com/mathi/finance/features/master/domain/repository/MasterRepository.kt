package com.mathi.finance.features.master.domain.repository

import com.mathi.finance.features.master.domain.model.InterestRates
import com.mathi.finance.features.master.domain.model.TransactionType
import com.mathi.finance.features.master.domain.model.instalment_data

interface MasterRepository {
    suspend fun fetchTransactionTypes(): Result<List<TransactionType>>
    suspend fun addTransactionType(transactionType: TransactionType): Result<Unit>
    suspend fun updateTransactionType(transactionType: TransactionType): Result<Unit>

    suspend fun fetchInterestRates(): Result<List<InterestRates>>
    suspend fun addInterestRate(interestRate: InterestRates): Result<Unit>
    suspend fun updateInterestRate(interestRate: InterestRates): Result<Unit>

    suspend fun fetchInstalmentTenures(): Result<List<instalment_data>>
    suspend fun addInstalmentTenure(instalment: instalment_data): Result<Unit>
    suspend fun updateInstalmentTenure(instalment: instalment_data): Result<Unit>
}
