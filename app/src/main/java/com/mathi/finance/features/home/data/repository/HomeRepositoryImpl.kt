package com.mathi.finance.features.home.data.repository

import com.mathi.finance.core.network.SupabaseClient
import com.mathi.finance.core.prefs.PreferenceManager
import com.mathi.finance.features.home.HomeDashboardBasicData
import com.mathi.finance.features.home.domain.repository.HomeRepository
import io.github.jan.supabase.postgrest.from

class HomeRepositoryImpl(
    private val preferenceManager: PreferenceManager
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
}
