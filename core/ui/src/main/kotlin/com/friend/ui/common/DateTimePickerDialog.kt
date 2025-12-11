package com.friend.ui.common

import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.SelectableDates
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import com.friend.common.dateparser.DateTimePatterns
import com.friend.common.dateparser.convertMillisToDate
import java.util.Calendar
import java.util.TimeZone

@OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterial3Api::class)
@Composable
fun AppDatePickerDialog(
    onDismissRequest: () -> Unit,
    onConfirm: (selectedDateMillis: String) -> Unit,
    minDateMillis: Long? = null,
    maxDateMillis: Long? = null,
    startSelectedDateMillis: Long? = null,
    confirmText: String = "OK",
    dismissText: String = "Cancel",
) {
    val selectableDates = remember(minDateMillis, maxDateMillis) {
        MinMaxSelectableDates(
            minDateMillis = minDateMillis,
            maxDateMillis = maxDateMillis
        )
    }

    // DatePickerState holds current visible month + selected date
    val datePickerState = rememberDatePickerState(
        initialSelectedDateMillis = startSelectedDateMillis,
        selectableDates = selectableDates
        // you can also control initialDisplayedMonthMillis if you want
    )

    DatePickerDialog(
        onDismissRequest = onDismissRequest,
        confirmButton = {
            TextButton(
                onClick = {
                    onConfirm(convertMillisToDate(datePickerState.selectedDateMillis ?: 0L,
                        DateTimePatterns.SQL_YMD))
                }
            ) { Text(confirmText) }
        },
        dismissButton = {
            TextButton(onClick = onDismissRequest) {
                Text(dismissText)
            }
        }
    ) {
        DatePicker(
            state = datePickerState,
        )
    }
}


@OptIn(ExperimentalMaterial3Api::class)
class MinMaxSelectableDates(
    private val minDateMillis: Long? = null,
    private val maxDateMillis: Long? = null,
) : SelectableDates {

    // called for specific days
    override fun isSelectableDate(utcTimeMillis: Long): Boolean {
        val afterMin = minDateMillis?.let { utcTimeMillis >= it } ?: true
        val beforeMax = maxDateMillis?.let { utcTimeMillis <= it } ?: true
        return afterMin && beforeMax
    }

    // called for year-only selection mode (year grid)
    override fun isSelectableYear(year: Int): Boolean {
        // optional: restrict year scrolling.
        // simplest: allow all years that are within min/max if provided
        val cal = Calendar.getInstance(TimeZone.getTimeZone("UTC"))

        val minOk = minDateMillis?.let {
            cal.timeInMillis = it
            year >= cal.get(Calendar.YEAR)
        } ?: true

        val maxOk = maxDateMillis?.let {
            cal.timeInMillis = it
            year <= cal.get(Calendar.YEAR)
        } ?: true

        return minOk && maxOk
    }
}

