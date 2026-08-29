package com.mathi.finance

import android.app.Activity
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseUser
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

sealed class AuthState {
    object Idle : AuthState()
    object Loading : AuthState()
    data class Success(val user: FirebaseUser?) : AuthState()
    data class Error(val message: String) : AuthState()
    data class OtpSent(val verificationId: String) : AuthState()
}

class AuthViewModel(private val authManager: AuthManager) : ViewModel() {
    private val _authState = MutableStateFlow<AuthState>(AuthState.Idle)
    val authState: StateFlow<AuthState> = _authState.asStateFlow()

    init {
        _authState.value = AuthState.Success(authManager.currentUser)
    }

    fun signInWithGoogle() {
        viewModelScope.launch {
            _authState.value = AuthState.Loading
            val result = authManager.signInWithGoogle()
            result.onSuccess { authResult ->
                _authState.value = AuthState.Success(authResult.user)
            }.onFailure { exception ->
                _authState.value = AuthState.Error(exception.localizedMessage ?: "Google Sign-In failed")
            }
        }
    }

    fun sendOtp(phoneNumber: String, activity: Activity) {
        _authState.value = AuthState.Loading
        authManager.sendOtp(
            phoneNumber = phoneNumber,
            activity = activity,
            onVerificationCompleted = { credential ->
                // Auto-verification handled by Firebase in some cases
                viewModelScope.launch {
                    // Sign in with credential if needed, but usually Firebase handles it
                }
            },
            onVerificationFailed = { e ->
                _authState.value = AuthState.Error(e.localizedMessage ?: "Verification failed")
            },
            onCodeSent = { verificationId, _ ->
                _authState.value = AuthState.OtpSent(verificationId)
            }
        )
    }

    fun verifyOtp(verificationId: String, otp: String) {
        viewModelScope.launch {
            _authState.value = AuthState.Loading
            val result = authManager.signInWithPhone(verificationId, otp)
            if (result != null) {
                _authState.value = AuthState.Success(result.user)
            } else {
                _authState.value = AuthState.Error("OTP Verification failed")
            }
        }
    }

    fun signOut() {
        authManager.signOut()
        _authState.value = AuthState.Success(null)
    }
}
