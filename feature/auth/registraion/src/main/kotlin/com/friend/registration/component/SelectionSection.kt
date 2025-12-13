package com.friend.registration.component

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Female
import androidx.compose.material.icons.rounded.Male
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.friend.common.constant.Gender
import com.friend.common.dateparser.DateTimePatterns
import com.friend.common.dateparser.DateTimeUtils
import com.friend.ui.common.AppDatePickerDialog
import com.friend.ui.components.AppOutlineTextField
import com.friend.ui.components.SingleChoiceSegmentsWithIcons
import com.friend.designsystem.R as Res

val genders = listOf(
    Pair(Icons.Rounded.Male, Gender.MALE.name),
    Pair(Icons.Rounded.Female, Gender.FEMALE.name),
)

@Composable
fun GenderSelection(
    selectedGender: String,
    onSelected: (Gender) -> Unit,
    modifier: Modifier = Modifier
) {
    val selectedIndex = genders.indexOfFirst { it.second == selectedGender }

    SingleChoiceSegmentsWithIcons(
        modifier = modifier,
        title = stringResource(Res.string.label_gender),
        options = genders,
        selectedIndex = selectedIndex,
        onSelected = {
            onSelected.invoke(Gender.toEnum(genders[it].second))
        }
    )
}

@Composable
fun InterestedInSelection(
    selectedGender: String,
    onSelected: (Gender) -> Unit,
    modifier: Modifier = Modifier
) {
    val selectedIndex = genders.indexOfFirst { it.second == selectedGender }

    SingleChoiceSegmentsWithIcons(
        modifier = modifier,
        title = stringResource(Res.string.label_interested_in),
        options = genders,
        selectedIndex = selectedIndex,
        onSelected = {
            onSelected.invoke(Gender.toEnum(genders[it].second))
        }
    )
}

@Composable
fun BirthDateSelection(
    selectedDate: String,
    onSelected: (String) -> Unit,
    showDatePicker: Boolean,
    setShowDatePicker: (Boolean) -> Unit,
    modifier: Modifier = Modifier
) {
    AppOutlineTextField(
        text = DateTimeUtils.parseToPattern(selectedDate, DateTimePatterns.DMY_TEXT),
        modifier = modifier.fillMaxWidth(),
        title = stringResource(Res.string.label_date_of_birth),
        placeholder = stringResource(Res.string.hint_dob),
        onValueChange = onSelected,
        isReadOnly = true,
        onClickListener = {
            setShowDatePicker(true)
        }
    )

    val maxSelectableDates = DateTimeUtils.yearsAgoFromTodayUtcMillis(18)
    val selectedDateTime = DateTimeUtils.parseToDateTime(selectedDate)

    val selectedDateTimeInMillis = if(selectedDate.isNotEmpty())
        DateTimeUtils.dateTimeToMillis(selectedDateTime)
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