package com.friend.common.dateparser

import timber.log.Timber
import java.time.Instant
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.OffsetDateTime
import java.time.ZoneId
import java.time.ZoneOffset
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter
import java.time.temporal.ChronoUnit
import java.util.Locale
import java.util.concurrent.TimeUnit
import kotlin.math.abs

object DateTimeParser {

    // -------------------------
    // Current device date & time
    // -------------------------

    /** Current device date-time as LocalDateTime (no zone attached) */
    fun nowLocalDateTime(): LocalDateTime =
        LocalDateTime.now()

    /** Current device date-time with zone info */
    fun nowDeviceZoned(zoneId: ZoneId = ZoneId.systemDefault()): ZonedDateTime =
        ZonedDateTime.now(zoneId)

    /** Current UTC date-time */
    fun nowUtc(): ZonedDateTime =
        ZonedDateTime.now(ZoneOffset.UTC)


    // -------------------------
    // Local <-> UTC conversion
    // -------------------------

    /**
     * Convert a local date-time (in [zoneId]) to UTC time.
     */
    fun localToUtc(
        localDateTime: LocalDateTime,
        zoneId: ZoneId = ZoneId.systemDefault()
    ): ZonedDateTime {
        return localDateTime
            .atZone(zoneId)                // local time with device zone
            .withZoneSameInstant(ZoneOffset.UTC) // same instant in UTC
    }

    /**
     * Convert a UTC date-time to local time in [zoneId].
     *
     * @param utcDateTime a date-time that should be interpreted as UTC
     */
    fun utcToLocal(
        utcDateTime: LocalDateTime,
        zoneId: ZoneId = ZoneId.systemDefault()
    ): ZonedDateTime {
        return utcDateTime
            .atZone(ZoneOffset.UTC)        // treat as UTC
            .withZoneSameInstant(zoneId)   // convert to device/local zone
    }


    // -------------------------
    // Add / subtract days
    // -------------------------

    /** Add [days] to a LocalDateTime */
    fun addDays(
        dateTime: LocalDateTime,
        days: Long
    ): LocalDateTime = dateTime.plusDays(days)

    /** Subtract [days] from a LocalDateTime */
    fun subtractDays(
        dateTime: LocalDateTime,
        days: Long
    ): LocalDateTime = dateTime.minusDays(days)

    /** Add [days] to a LocalDate */
    fun addDays(
        date: LocalDate,
        days: Long
    ): LocalDate = date.plusDays(days)

    /** Subtract [days] from a LocalDate */
    fun subtractDays(
        date: LocalDate,
        days: Long
    ): LocalDate = date.minusDays(days)


    // -------------------------
    // String helpers (optional)
    // -------------------------

    /**
     * Convert a **local** time string (in [inputPattern]) to **UTC string** in [outputPattern].
     */
    fun localStringToUtcString(
        rawLocal: String,
        inputPattern: String,
        outputPattern: String,
        zoneId: ZoneId = ZoneId.systemDefault(),
        locale: Locale = Locale.getDefault()
    ): String {
        val inFormatter = DateTimeFormatter.ofPattern(inputPattern, locale)
        val outFormatter = DateTimeFormatter.ofPattern(outputPattern, locale)

        val local = LocalDateTime.parse(rawLocal, inFormatter)
        val utcZoned = localToUtc(local, zoneId)

        return utcZoned.format(outFormatter)
    }

    /**
     * Convert a **UTC** time string (in [inputPattern]) to **local string** in [outputPattern].
     */
    fun utcStringToLocalString(
        rawUtc: String,
        inputPattern: String,
        outputPattern: String,
        zoneId: ZoneId = ZoneId.systemDefault(),
        locale: Locale = Locale.getDefault()
    ): String {
        val inFormatter = DateTimeFormatter.ofPattern(inputPattern, locale)
        val outFormatter = DateTimeFormatter.ofPattern(outputPattern, locale)

        val utc = LocalDateTime.parse(rawUtc, inFormatter)
        val localZoned = utcToLocal(utc, zoneId)

        return localZoned.format(outFormatter)
    }

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
        zoneId: ZoneId = ZoneOffset.UTC,
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

    fun parseToLocalDateTime(
        raw: String,
        zoneId: ZoneId = ZoneId.systemDefault()
    ): LocalDateTime? {

        // 1) Try strict ISO instant (e.g. 2025-12-05T08:12:34Z)
        runCatching {
            val instant = Instant.parse(raw)
            return instant.atZone(zoneId).toLocalDateTime()
        }

        // 2) Try all known patterns
        for (formatter in DateTimePatterns.inputFormatters) {
            try {
                val result = tryParseWithFormatter(raw, formatter, zoneId)
                if (result != null) {
                    return result
                }
            } catch (ex: Exception) {
                Timber.e("Exception while parsing with $formatter: ${ex.message}")
            }
        }

        // 3) Nothing matched
        return null
    }

