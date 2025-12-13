package com.friend.profilecompletion.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.friend.designsystem.R as Res
import com.friend.designsystem.spacing.SpacingToken
import com.friend.ui.components.AutoCompleteTextField

@Composable
fun PersonalDetails() {
    var selectedValue by rememberSaveable { mutableStateOf("") }

    val items = listOf(
        "Item1",
        "Item2",
        "Item3",
        "Item4",
        "Item5",
        "Item6",
        "Item7",
        "Item8"
    )
    Column(
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            AutoCompleteTextField(
                allOptions = items,
                modifier = Modifier.weight(1f),
                label = stringResource(Res.string.label_height),
                placeholder = stringResource(Res.string.hint_select_item),
                onValueChange = { selectedValue = it },
                value = selectedValue
            )

            Spacer(modifier = Modifier.width(SpacingToken.medium))

            AutoCompleteTextField(
                allOptions = items,
                modifier = Modifier.weight(1f),
                label = stringResource(Res.string.label_weight),
                placeholder = stringResource(Res.string.hint_select_item),
                onValueChange = { selectedValue = it },
                value = selectedValue
            )
        }

        Spacer(Modifier.height(SpacingToken.medium))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            AutoCompleteTextField(
                allOptions = items,
                modifier = Modifier.weight(1f),
                label = stringResource(Res.string.label_eyes),
                placeholder = stringResource(Res.string.hint_select_item),
                onValueChange = { selectedValue = it },
                value = selectedValue
            )

            Spacer(modifier = Modifier.width(SpacingToken.medium))

            AutoCompleteTextField(
                allOptions = items,
                modifier = Modifier.weight(1f),
                label = stringResource(Res.string.label_hair),
                placeholder = stringResource(Res.string.hint_select_item),
                onValueChange = { selectedValue = it },
                value = selectedValue
            )
        }

        Spacer(Modifier.height(SpacingToken.medium))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            AutoCompleteTextField(
                allOptions = items,
                modifier = Modifier.weight(1f),
                label = stringResource(Res.string.label_smoking),
                placeholder = stringResource(Res.string.hint_select_item),
                onValueChange = { selectedValue = it },
                value = selectedValue
            )

            Spacer(modifier = Modifier.width(SpacingToken.medium))

            AutoCompleteTextField(
                allOptions = items,
                modifier = Modifier.weight(1f),
                label = stringResource(Res.string.label_drinking),
                placeholder = stringResource(Res.string.hint_select_item),
                onValueChange = { selectedValue = it },
                value = selectedValue
            )
        }

        Spacer(Modifier.height(SpacingToken.medium))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            AutoCompleteTextField(
                allOptions = items,
                modifier = Modifier.weight(1f),
                label = stringResource(Res.string.label_body_type),
                placeholder = stringResource(Res.string.hint_select_item),
                onValueChange = { selectedValue = it },
                value = selectedValue
            )

            Spacer(modifier = Modifier.width(SpacingToken.medium))

            AutoCompleteTextField(
                allOptions = items,
                modifier = Modifier.weight(1f),
                label = stringResource(Res.string.label_looking_for),
                placeholder = stringResource(Res.string.hint_select_item),
                onValueChange = { selectedValue = it },
                value = selectedValue
            )
        }
    }
}