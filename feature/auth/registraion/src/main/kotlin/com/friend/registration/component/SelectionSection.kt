package com.friend.registration.component

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Female
import androidx.compose.material.icons.rounded.Male
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.friend.common.constant.Gender
import com.friend.common.dateparser.DateTimeParser
import com.friend.common.dateparser.DateTimePatterns
import com.friend.common.dateparser.DateTimeUtils
import com.friend.ui.common.AppDatePickerDialog
import com.friend.ui.components.AppOutlineTextField
import com.friend.ui.components.SingleChoiceSegmentsWithIcons
import timber.log.Timber
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
        .let { if (it == -1) 0 else it } // fallback to 0 if not found

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
        .let { if (it == -1) 0 else it } // fallback to 0 if not found

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

    if (showDatePicker) {
        AppDatePickerDialog(
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