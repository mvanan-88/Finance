package com.mathi.finance.features.auth.domain.model

import kotlinx.serialization.Serializable

@Serializable
data class User(
    val id: Int? = null,
    val user_name: String,
    val name: String? = null,
    val role: String? = null
)
