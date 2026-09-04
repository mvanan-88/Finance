package com.mathi.finance.features.transactions.domain.model

import kotlinx.serialization.Serializable

@Serializable
data class PaymentsModel(
    val id: Int? = null,
    val loan_id: Int,
    val amount_paid: Float,
    val notes: String?,
    val created_at: String = "",
    val created_by: Int?
)
