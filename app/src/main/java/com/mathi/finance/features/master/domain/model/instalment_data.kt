package com.mathi.finance.features.master.domain.model

import kotlinx.serialization.Serializable

@Serializable
data class instalment_data(
    val id:Int? = null,
    val created_at : String,
    val created_by:Int? = null,
    val tenure:Int,
    val status:Int,
)
