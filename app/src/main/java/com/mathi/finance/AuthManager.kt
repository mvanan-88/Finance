package com.mathi.finance

import android.content.Context
import androidx.credentials.CredentialManager
import androidx.credentials.GetCredentialRequest
import androidx.credentials.GetCredentialResponse
import com.google.android.libraries.identity.googleid.GetGoogleIdOption
import com.google.android.libraries.identity.googleid.GoogleIdTokenCredential
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.PhoneAuthOptions
import com.google.firebase.auth.PhoneAuthProvider
import kotlinx.coroutines.tasks.await
import java.util.concurrent.TimeUnit
import android.app.Activity
import com.google.firebase.FirebaseException
import com.google.firebase.auth.PhoneAuthCredential
import android.util.Log
import androidx.credentials.exceptions.NoCredentialException
import androidx.credentials.CustomCredential

class AuthManager(private val context: Context) {
    private val auth: FirebaseAuth = FirebaseAuth.getInstance()
    private val credentialManager = CredentialManager.create(context)

    companion object {
        private const val TAG = "AuthManager"
    }

    val currentUser get() = auth.currentUser

    suspend fun signInWithGoogle(): Result<AuthResult> {
        Log.d(TAG, "Starting Google Sign-In")
        val googleIdOption = GetGoogleIdOption.Builder()
            .setFilterByAuthorizedAccounts(false)
            .setServerClientId("794201004088-m6im5beu3vol174d2obdqc07if668cns.apps.googleusercontent.com")
            .setAutoSelectEnabled(false) // Changed to false to avoid issues when no accounts are present
            .build()

        val request = GetCredentialRequest.Builder()
            .addCredentialOption(googleIdOption)
            .build()

        return try {
            val result = credentialManager.getCredential(
                context = context,
                request = request
            )
            Log.d(TAG, "Credential obtained: ${result.credential.type}")
            val authResult = handleSignIn(result)
            if (authResult != null) {
                Result.success(authResult)
            } else {
                Result.failure(Exception("Failed to sign in with Google credential"))
            }
        } catch (e: NoCredentialException) {
            Log.e(TAG, "No Google accounts found on device", e)
            Result.failure(Exception("No Google accounts found on this device. Please add a Google account in Settings."))
        } catch (e: Exception) {
            Log.e(TAG, "Google Sign-In Exception: ${e.message}", e)
            Result.failure(e)
        }
    }

    private suspend fun handleSignIn(result: GetCredentialResponse): AuthResult? {
        val credential = result.credential
        Log.d(TAG, "Handling sign in for credential type: ${credential.type}")

        val googleIdTokenCredential = when {
            credential is GoogleIdTokenCredential -> credential
            credential is CustomCredential && credential.type == GoogleIdTokenCredential.TYPE_GOOGLE_ID_TOKEN_CREDENTIAL -> {
                try {
                    GoogleIdTokenCredential.createFrom(credential.data)
                } catch (e: Exception) {
                    Log.e(TAG, "Failed to parse GoogleIdTokenCredential from CustomCredential", e)
                    null
                }
            }
            else -> {
                Log.w(TAG, "Received credential is not GoogleIdTokenCredential or compatible CustomCredential: ${credential.javaClass.name}")
                null
            }
        }

        if (googleIdTokenCredential != null) {
            Log.d(TAG, "Google ID Token obtained")
            val firebaseCredential = GoogleAuthProvider.getCredential(googleIdTokenCredential.idToken, null)
            return try {
                val authResult = auth.signInWithCredential(firebaseCredential).await()
                Log.d(TAG, "Firebase Sign-In successful: ${authResult.user?.uid}")
                authResult
            } catch (e: Exception) {
                Log.e(TAG, "Firebase Sign-In with credential failed", e)
                null
            }
        }
        return null
    }

    fun sendOtp(
        phoneNumber: String,
        activity: Activity,
        onVerificationCompleted: (PhoneAuthCredential) -> Unit,
        onVerificationFailed: (FirebaseException) -> Unit,
        onCodeSent: (String, PhoneAuthProvider.ForceResendingToken) -> Unit
    ) {
        val options = PhoneAuthOptions.newBuilder(auth)
            .setPhoneNumber(phoneNumber)
            .setTimeout(60L, TimeUnit.SECONDS)
            .setActivity(activity)
            .setCallbacks(object : PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
                override fun onVerificationCompleted(credential: PhoneAuthCredential) {
                    onVerificationCompleted(credential)
                }

                override fun onVerificationFailed(e: FirebaseException) {
                    onVerificationFailed(e)
                }

                override fun onCodeSent(
                    verificationId: String,
                    token: PhoneAuthProvider.ForceResendingToken
                ) {
                    onCodeSent(verificationId, token)
                }
            })
            .build()
        PhoneAuthProvider.verifyPhoneNumber(options)
    }

    suspend fun signInWithPhone(verificationId: String, otp: String): AuthResult? {
        val credential = PhoneAuthProvider.getCredential(verificationId, otp)
        return try {
            auth.signInWithCredential(credential).await()
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }

    fun signOut() {
        auth.signOut()
    }
}
