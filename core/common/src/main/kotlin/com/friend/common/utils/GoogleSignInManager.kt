package com.friend.common.utils

import android.content.Context
import androidx.credentials.ClearCredentialStateRequest
import androidx.credentials.CredentialManager
import androidx.credentials.CustomCredential
import androidx.credentials.GetCredentialRequest
import androidx.credentials.GetCredentialResponse
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
            GoogleSignInResult.Error(e.message.toString())
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

    private fun handleSignIn(result: GetCredentialResponse): GoogleSignInResult {
        val credential = result.credential

        // Case 1: Direct Match (What you had before)
        if (credential is GoogleIdTokenCredential) {
            return GoogleSignInResult.Success(
                idToken = credential.idToken,
                email = credential.id,
                displayName = credential.displayName
            )
        }
        // Case 2: CustomCredential Wrapper (The Fix)
        // Sometimes the library returns this generic type with the Google data inside
        else if (credential is CustomCredential && credential.type == GoogleIdTokenCredential.TYPE_GOOGLE_ID_TOKEN_CREDENTIAL) {
            try {
                // Unpack the data manually
                val googleIdTokenCredential = GoogleIdTokenCredential.createFrom(credential.data)
                return GoogleSignInResult.Success(
                    idToken = googleIdTokenCredential.idToken,
                    email = googleIdTokenCredential.id,
                    displayName = googleIdTokenCredential.displayName
                )
            } catch (e: Exception) {
                Timber.e("Failed to unwrap Google Credential: $e")
                return GoogleSignInResult.Error("Failed to parse Google data")
            }
        }

        // Debugging: Print exactly what type you received if it still fails
        Timber.e( "Received unknown type: ${credential.type} / Class: ${credential.javaClass.name}")
        return GoogleSignInResult.Error("Unknown credential type")
    }
}

// Simple wrapper for the result
sealed class GoogleSignInResult {
    data class Success(val idToken: String, val email: String, val displayName: String?) :
        GoogleSignInResult()

    data class Error(val message: String) : GoogleSignInResult()
}