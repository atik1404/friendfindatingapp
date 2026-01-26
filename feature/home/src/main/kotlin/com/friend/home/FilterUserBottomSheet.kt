package com.friend.home

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import com.friend.common.constant.AppConstants
import com.friend.common.constant.Gender
import com.friend.designsystem.spacing.RadiusToken
import com.friend.designsystem.spacing.SpacingToken
import com.friend.designsystem.spacing.appPadding
import com.friend.designsystem.spacing.appPaddingHorizontal
import com.friend.designsystem.theme.buttonColors
import com.friend.designsystem.theme.textColors
import com.friend.designsystem.typography.AppTypography
import com.friend.ui.common.GenderSelection
import com.friend.ui.common.bottomsheet.ShowBottomSheet
import com.friend.ui.components.AppBaseTextField
import com.friend.ui.components.AppCheckbox
import com.friend.ui.components.AppElevatedButton
import com.friend.ui.components.AppOutlineTextField
import com.friend.ui.components.AppOutlinedButton
import com.friend.ui.components.AppText
import com.friend.ui.components.AutoCompleteTextField
import com.friend.ui.preview.LightPreview
import com.friend.designsystem.R as Res

@Composable
fun FilterUserBottomSheet(
    modifier: Modifier = Modifier,
    filterUiState: FilterUiState,
    onAction: (UiAction) -> Unit,
    onDismissRequest: () -> Unit = {},
    onSearchApply: () -> Unit
) {
    ShowBottomSheet(
        heightRatio = .7f,
        cancellable = false,
        onDismissRequest = onDismissRequest,
        title = stringResource(Res.string.title_individual_search),
        titleColor = MaterialTheme.textColors.primary
    ) {
        FilterUi(
            modifier = modifier,
            filterUiState = filterUiState,
            onAction = onAction,
            onResetFilter = {
                onAction.invoke(UiAction.ResetFilter)
                onDismissRequest.invoke()
            },
            onSearchApply = {
                onSearchApply.invoke()
            }
        )
    }
}

