package com.mathi.finance.features.transactions.domain.model

import kotlinx.serialization.Serializable

@Serializable
data class PerPersonTransaction(
    val id: Int? = null,
    val created_at: String,
    val contact_id: Int,
    val transaction_type_id: Int,
    val interest_rate_id: Int? = null,
    val amount: Float,
    val created_by: Int,
)