    private fun tryParseWithFormatter(
        raw: String,
        formatter: DateTimeFormatter,
        zoneId: ZoneId
    ): LocalDateTime? {
        return try {
            when {
                formatter in DateTimePatterns.dateTimeFormatters -> {
                    val local = LocalDateTime.parse(raw, formatter)
                    local.atZone(ZoneOffset.UTC)
                        .withZoneSameInstant(zoneId)
                        .toLocalDateTime()
                }
                // ISO instant
                formatter == DateTimeFormatter.ISO_INSTANT -> {
                    Instant.parse(raw).atZone(zoneId).toLocalDateTime()
                }

                // ISO offset
                formatter == DateTimeFormatter.ISO_OFFSET_DATE_TIME -> {
                    OffsetDateTime.parse(raw, formatter)
                        .atZoneSameInstant(zoneId)
                        .toLocalDateTime()
                }

                // Our "offset" style patterns (with +… or Z)
                formatter in DateTimePatterns.offsetOrIsoFormatters &&
                        (raw.contains('+') || raw.endsWith("Z", ignoreCase = true)) -> {
                    OffsetDateTime.parse(raw, formatter)
                        .atZoneSameInstant(zoneId)
                        .toLocalDateTime()
                }

                // Date + time (no offset)
                formatter in DateTimePatterns.dateTimeFormatters -> {
                    LocalDateTime.parse(raw, formatter)
                }

                // Date-only -> assume start of day
                formatter in DateTimePatterns.dateOnlyFormatters -> {
                    val date = LocalDate.parse(raw, formatter)
                    date.atStartOfDay()
                }

                // Time-only -> attach to "today" (or any default date)
                formatter in DateTimePatterns.timeOnlyFormatters -> {
                    val time = LocalTime.parse(raw, formatter)
                    LocalDate.now(zoneId).atTime(time)
                }

                else -> {
                    val utcLdt = LocalDateTime.parse(raw, formatter)
                    utcLdt.atZone(ZoneOffset.UTC)
                        .withZoneSameInstant(zoneId)
                        .toLocalDateTime()
                }
            }
        } catch (e: Exception) {
            null
        }
    }

    fun convertMillisToDate(millis: Long, dateFormat: String): String {
        val formatter = DateTimeFormatter.ofPattern(dateFormat)
            .withZone(ZoneOffset.UTC)
        return formatter.format(Instant.ofEpochMilli(millis))
    }

    fun convertMillisToTime(millis: Long): String {
        val safeMillis = millis.coerceAtLeast(0L)

        val totalSeconds = TimeUnit.MILLISECONDS.toSeconds(safeMillis)
        val hours = totalSeconds / 3600
        val minutes = (totalSeconds % 3600) / 60
        val seconds = totalSeconds % 60

        return if (hours > 0) String.format("%d:%02d:%02d", hours, minutes, seconds)
        else String.format("%d:%02d", minutes, seconds)
    }

    fun yearsAgoFromTodayUtcMillis(years: Long): Long {
        val todayUtc = LocalDate.now(ZoneOffset.UTC)
        val targetDate = todayUtc.minusYears(years)
        return targetDate
            .atStartOfDay(ZoneOffset.UTC)
            .toInstant()
            .toEpochMilli()
    }

    /**
     * Convert a LocalDateTime to epoch millis in [zoneId].
     */
    fun dateTimeToMillis(
        dateTime: LocalDateTime?,
        zoneId: ZoneId = ZoneOffset.UTC
    ): Long {
        if (dateTime == null) return 0L

        return dateTime
            .atZone(zoneId)
            .toInstant()
            .toEpochMilli()
    }

    fun calendarDayDifference(from: LocalDateTime, to: LocalDateTime): Long {
        return abs(ChronoUnit.DAYS.between(from.toLocalDate(), to.toLocalDate()))
    }

    /**
     * @param dayDiff whole-day difference between 'today' and [date].
     *        Example: today->0, yesterday->1, 2 days ago->2, etc.
     * @param date the message date (local calendar date)
     * @param locale output locale (defaults to device)
     */
    fun formatRelativeDateLabel(
        dayDiff: Long,
        date: String,
    ): String {
        try {
            val effectiveDate = when {
                dayDiff <= 0 -> "Today" // also covers future/invalid negatives
                dayDiff == 1L -> "Yesterday"
                dayDiff in 2..5 -> {
                    val dateTime = parseToLocalDateTime(date)
                    var day = ""
                    dateTime?.let {
                        day = getDayOfWeek(dateTime).toString()
                    }
                    day
                }

                else -> {
                    // MMM dd, yyyy -> "Sep 14, 2025"
                    val fmt = parseToPattern(date, DateTimePatterns.MDY_TEXT_COMMA)
                    fmt
                }
            }
            return effectiveDate
        } catch (ex: Exception) {
            ex.printStackTrace()
            return date
        }
    }

    fun getDayOfMonth(dateTime: LocalDateTime): Int = dateTime.dayOfMonth   // 1..31
    fun getDayOfYear(dateTime: LocalDateTime): Int = dateTime.dayOfYear     // 1..365/366
    fun getDayOfWeek(dateTime: LocalDateTime) = dateTime.dayOfWeek
}