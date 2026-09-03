package com.mathi.finance.features.transactions.domain.model

import kotlinx.serialization.Serializable

@Serializable
data class TransactionSummary(
    val id: Int,
    val created_at: String,
    val amount: Float,
    val amount_paid: Float,
    val tenure: Int? = 0,
    val days_difference: Int = 0,
    val interest_rate: Int? = 0,
    val total_terms_paid: Int? = 0,
    val risk_level: Int? = 0,
    val transaction_type: String,
    val name: String,
    val created_by: Int,
)
