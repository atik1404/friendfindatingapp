package com.friend.common.utils

import android.content.Context
import androidx.credentials.ClearCredentialStateRequest
import androidx.credentials.CredentialManager
import androidx.credentials.GetCredentialRequest
import androidx.credentials.exceptions.GetCredentialException
import com.friend.common.constant.AppConstants
import com.google.android.libraries.identity.googleid.GetSignInWithGoogleOption
import com.google.android.libraries.identity.googleid.GoogleIdTokenCredential
import timber.log.Timber

class GoogleSignInManager(private val context: Context) {

    private val credentialManager = CredentialManager.create(context)

    suspend fun signIn(): GoogleSignInResult {
        // Use the WEB Client ID from Google Cloud Console

        val googleIdOption =
            GetSignInWithGoogleOption.Builder(serverClientId = AppConstants.WEB_CLIENT_ID)
                .build()

        // 2. Build the Request
        val request = GetCredentialRequest.Builder()
            .addCredentialOption(googleIdOption)
            .build()

        return try {
            // 3. Perform the request
            // NOTE: specific to Credential Manager, this must run on UI context usually,
            // but the API handles the Activity context internally if passed correctly.
            val result = credentialManager.getCredential(
                request = request,
                context = context
            )

            handleSignIn(result)
        } catch (e: GetCredentialException) {
            GoogleSignInResult.Error(e.message)
        }
    }

    suspend fun signOut() {
        try {
            // 1. Clear the Credential Manager state
            // This prevents auto-selection on next login
            credentialManager.clearCredentialState(ClearCredentialStateRequest())

            // 2. IMPORTANT: Clear your own app's local storage
            // e.g., DataStore, SharedPreferences, or a Singleton holding the user
            // LocalStorage.clearUser()

        } catch (e: Exception) {
            Timber.e("Error signing out: ${e.message}")
        }
    }

    private fun handleSignIn(result: androidx.credentials.GetCredentialResponse): GoogleSignInResult {
        val credential = result.credential

        // Check if the credential is a Google ID Token
        if (credential is GoogleIdTokenCredential) {
            val googleIdToken = credential.idToken
            val email = credential.id
            val displayName = credential.displayName

            // Send this token to your backend or Firebase
            return GoogleSignInResult.Success(
                idToken = googleIdToken,
                email = email,
                displayName = displayName
            )
        } else {
            return GoogleSignInResult.Error("Unknown credential type")
        }
    }
}

// Simple wrapper for the result
sealed class GoogleSignInResult {
    data class Success(val idToken: String, val email: String, val displayName: String?) :
        GoogleSignInResult()

    data class Error(val message: String?) : GoogleSignInResult()
}