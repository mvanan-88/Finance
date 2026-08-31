package com.mathi.finance.features.contacts.domain.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Contact(
    val id: Int? = null,
    val name: String,
    @SerialName("phone_number")
    val phoneNumber: String? = null,
    val created_by: Int? = null
)
