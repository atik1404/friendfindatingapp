package com.friend.common.extfun

import android.util.Base64
import java.time.LocalTime


fun String.decode(): String =
    Base64.decode(this.replace(" ", "/"), Base64.DEFAULT).toString(charset("UTF-8"))


fun String.encode(): String =
    Base64.encodeToString(this.toByteArray(charset("UTF-8")), Base64.DEFAULT).replace("/", " ")

fun getGreetingText(now: LocalTime = LocalTime.now()): String {
    val hour = now.hour  // 0–23

    return when (hour) {
        in 5..11 -> "Good morning"
        in 12..16 -> "Good afternoon"
        else -> "Good evening"
    }
}
