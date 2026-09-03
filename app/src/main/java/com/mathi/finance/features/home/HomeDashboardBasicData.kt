package com.mathi.finance.features.home

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class HomeDashboardBasicData(
    @SerialName("lended")
    val totalActiveLended: Float = 0f,
    @SerialName("recovered")
    val totalActiveRecovered: Float = 0f,
    @SerialName("active_count")
    val activeLoans: Int = 0,
    @SerialName("inactive_count")
    val completedLoans: Int = 0,
)
