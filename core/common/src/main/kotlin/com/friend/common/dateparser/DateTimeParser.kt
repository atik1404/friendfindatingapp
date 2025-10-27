package com.friend.common.dateparser

import java.time.Instant
import java.time.ZoneId
import java.time.format.DateTimeFormatter

fun convertMillisToDate(millis: Long, dateFormat: String): String {
    val formatter = DateTimeFormatter.ofPattern(dateFormat)
        .withZone(ZoneId.systemDefault())
    return formatter.format(Instant.ofEpochMilli(millis))
}

