package com.mathi.finance.features.home.domain.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class TransactionDashboardSummary(
    @SerialName("created_by") val createdBy: Int = 0,
    @SerialName("active_count") val activeCount: Int = 0,
    @SerialName("inactive_count") val inactiveCount: Int = 0,
    @SerialName("lended") val lended: Float = 0f,
    @SerialName("recovered") val recovered: Float = 0f
)
