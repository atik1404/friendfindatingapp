package com.friendfinapp.dating

import android.content.Intent
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext

@Composable
fun ShareApp() {
    val context = LocalContext.current
    try {
        val shareIntent = Intent(Intent.ACTION_SEND).apply {
            type = "text/plain"
            putExtra(Intent.EXTRA_SUBJECT, "FriendFin")
            val shareMessage = buildString {
                appendLine("Let me recommend you this application")
                appendLine()
                append("https://play.google.com/store/apps/details?id=${BuildConfig.APPLICATION_ID}")
            }
            putExtra(Intent.EXTRA_TEXT, shareMessage)
        }
        context.startActivity(
            Intent.createChooser(shareIntent, "Choose one")
        )
    } catch (e: Exception) {
        // Handle error if needed (e.g., show a Toast)
    }
}