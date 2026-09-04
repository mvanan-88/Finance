package com.mathi.finance.features.auth.domain.repository

import com.mathi.finance.features.auth.domain.model.User

interface AuthRepository {
    suspend fun login(username: String, password: String): Result<User>
    suspend fun checkAutoLogin(): Result<User?>
    suspend fun logout()
}
