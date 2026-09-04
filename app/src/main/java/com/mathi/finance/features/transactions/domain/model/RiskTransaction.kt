package com.mathi.finance.features.transactions.domain.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class RiskTransaction(
    val id: Int,
    val name: String,
    val amount: Float,
    @SerialName("days_difference") val daysDifference: Int,
    @SerialName("risk_level_text") val riskLevel: String, // "High", "Medium", "Low"
    @SerialName("created_by") val createdBy: Int
)
