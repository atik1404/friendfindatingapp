package com.friend.common.extfun

import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.net.Uri
import timber.log.Timber


fun Context.shareApp() {
    try {
        val packageName = this.packageName
        val shareIntent = Intent(Intent.ACTION_SEND).apply {
            type = "text/plain"
            putExtra(Intent.EXTRA_SUBJECT, "FriendFin")
            val shareMessage = buildString {
                appendLine("Let me recommend you this application")
                append("https://play.google.com/store/apps/details?id=$packageName")
            }
            putExtra(Intent.EXTRA_TEXT, shareMessage)
        }
        this.startActivity(
            Intent.createChooser(shareIntent, "Choose one")
        )
    } catch (e: Exception) {
        Timber.e("$e Exception while sharing")
    }
}


fun Context.openAppInPlayStore() {
    val packageName = this.packageName
    try {
        // Try to open in Play Store app
        this.startActivity(
            Intent(
                Intent.ACTION_VIEW,
                Uri.parse("market://details?id=$packageName")
            )
        )
    } catch (e: ActivityNotFoundException) {
        // Fallback: open in browser
        Timber.e("$e Exception while opening in Play Store")
        this.startActivity(
            Intent(
                Intent.ACTION_VIEW,
                Uri.parse("https://play.google.com/store/apps/details?id=$packageName")
            )
        )
    }
}

fun Context.openMailApp() {
    val recipient = "atik@gmail.com"

    val emailIntent = Intent(Intent.ACTION_SENDTO).apply {
        data = Uri.parse("mailto:$recipient")
    }

    try {
        this.startActivity(emailIntent)
    } catch (e: ActivityNotFoundException) {
        Timber.e("No email app found: $e")
    }
}