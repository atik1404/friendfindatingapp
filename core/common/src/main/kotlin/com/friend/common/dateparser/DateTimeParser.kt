package com.friend.common.dateparser

import timber.log.Timber
import java.time.Instant
import java.time.LocalDateTime
import java.time.OffsetDateTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.Locale

fun convertMillisToDate(millis: Long, dateFormat: String): String {
    val formatter = DateTimeFormatter.ofPattern(dateFormat)
        .withZone(ZoneId.systemDefault())
    return formatter.format(Instant.ofEpochMilli(millis))
}

object DateTimeParser {
    /**
     * Try to parse any supported date-time string and format it to [outputPattern].
     *
     * @param raw           Input date-time string (various formats supported)
     * @param outputPattern Target output pattern (e.g. "dd MMM yyyy, hh:mm a")
     * @param zoneId        Zone used when converting Instant/Offset types to local
     * @param locale        Locale for month names etc.
     *
     * @return Formatted date string if parsing succeeds, otherwise original [raw].
     */
    fun parseToPattern(
        raw: String,
        outputPattern: String,
        zoneId: ZoneId = ZoneId.systemDefault(),
        locale: Locale = Locale.getDefault()
    ): String {
        val outputFormatter = DateTimeFormatter.ofPattern(outputPattern, locale)

        // 1) Try Instant.parse directly (only works for strict ISO)
        runCatching {
            val instant = Instant.parse(raw)
            return instant.atZone(zoneId).format(outputFormatter)
        }

        // 2) Try our list of formatters
        for (formatter in DateTimePatterns.inputFormatters) {
            try {
                val result = tryParseWithFormatter(raw, formatter, zoneId)
                if (result != null) {
                    return result.format(outputFormatter)
                }
            } catch (ex: Exception) {
                Timber.e("Exception: ${ex.message}")
            }
        }

        // 3) Fallback: give back original if nothing matched
        return raw
    }

    private fun tryParseWithFormatter(
        raw: String,
        formatter: DateTimeFormatter,
        zoneId: ZoneId
    ): LocalDateTime? {
        return try {
            // Some formatters may include zone/offset, others not.
            return when {
                formatter == DateTimeFormatter.ISO_INSTANT -> {
                    Instant.parse(raw).atZone(zoneId).toLocalDateTime()
                }

                formatter == DateTimeFormatter.ISO_OFFSET_DATE_TIME -> {
                    OffsetDateTime.parse(raw, formatter).atZoneSameInstant(zoneId).toLocalDateTime()
                }

                raw.contains('+') || raw.endsWith("Z", ignoreCase = true) -> {
                    // crude guess: treat as offset date-time
                    OffsetDateTime.parse(raw, formatter).atZoneSameInstant(zoneId).toLocalDateTime()
                }

                else -> {
                    // No zone info -> plain LocalDateTime
                    LocalDateTime.parse(raw, formatter)
                }
            }
        } catch (e: Exception) {
            null
        }
    }
}
