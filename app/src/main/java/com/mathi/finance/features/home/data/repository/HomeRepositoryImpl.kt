package com.mathi.finance.features.home.data.repository

import com.mathi.finance.core.network.SupabaseClient
import com.mathi.finance.core.prefs.PreferenceManager
import com.mathi.finance.features.home.HomeDashboardBasicData
import com.mathi.finance.features.home.domain.model.TransactionDashboardSummary
import com.mathi.finance.features.home.domain.repository.HomeRepository
import com.mathi.finance.features.transactions.domain.model.TransactionSummary
import io.github.jan.supabase.postgrest.from
import io.github.jan.supabase.postgrest.query.Order

class HomeRepositoryImpl(
    preferenceManager: PreferenceManager
) : HomeRepository {
    private val currentUserId = preferenceManager.getUserId()

    override suspend fun fetchDashboardSummary(): Result<HomeDashboardBasicData?> {
        if (currentUserId == -1) return Result.success(null)
        return try {
            val result = SupabaseClient.client.from("dashboard_summary")
                .select {
                    filter {
                        eq("created_by", currentUserId)
                    }
                }
                .decodeSingleOrNull<HomeDashboardBasicData>()
            Result.success(result)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun fetchTransactionDashboardSummary(): Result<TransactionDashboardSummary?> {
        if (currentUserId == -1) return Result.success(null)
        return try {
            val result = SupabaseClient.client.from("dashboard_summary")
                .select {
                    filter {
                        eq("created_by", currentUserId)
                    }
                }
                .decodeSingleOrNull<TransactionDashboardSummary>()
            Result.success(result)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun fetchUrgentActions(): Result<List<TransactionSummary>> {
        if (currentUserId == -1) return Result.success(emptyList())
        return try {
            val result = SupabaseClient.client.from("transaction_summary_view")
                .select {
                    filter {
                        eq("created_by", currentUserId)
                    }
                    order(column = "risk_level", order = Order.DESCENDING)
                    limit(5)
                }
                .decodeList<TransactionSummary>()
            Result.success(result)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
