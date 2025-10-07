package com.friendfinapp.dating.helper.dateparser

import com.iamkamrul.dateced.DateCed
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale
import java.util.TimeZone

fun String.parseUtcToLocalCompat(
    outputPattern: String = "yyyy-MM-dd HH:mm:ss"
): String {
    val patterns = arrayOf(
        "yyyy-MM-dd'T'HH:mm:ss.SSSX", // with millis + offset/Z
        "yyyy-MM-dd'T'HH:mm:ss.SSS",  // with millis, no offset
        "yyyy-MM-dd'T'HH:mm:ssX",     // no millis, with offset/Z
        "yyyy-MM-dd'T'HH:mm:ss"       // plain
    )

    var parsedDate: Date? = null
    for (p in patterns) {
        try {
            val inFmt = SimpleDateFormat(p, Locale.US).apply {
                timeZone = TimeZone.getTimeZone("UTC")
            }
            parsedDate = inFmt.parse(this)
            if (parsedDate != null) break
        } catch (_: ParseException) {
            // try next pattern
        }
    }
    if (parsedDate == null) return this

    val outFmt = SimpleDateFormat(outputPattern, Locale.getDefault()).apply {
        timeZone = TimeZone.getDefault() // local
    }
    return outFmt.format(parsedDate)
}


object DateTimeParser {
    fun getCurrentDeviceDateTime(format: String): String {
        val c = Calendar.getInstance().time
        val df = SimpleDateFormat(format, Locale.US)
        return df.format(c)
    }

    fun convertLongToReadableDateTime(time: Long, format: String): String {
        val df = SimpleDateFormat(format, Locale.US)
        return df.format(time)
    }

    fun String.convertReadableDateTime(
        dateFormat: String,
        outputFormat: String
    ): String {
        var formatDate = ""
        var sf = SimpleDateFormat(dateFormat, Locale.US)
        try {
            this.let {
                val parseDate = sf.parse(it)
                sf = SimpleDateFormat(outputFormat, Locale.US)
                formatDate = sf.format(parseDate!!)
            }
        } catch (e: ParseException) {
            e.printStackTrace()
        }
        return formatDate
    }

    fun addLimitToDate(allowedDay: Int): Long {
        val calendar = Calendar.getInstance()
        try {
            calendar.add(Calendar.DATE, allowedDay)
        } catch (e: ParseException) {
            e.printStackTrace()
        }
        return calendar.timeInMillis
    }

    fun convertMilliSecondToMinute(milliSecond: Long) = try {
        ((milliSecond / 1000) / 60).toInt()
    } catch (ex: Exception) {
        0
    }

    private fun convertDateFormatToMilliSeconds(dateFormat: String, dateTime: String) =
        SimpleDateFormat(dateFormat, Locale.US).parse(dateTime)?.time ?: 0L

    fun daysDifferenceBetweenTwoDates(
        firstDate: String,
        firstDateFormat: String,
        secondDate: String,
        secondDateFormat: String
    ): Int {
        val firstDateToMillis =
            convertDateFormatToMilliSeconds(dateFormat = firstDateFormat, dateTime = firstDate)
        val secondDateToMillis =
            convertDateFormatToMilliSeconds(dateFormat = secondDateFormat, dateTime = secondDate)

        val diffInMillis =
            if (firstDateToMillis > secondDateToMillis) firstDateToMillis - secondDateToMillis
            else secondDateToMillis - firstDateToMillis
        return (diffInMillis / (1000 * 60 * 60 * 24)).toInt()
    }

    /**
     * @param dayDiff whole-day difference between 'today' and [date].
     *        Example: today->0, yesterday->1, 2 days ago->2, etc.
     * @param date the message date (local calendar date)
     * @param locale output locale (defaults to device)
     */
    fun formatRelativeDateLabel(
        dayDiff: Int,
        date: String,
    ): String {
        try {
            val effectiveDate = when {
                dayDiff <= 0 -> "Today" // also covers future/invalid negatives
                dayDiff == 1 -> "Yesterday"
                dayDiff in 2..5 -> {
                    // sat, sun, mon…
                    DateCed.Factory.parse(date).day
                }
                else -> {
                    // MMM dd, yyyy -> "Sep 14, 2025"
                    val fmt = DateCed.Factory.parse(date).format("MMMM dd, yyyy")
                    fmt
                }
            }
            return effectiveDate
        }catch (ex : Exception){
            ex.printStackTrace()
            return date
        }
    }
}
