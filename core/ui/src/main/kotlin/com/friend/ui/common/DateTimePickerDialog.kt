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
import java.time.Instant
import java.time.LocalDate
import java.time.ZoneId

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SimpleDatePickerDialog(
    onDateSelected: (Long?) -> Unit,
    onDismiss: () -> Unit,
    isMaxDateEnable: Boolean = false, // true = block future dates
    isMinDateEnable: Boolean = false, // true = block past dates
) {
    // figure out "today at start of day UTC" in millis
    val todayStartUtcMillis = remember {
        // java.time for accuracy
        val todayStartInstant = LocalDate
            .now(ZoneId.systemDefault()) // take device local date
            .atStartOfDay(ZoneId.systemDefault()) // 00:00 local
            .toInstant()
        todayStartInstant.toEpochMilli()
    }

    // Build selectableDates rules based on params
    val selectableDates = remember(isMaxDateEnable, isMinDateEnable, todayStartUtcMillis) {
        object : SelectableDates {
            override fun isSelectableDate(utcTimeMillis: Long): Boolean {
                return when {
                    // both true → only today allowed
                    isMaxDateEnable && isMinDateEnable -> {
                        isSameDay(utcTimeMillis, todayStartUtcMillis)
                    }

                    // max enabled → user can't go AFTER today
                    isMaxDateEnable -> {
                        utcTimeMillis <= endOfDayMillis(todayStartUtcMillis)
                    }

                    // min enabled → user can't go BEFORE today
                    isMinDateEnable -> {
                        utcTimeMillis >= todayStartUtcMillis
                    }

                    // none → all allowed
                    else -> true
                }
            }
        }
    }

    val datePickerState = rememberDatePickerState(
        initialSelectedDateMillis = todayStartUtcMillis,
        selectableDates = selectableDates
    )

    DatePickerDialog(
        onDismissRequest = onDismiss,
        confirmButton = {
            TextButton(
                onClick = {
                    onDateSelected(datePickerState.selectedDateMillis)
                    onDismiss()
                }
            ) {
                Text("OK")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancel")
            }
        }
    ) {
        DatePicker(
            state = datePickerState
        )
    }
}

/**
 * Helper: check if two millis are the same calendar day (local zone).
 */
private fun isSameDay(millisA: Long, millisB: Long): Boolean {
    val zone = ZoneId.systemDefault()
    val dateA = Instant.ofEpochMilli(millisA).atZone(zone).toLocalDate()
    val dateB = Instant.ofEpochMilli(millisB).atZone(zone).toLocalDate()
    return dateA == dateB
}

/**
 * Helper: get 23:59:59.999 of the same day as [startOfDayMillis].
 * This lets "max today" include all timestamps within today.
 */
private fun endOfDayMillis(startOfDayMillis: Long): Long {
    val zone = ZoneId.systemDefault()
    val endOfDayInstant = Instant
        .ofEpochMilli(startOfDayMillis)
        .atZone(zone)
        .toLocalDate()
        .atTime(23, 59, 59, 999_000_000)
        .atZone(zone)
        .toInstant()

    return endOfDayInstant.toEpochMilli()
}
