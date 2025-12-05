package com.friend.registration.component

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Female
import androidx.compose.material.icons.rounded.Male
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.friend.ui.common.AppDatePickerDialog
import com.friend.ui.components.AppOutlineTextField
import com.friend.designsystem.R as Res
import com.friend.ui.components.SingleChoiceSegmentsWithIcons

@Composable
fun GenderSelection(
    modifier: Modifier = Modifier
) {
    var segIndex by remember { mutableIntStateOf(0) }
    SingleChoiceSegmentsWithIcons(
        modifier = modifier,
        title = stringResource(Res.string.label_gender),
        options = listOf(
            Pair(Icons.Rounded.Male, "Male"),
            Pair(Icons.Rounded.Female, "Female"),
        ),
        selectedIndex = segIndex.coerceIn(0, 1),
        onSelected = { segIndex = it }
    )
}

@Composable
fun InterestedInSelection(
    modifier: Modifier = Modifier
) {
    var segIndex by remember { mutableIntStateOf(0) }
    SingleChoiceSegmentsWithIcons(
        modifier = modifier,
        title = stringResource(Res.string.label_interested_in),
        options = listOf(
            Pair(Icons.Rounded.Male, "Male"),
            Pair(Icons.Rounded.Female, "Female"),
        ),
        selectedIndex = 1,
        onSelected = { segIndex = it }
    )
}

@Composable
fun BirthDateSelection(
    modifier: Modifier = Modifier
) {
    var dateOfBirth by rememberSaveable { mutableStateOf("") }
    var showDatePicker by remember { mutableStateOf(false) }
    AppOutlineTextField(
        text = dateOfBirth,
        modifier = Modifier.fillMaxWidth(),
        title = stringResource(Res.string.label_date_of_birth),
        placeholder = stringResource(Res.string.hint_dob),
        onValueChange = { dateOfBirth = it },
        isReadOnly = true,
        onClickListener = {
            showDatePicker = true
        }
    )

    if (showDatePicker) {
        AppDatePickerDialog(
            onDismissRequest = {
                showDatePicker = false
            },
            onConfirm = {
                dateOfBirth = it
                showDatePicker = false
            }
        )
    }
}