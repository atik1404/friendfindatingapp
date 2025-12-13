package com.friend.profilecompletion.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.friend.designsystem.spacing.SpacingToken
import com.friend.profilecompletion.SelectionData
import com.friend.ui.components.AutoCompleteTextField
import com.friend.designsystem.R as Res

@Composable
fun PersonalDetails(
    modifier: Modifier = Modifier,
    initialSelectionData: SelectionData,
    height: String,
    weight: String,
    eyes: String,
    hair: String,
    smoking: String,
    drinking: String,
    bodyType: String,
    lookingFor: String,
    onHeightChange: (String) -> Unit,
    onWeightChange: (String) -> Unit,
    onEyesChange: (String) -> Unit,
    onHairChange: (String) -> Unit,
    onSmokingChange: (String) -> Unit,
    onDrinkingChange: (String) -> Unit,
    onBodyTypeChange: (String) -> Unit,
    onLookingForChange: (String) -> Unit,
) {
    Column(
        modifier = modifier.fillMaxWidth()
    ) {
        Row(
            modifier = modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            AutoCompleteTextField(
                allOptions = initialSelectionData.height,
                modifier = modifier.weight(1f),
                label = stringResource(Res.string.label_height),
                placeholder = stringResource(Res.string.hint_select_item),
                onValueChange = onHeightChange,
                value = height
            )

            Spacer(modifier = modifier.width(SpacingToken.medium))

            AutoCompleteTextField(
                allOptions = initialSelectionData.weight,
                modifier = modifier.weight(1f),
                label = stringResource(Res.string.label_weight),
                placeholder = stringResource(Res.string.hint_select_item),
                onValueChange = onWeightChange,
                value = weight
            )
        }

        Spacer(modifier.height(SpacingToken.medium))

        Row(
            modifier = modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            AutoCompleteTextField(
                allOptions = initialSelectionData.eyes,
                modifier = modifier.weight(1f),
                label = stringResource(Res.string.label_eyes),
                placeholder = stringResource(Res.string.hint_select_item),
                onValueChange = onEyesChange,
                value = eyes
            )

            Spacer(modifier = modifier.width(SpacingToken.medium))

            AutoCompleteTextField(
                allOptions = initialSelectionData.hair,
                modifier = modifier.weight(1f),
                label = stringResource(Res.string.label_hair),
                placeholder = stringResource(Res.string.hint_select_item),
                onValueChange = onHairChange,
                value = hair
            )
        }

        Spacer(modifier.height(SpacingToken.medium))

        Row(
            modifier = modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            AutoCompleteTextField(
                allOptions = initialSelectionData.smoking,
                modifier = modifier.weight(1f),
                label = stringResource(Res.string.label_smoking),
                placeholder = stringResource(Res.string.hint_select_item),
                onValueChange = onSmokingChange,
                value = smoking
            )

            Spacer(modifier = modifier.width(SpacingToken.medium))

            AutoCompleteTextField(
                allOptions = initialSelectionData.drinking,
                modifier = modifier.weight(1f),
                label = stringResource(Res.string.label_drinking),
                placeholder = stringResource(Res.string.hint_select_item),
                onValueChange = onDrinkingChange,
                value = drinking
            )
        }

        Spacer(modifier.height(SpacingToken.medium))

        Row(
            modifier = modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            AutoCompleteTextField(
                allOptions = initialSelectionData.bodyType,
                modifier = modifier.weight(1f),
                label = stringResource(Res.string.label_body_type),
                placeholder = stringResource(Res.string.hint_select_item),
                onValueChange = onBodyTypeChange,
                value = bodyType
            )

            Spacer(modifier = modifier.width(SpacingToken.medium))

            AutoCompleteTextField(
                allOptions = initialSelectionData.lookingFor,
                modifier = modifier.weight(1f),
                label = stringResource(Res.string.label_looking_for),
                placeholder = stringResource(Res.string.hint_select_item),
                onValueChange = onLookingForChange,
                value = lookingFor
            )
        }
    }
}