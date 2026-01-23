package com.friend.registration.component

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.friend.common.dateparser.DateTimePatterns
import com.friend.common.dateparser.DateTimeParser
import com.friend.ui.common.AppDatePickerDialog
import com.friend.ui.components.AppOutlineTextField
import com.friend.designsystem.R as Res


@Composable
fun BirthDateSelection(
    selectedDate: String,
    onSelected: (String) -> Unit,
    showDatePicker: Boolean,
    setShowDatePicker: (Boolean) -> Unit,
    modifier: Modifier = Modifier
) {
    AppOutlineTextField(
        text = DateTimeParser.parseToPattern(selectedDate, DateTimePatterns.DMY_TEXT),
        modifier = modifier.fillMaxWidth(),
        title = stringResource(Res.string.label_date_of_birth),
        placeholder = stringResource(Res.string.hint_dob),
        onValueChange = onSelected,
        isReadOnly = true,
        onClickListener = {
            setShowDatePicker(true)
        }
    )

    val maxSelectableDates = DateTimeParser.yearsAgoFromTodayUtcMillis(18)
    val selectedDateTime = DateTimeParser.parseToLocalDateTime(selectedDate)

    val selectedDateTimeInMillis = if (selectedDate.isNotEmpty())
        DateTimeParser.dateTimeToMillis(selectedDateTime)
    else maxSelectableDates

    if (showDatePicker) {
        AppDatePickerDialog(
            maxDateMillis = maxSelectableDates,
            startSelectedDateMillis = selectedDateTimeInMillis,
            onDismissRequest = {
                setShowDatePicker(false)
            },
            onConfirm = {
                onSelected.invoke(it)
                setShowDatePicker(false)
            }
        )
    }
}