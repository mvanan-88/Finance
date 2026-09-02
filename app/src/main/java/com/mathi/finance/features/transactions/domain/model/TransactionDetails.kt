package com.mathi.finance.features.transactions.domain.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class TransactionDetails(
    val id: Int,
    @SerialName("created_at")
    val createdAt: String,
    val amount: Float,
    @SerialName("transaction_type")
    val transactionType: TransactionTypeInfo? = null,
    @SerialName("interest_rates")
    val interestRate: InterestRateInfo? = null,
    @SerialName("installment_tenure")
    val instalment_tenure: InstalmentTenureInfo? = null,
    @SerialName("contacts")
    val contact: ContactInfo? = null
)

@Serializable
data class TransactionTypeInfo(
    val name: String
)

@Serializable
data class InterestRateInfo(
    @SerialName("interest_rate")
    val rate: Float
)

@Serializable
data class InstalmentTenureInfo(
    @SerialName("tenure")
    val tenure: Int
)

@Serializable
data class ContactInfo(
    val name: String
)
