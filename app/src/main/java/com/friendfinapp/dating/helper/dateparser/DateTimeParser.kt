package com.jerp.common.dateparser

import com.iamkamrul.dateced.DateCed
import java.text.ParseException
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.util.Calendar
import java.util.Date
import java.util.Locale


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
        val effectiveDate = when {
            dayDiff <= 0 -> "Today" // also covers future/invalid negatives
            dayDiff == 1 -> "Yesterday"
            dayDiff in 2..5 -> {
                // sat, sun, mon…
                DateCed.Factory.parse(date).day
            }
            dayDiff in 6..180 -> {
                // EEE, MMM dd  -> "Sat, Sep 14"
                val fmt = DateCed.Factory.parse(date).format("EEE, MMM dd")
                fmt
            }
            else -> {
                // MMM dd, yyyy -> "Sep 14, 2025"
                val fmt = DateCed.Factory.parse(date).format("MMM dd, yyyy")
                fmt
            }
        }

        return "$effectiveDate \n$dayDiff \n$date"
    }
}