@Composable
private fun FilterUi(
    modifier: Modifier = Modifier,
    filterUiState: FilterUiState,
    onAction: (UiAction) -> Unit,
    onSearchApply: () -> Unit,
    onResetFilter: () -> Unit,
) {
    Box(
        modifier = modifier
            .appPadding(SpacingToken.medium)
    ) {
        Column(
            modifier = modifier
                .verticalScroll(rememberScrollState())
        ) {
            AppOutlineTextField(
                text = filterUiState.username,
                modifier = Modifier.fillMaxWidth(),
                title = stringResource(Res.string.label_username),
                placeholder = stringResource(Res.string.hint_user_name),
                onValueChange = { onAction.invoke(UiAction.OnChangeUsername(it)) },
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Text,
                    imeAction = ImeAction.Next,
                ),
            )

            Spacer(Modifier.height(SpacingToken.medium))

            AppText(
                text = stringResource(Res.string.label_age_range),
                textStyle = AppTypography.bodySmall,
                fontWeight = FontWeight.Light,
                modifier = Modifier,
                textColor = MaterialTheme.textColors.secondary
            )
            Spacer(Modifier.height(SpacingToken.micro))

            AgeRangeUi(
                minAge = filterUiState.fromAge,
                maxAge = filterUiState.toAge,
                onFromAgeChange = { onAction.invoke(UiAction.OnChangeFromAge(it)) },
                onToAgeChange = { onAction.invoke(UiAction.OnChangeToAge(it)) },
            )

            Spacer(Modifier.height(SpacingToken.medium))

            GenderSelection(
                title = stringResource(Res.string.label_am_looking_for),
                selectedValue = Gender.fromValue(filterUiState.gender).name,
            ) {
                onAction.invoke(UiAction.OnChangeGender(it))
            }

            Spacer(modifier = Modifier.height(SpacingToken.medium))

            GenderSelection(
                title = stringResource(Res.string.label_seeking_for),
                selectedValue = Gender.fromValue(filterUiState.interestedIn).name,
            ) {
                onAction.invoke(UiAction.OnChangeInterested(it))
            }

            //Spacer(modifier = Modifier.height(SpacingToken.medium))
            //AddressField()

            Spacer(modifier = Modifier.height(SpacingToken.medium))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                AutoCompleteTextField(
                    allOptions = AppConstants.BodyTypes,
                    modifier = Modifier.weight(1f),
                    label = stringResource(Res.string.label_body_type),
                    placeholder = stringResource(Res.string.hint_select_item),
                    onValueChange = { onAction.invoke(UiAction.BodyTypeChanged(it)) },
                    value = filterUiState.bodyType ?: ""
                )

                Spacer(modifier = Modifier.width(SpacingToken.medium))

                AutoCompleteTextField(
                    allOptions = AppConstants.LookingFor,
                    modifier = Modifier.weight(1f),
                    label = stringResource(Res.string.label_looking_for),
                    placeholder = stringResource(Res.string.hint_select_item),
                    onValueChange = { onAction.invoke(UiAction.LookingForChanged(it)) },
                    value = filterUiState.lookingFor ?: ""
                )
            }
            Spacer(Modifier.height(SpacingToken.medium))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                AutoCompleteTextField(
                    allOptions = AppConstants.Height,
                    modifier = Modifier.weight(1f),
                    label = stringResource(Res.string.label_height),
                    placeholder = stringResource(Res.string.hint_select_item),
                    onValueChange = { onAction.invoke(UiAction.HeightChanged(it)) },
                    value = filterUiState.height ?: ""
                )

                Spacer(modifier = Modifier.width(SpacingToken.medium))

                AutoCompleteTextField(
                    allOptions = AppConstants.Weight,
                    modifier = Modifier.weight(1f),
                    label = stringResource(Res.string.label_weight),
                    placeholder = stringResource(Res.string.hint_select_item),
                    onValueChange = { onAction.invoke(UiAction.WeightChanged(it)) },
                    value = filterUiState.weight ?: ""
                )
            }

            Spacer(Modifier.height(SpacingToken.medium))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                AutoCompleteTextField(
                    allOptions = AppConstants.Eyes,
                    modifier = Modifier.weight(1f),
                    label = stringResource(Res.string.label_eyes),
                    placeholder = stringResource(Res.string.hint_select_item),
                    onValueChange = { onAction.invoke(UiAction.EyesChanged(it)) },
                    value = filterUiState.eyes ?: ""
                )

                Spacer(modifier = Modifier.width(SpacingToken.medium))

                AutoCompleteTextField(
                    allOptions = AppConstants.Hairs,
                    modifier = Modifier.weight(1f),
                    label = stringResource(Res.string.label_hair),
                    placeholder = stringResource(Res.string.hint_select_item),
                    onValueChange = { onAction.invoke(UiAction.HairChanged(it)) },
                    value = filterUiState.hair ?: ""
                )
            }

            Spacer(Modifier.height(SpacingToken.medium))

            Row(
                modifier = Modifier
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                AutoCompleteTextField(
                    allOptions = AppConstants.Smoking,
                    modifier = Modifier.weight(1f),
                    label = stringResource(Res.string.label_smoking),
                    placeholder = stringResource(Res.string.hint_select_item),
                    onValueChange = { onAction.invoke(UiAction.SmokingChanged(it)) },
                    value = filterUiState.smoking ?: ""
                )

                Spacer(modifier = Modifier.width(SpacingToken.medium))

                AutoCompleteTextField(
                    allOptions = AppConstants.Drinking,
                    modifier = Modifier.weight(1f),
                    label = stringResource(Res.string.label_drinking),
                    placeholder = stringResource(Res.string.hint_select_item),
                    onValueChange = { onAction.invoke(UiAction.DrinkingChanged(it)) },
                    value = filterUiState.drinking ?: ""
                )
            }

            Spacer(modifier = Modifier.height(SpacingToken.medium))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                AppCheckbox(
                    checked = filterUiState.isOnlineUser,
                    onCheckedChange = { onAction.invoke(UiAction.OnlineUserChanged(it)) },
                    label = stringResource(Res.string.msg_online_user_only)
                )

                Spacer(modifier = Modifier.height(SpacingToken.extraSmall))

                AppCheckbox(
                    checked = filterUiState.isPhotoRequired,
                    onCheckedChange = { onAction.invoke(UiAction.PhotoRequiredChanged(it)) },
                    label = stringResource(Res.string.msg_photo_required)
                )
            }

            Spacer(modifier = Modifier.height(SpacingToken.huge))
            Spacer(modifier = Modifier.height(SpacingToken.huge))
        }

        Row(
            modifier = modifier
                .fillMaxWidth()
                .align(alignment = Alignment.BottomCenter),
        ) {
            AppOutlinedButton(
                modifier = modifier.weight(1f),
                text = stringResource(Res.string.action_reset),
                colors = MaterialTheme.buttonColors.outlineButton,
                onClick = onResetFilter,
            )

            Spacer(modifier = modifier.width(SpacingToken.medium))

            AppElevatedButton(
                modifier = modifier.weight(1f),
                text = stringResource(Res.string.action_search),
                onClick = onSearchApply,
            )
        }
    }
}

