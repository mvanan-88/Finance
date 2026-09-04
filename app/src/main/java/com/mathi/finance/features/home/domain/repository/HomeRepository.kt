package com.mathi.finance.features.home.domain.repository

import com.mathi.finance.features.home.HomeDashboardBasicData

interface HomeRepository {
    suspend fun fetchDashboardSummary(): Result<HomeDashboardBasicData?>
}
