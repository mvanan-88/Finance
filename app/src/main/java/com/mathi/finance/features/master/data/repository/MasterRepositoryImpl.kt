package com.mathi.finance.features.master.data.repository

import com.mathi.finance.core.network.SupabaseClient
import com.mathi.finance.core.prefs.PreferenceManager
import com.mathi.finance.features.master.domain.model.InterestRates
import com.mathi.finance.features.master.domain.model.TransactionType
import com.mathi.finance.features.master.domain.model.instalment_data
import com.mathi.finance.features.master.domain.repository.MasterRepository
import io.github.jan.supabase.postgrest.from

class MasterRepositoryImpl(
    preferenceManager: PreferenceManager
) : MasterRepository {
    private val currentUserId = preferenceManager.getUserId()

    override suspend fun fetchTransactionTypes(): Result<List<TransactionType>> {
        return try {
            val result = SupabaseClient.client.from("transaction_type")
                .select {
                    filter { eq("created_by", currentUserId) }
                }
                .decodeList<TransactionType>()
            Result.success(result)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun addTransactionType(transactionType: TransactionType): Result<Unit> {
        transactionType.copy(created_by = currentUserId)
        return try {
            SupabaseClient.client.from("transaction_type")
                .insert(transactionType)
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun updateTransactionType(transactionType: TransactionType): Result<Unit> {
        return try {
            SupabaseClient.client.from("transaction_type")
                .update(transactionType) {
                    filter {
                        eq("id", transactionType.id ?: 0)
                    }
                }
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun fetchInterestRates(): Result<List<InterestRates>> {
        return try {
            val result = SupabaseClient.client.from("interest_rates")
                .select {
                    filter { eq("created_by", currentUserId) }
                }
                .decodeList<InterestRates>()
            Result.success(result)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun addInterestRate(interestRate: InterestRates): Result<Unit> {
        interestRate.copy(created_by = currentUserId)
        return try {
            val interestWithUser = interestRate.copy(created_by = currentUserId)
            SupabaseClient.client.from("interest_rates")
                .insert(interestWithUser)
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun updateInterestRate(interestRate: InterestRates): Result<Unit> {
        return try {
            SupabaseClient.client.from("interest_rates")
                .update(interestRate) {
                    filter {
                        eq("id", interestRate.id ?: 0)
                    }
                }
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun fetchInstalmentTenures(): Result<List<instalment_data>> {
        return try {
            val result = SupabaseClient.client.from("installment_tenure")
                .select {
                    filter { eq("created_by", currentUserId) }
                }
                .decodeList<instalment_data>()
            Result.success(result)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun addInstalmentTenure(instalment: instalment_data): Result<Unit> {
        return try {
            val instalmentWithUser = instalment.copy(created_by = currentUserId)
            SupabaseClient.client.from("installment_tenure")
                .insert(instalmentWithUser)
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun updateInstalmentTenure(instalment: instalment_data): Result<Unit> {
        return try {
            SupabaseClient.client.from("installment_tenure")
                .update(instalment) {
                    filter {
                        eq("id", instalment.id ?: 0)
                    }
                }
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
