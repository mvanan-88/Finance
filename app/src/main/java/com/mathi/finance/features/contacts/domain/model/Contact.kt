package com.mathi.finance.features.contacts.domain.model

data class Contact(
    val id: String,
    val name: String,
    val phoneNumber: String? = null
)
