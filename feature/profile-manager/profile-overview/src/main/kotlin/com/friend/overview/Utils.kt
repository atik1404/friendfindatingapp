package com.friend.overview

import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.net.Uri
import timber.log.Timber

fun shareApp(context: Context) {
    try {
        val packageName = context.packageName
        val shareIntent = Intent(Intent.ACTION_SEND).apply {
            type = "text/plain"
            putExtra(Intent.EXTRA_SUBJECT, "FriendFin")
            val shareMessage = buildString {
                appendLine("Let me recommend you this application")
                append("https://play.google.com/store/apps/details?id=$packageName")
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


fun openAppInPlayStore(context: Context) {
    val packageName = context.packageName
    try {

        // Try to open in Play Store app
        context.startActivity(
            Intent(
                Intent.ACTION_VIEW,
                Uri.parse("market://details?id=$packageName")
            )
        )
    } catch (e: ActivityNotFoundException) {
        // Fallback: open in browser
        context.startActivity(
            Intent(
                Intent.ACTION_VIEW,
                Uri.parse("https://play.google.com/store/apps/details?id=$packageName")
            )
        )
    }
}

fun openMailApp(context: Context) {
    val recipient = "atik@gmail.com"

    val emailIntent = Intent(Intent.ACTION_SENDTO).apply {
        data = Uri.parse("mailto:$recipient")
    }

    try {
        context.startActivity(emailIntent)
    } catch (e: ActivityNotFoundException) {
        Timber.e("No email app found")
    }
}