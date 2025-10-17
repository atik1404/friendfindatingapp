package com.friend.common.dateparser

import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

object DateTimeParser {
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
}
