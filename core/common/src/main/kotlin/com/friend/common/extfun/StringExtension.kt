package com.friend.common.extfun

import android.util.Base64
import java.net.Inet4Address
import java.net.NetworkInterface
import java.net.SocketException
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

fun getLocalIpAddress(): String? {
    try {
        val en = NetworkInterface.getNetworkInterfaces()
        while (en.hasMoreElements()) {
            val intf = en.nextElement()
            val enumIpAddr = intf.inetAddresses
            while (enumIpAddr.hasMoreElements()) {
                val inetAddress = enumIpAddr.nextElement()
                if (!inetAddress.isLoopbackAddress && inetAddress is Inet4Address) {
                    return inetAddress.hostAddress
                }
            }
        }
    } catch (ex: SocketException) {
        ex.printStackTrace()
    }
    return null
}

fun String.initialsOf(): String {
    val cleaned = this.trim()
    if (cleaned.isEmpty()) return "?"

    // first two visible chars (works fine for normal latin names)
    return cleaned.take(2).uppercase()
}