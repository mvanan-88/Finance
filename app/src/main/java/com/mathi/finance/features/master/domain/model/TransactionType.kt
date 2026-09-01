package com.mathi.finance.features.master.domain.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class TransactionType(
    val id: Int? = null,
    val name: String,
    @SerialName("created_at")
    val createdAt: String,
    val status: Int,
)