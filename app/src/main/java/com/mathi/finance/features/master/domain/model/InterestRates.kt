package com.mathi.finance.features.master.domain.model

import kotlinx.serialization.Serializable

@Serializable
data class InterestRates(
    val id: Int?=null,
    val created_at: String,
    val interest_rate : Float,
    val status: Int,
    val created_by: Int
)