@Composable
private fun AgeRangeUi(
    modifier: Modifier = Modifier,
    minAge: String = "",
    maxAge: String = "",
    onFromAgeChange: (String) -> Unit,
    onToAgeChange: (String) -> Unit,
) {
    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        AppBaseTextField(
            value = minAge,
            modifier = modifier.weight(1f),
            placeholder = stringResource(Res.string.hint_min),
            onValueChange = { onFromAgeChange.invoke(it) },
            shape = RoundedCornerShape(RadiusToken.large),
            maxLength = 3,
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Number,
                imeAction = ImeAction.Next,
            ),
        )

        AppText(
            text = stringResource(Res.string.hint_to),
            textStyle = AppTypography.bodySmall,
            fontWeight = FontWeight.Light,
            modifier = modifier.appPaddingHorizontal(SpacingToken.medium),
            textColor = MaterialTheme.textColors.secondary
        )

        AppBaseTextField(
            value = maxAge,
            modifier = modifier.weight(1f),
            placeholder = stringResource(Res.string.hint_max),
            shape = RoundedCornerShape(RadiusToken.large),
            maxLength = 3,
            onValueChange = { onToAgeChange.invoke(it) },
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Number,
                imeAction = ImeAction.Next,
            ),
        )
    }
}

@Composable
private fun AddressField() {
    var text by remember { mutableStateOf("") }

    val cities = listOf(
        "Dhaka",
        "Chattogram",
        "Rajshahi",
        "Khulna",
        "Sylhet",
        "Barishal",
        "Rangpur",
        "Mymensingh"
    )
    Column(
        modifier = Modifier.fillMaxWidth()
    ) {
        AutoCompleteTextField(
            allOptions = cities,
            modifier = Modifier.fillMaxWidth(),
            label = stringResource(Res.string.label_country),
            placeholder = stringResource(Res.string.hint_select_item),
            onValueChange = { text = it },
            value = text
        )

        Spacer(modifier = Modifier.height(SpacingToken.medium))

        Row {
            AutoCompleteTextField(
                allOptions = cities,
                modifier = Modifier.weight(1f),
                label = stringResource(Res.string.label_state),
                placeholder = stringResource(Res.string.hint_select_item),
                onValueChange = { text = it },
                value = text
            )

            Spacer(modifier = Modifier.width(SpacingToken.medium))

            AutoCompleteTextField(
                allOptions = cities,
                modifier = Modifier.weight(1f),
                label = stringResource(Res.string.label_city),
                placeholder = stringResource(Res.string.hint_select_item),
                onValueChange = { text = it },
                value = text
            )
        }
    }
}

@Composable
@LightPreview
private fun ScreenPreview() {
    FilterUi(
        filterUiState = FilterUiState(),
        onAction = {},
        onSearchApply = {},
        onResetFilter = {},
    )
}