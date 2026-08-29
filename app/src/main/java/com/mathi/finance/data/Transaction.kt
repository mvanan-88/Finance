package com.mathi.finance.data

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Transaction_Type(
    val id: Int? = null,
    val name: String,
    @SerialName("created_at")
    val createdAt: String,
    val status: Int,
)
