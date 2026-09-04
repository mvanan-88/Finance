package com.mathi.finance.features.auth.data.repository

import com.mathi.finance.core.network.SupabaseClient
import com.mathi.finance.core.prefs.PreferenceManager
import com.mathi.finance.features.auth.domain.model.User
import com.mathi.finance.features.auth.domain.repository.AuthRepository
import io.github.jan.supabase.postgrest.from

class AuthRepositoryImpl(
    private val preferenceManager: PreferenceManager
) : AuthRepository {

    override suspend fun login(username: String, password: String): Result<User> {
        return try {
            val user = SupabaseClient.client.from("users").select {
                filter {
                    eq("user_name", username)
                    eq("password", password)
                }
            }.decodeSingleOrNull<User>()

            if (user != null) {
                user.id?.let { preferenceManager.saveUserId(it) }
                Result.success(user)
            } else {
                Result.failure(Exception("Invalid username or password"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun checkAutoLogin(): Result<User?> {
        val savedUserId = preferenceManager.getUserId()
        if (savedUserId == -1) return Result.success(null)

        return try {
            val user = SupabaseClient.client.from("users").select {
                filter {
                    eq("id", savedUserId)
                }
            }.decodeSingleOrNull<User>()

            if (user != null) {
                Result.success(user)
            } else {
                preferenceManager.clear()
                Result.success(null)
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun logout() {
        preferenceManager.clear()
    }
}
