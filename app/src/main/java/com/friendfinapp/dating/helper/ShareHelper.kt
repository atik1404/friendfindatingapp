package com.friendfinapp.dating.helper

import android.content.Context
import android.content.Intent

object ShareHelper {

    fun shareLink(title: String?, link: String?, mContext: Context) {
        val shareIntent = Intent(Intent.ACTION_SEND)
        shareIntent.type = "text/plain"
        shareIntent.putExtra(
            Intent.EXTRA_TEXT, """
     $title
     $link
     """.trimIndent()
        )
        mContext.startActivity(Intent.createChooser(shareIntent, "Share via"))
    }
}