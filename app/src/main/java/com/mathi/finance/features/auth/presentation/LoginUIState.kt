package com.mathi.finance.features.auth.presentation

import com.mathi.finance.features.auth.domain.model.User

data class LoginUIState(
    val isLoading: Boolean = false,
    val user: User? = null,
    val error: String? = null
)
