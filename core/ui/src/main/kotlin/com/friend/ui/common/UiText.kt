package com.friend.ui.common

import android.content.Context
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext

sealed class UiText {
    data class Dynamic(val text: String) : UiText()
    data class StringRes(
        val resId: Int,
        val args: List<Any> = emptyList()
    ) : UiText()
}


fun UiText.asString(context: Context): String {
    return when (this) {
        is UiText.Dynamic -> text
        is UiText.StringRes -> context.getString(resId, *args.toTypedArray())
    }
}