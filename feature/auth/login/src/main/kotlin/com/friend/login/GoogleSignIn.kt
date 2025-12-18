package com.friend.login

import android.content.Context
import androidx.credentials.CredentialManager
import androidx.credentials.CustomCredential
import androidx.credentials.GetCredentialRequest
import androidx.credentials.exceptions.GetCredentialCancellationException
import androidx.credentials.exceptions.NoCredentialException
import com.google.android.libraries.identity.googleid.GetGoogleIdOption
import com.google.android.libraries.identity.googleid.GoogleIdTokenCredential
import java.util.UUID
import com.friend.designsystem.R as Res

suspend fun signInWithGoogle(activity: Context): String {
    val credentialManager = CredentialManager.create(activity)

    fun buildRequest(filterAuthorized: Boolean): GetCredentialRequest {
        val googleIdOption = GetGoogleIdOption.Builder()
            .setServerClientId(activity.getString(Res.string.default_web_client_id))
            .setFilterByAuthorizedAccounts(false)
            .setAutoSelectEnabled(true)
            .setNonce(UUID.randomUUID().toString())
            .build()

        return GetCredentialRequest.Builder()
            .addCredentialOption(googleIdOption)
            .build()
    }

    try {
        val result =
            credentialManager.getCredential(activity, buildRequest(filterAuthorized = true))
        val cred = result.credential

        if (cred is CustomCredential &&
            cred.type == GoogleIdTokenCredential.TYPE_GOOGLE_ID_TOKEN_CREDENTIAL
        ) {
            return GoogleIdTokenCredential.createFrom(cred.data).idToken
        }
        error("Unexpected credential type")
    } catch (e: NoCredentialException) {
        // No previously-authorized accounts → try showing all Google accounts (sign-up/sign-in)
        val result =
            credentialManager.getCredential(activity, buildRequest(filterAuthorized = false))
        val cred = result.credential
        if (cred is CustomCredential &&
            cred.type == GoogleIdTokenCredential.TYPE_GOOGLE_ID_TOKEN_CREDENTIAL
        ) {
            return GoogleIdTokenCredential.createFrom(cred.data).idToken
        }
        error("Unexpected credential type: ${e.message}")
    } catch (e: GetCredentialCancellationException) {
        error("User cancelled: ${e.message}")
    }
}
