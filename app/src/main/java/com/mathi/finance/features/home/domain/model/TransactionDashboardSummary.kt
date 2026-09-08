package com.mathi.finance.features.home.domain.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class TransactionDashboardSummary(
    @SerialName("created_by") val createdBy: Int = 0,
    @SerialName("active_loans") val activeCount: Int = 0,
    @SerialName("inactive_loans") val inactiveCount: Int = 0,
    @SerialName("lended") val lended: Float = 0f,
    @SerialName("recovered") val recovered: Float = 0f,
    @SerialName("today_payment") val today: Float = 0f,
    @SerialName("this_week_payment") val this_week: Float = 0f
)
