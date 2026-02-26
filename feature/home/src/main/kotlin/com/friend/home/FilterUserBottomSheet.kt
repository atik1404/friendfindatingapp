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
import com.friend.home.components.AddressSection
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
    locationState: LocationState,
    onAction: (UiAction) -> Unit,
    onDismissRequest: () -> Unit = {},
    onSearchApply: (FilterUiState) -> Unit,
) {
    ShowBottomSheet(
        heightRatio = .8f,
        cancellable = false,
        isExpand = true,
        onDismissRequest = onDismissRequest,
        title = stringResource(Res.string.title_individual_search),
        titleColor = MaterialTheme.textColors.primary
    ) {
        FilterUi(
            modifier = modifier,
            onAction = onAction,
            filterUiState = filterUiState,
            locationState = locationState,
            onResetFilter = {
                onAction.invoke(UiAction.ResetFilter)
                onDismissRequest.invoke()
            },
            onSearchApply = { state ->
                onSearchApply.invoke(state)
            }
        )
    }
}

@Composable
private fun FilterUi(
    modifier: Modifier = Modifier,
    filterUiState: FilterUiState,
    locationState: LocationState,
    onAction: (UiAction) -> Unit,
    onSearchApply: (FilterUiState) -> Unit,
    onResetFilter: () -> Unit,
) {
    var filter by remember(filterUiState) { mutableStateOf(filterUiState) }

    Box(
        modifier = modifier
            .appPadding(SpacingToken.medium)
    ) {
        Column(
            modifier = modifier
                .verticalScroll(rememberScrollState())
        ) {
            AppOutlineTextField(
                text = filter.username,
                modifier = Modifier.fillMaxWidth(),
                title = stringResource(Res.string.label_username),
                placeholder = stringResource(Res.string.hint_user_name),
                onValueChange = { filter = filter.copy(username = it) },
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
                minAge = filter.fromAge,
                maxAge = filter.toAge,
                onFromAgeChange = { filter = filter.copy(fromAge = it) },
                onToAgeChange = { filter = filter.copy(toAge = it) },
            )

            Spacer(Modifier.height(SpacingToken.medium))

            GenderSelection(
                title = stringResource(Res.string.label_am_looking_for),
                selectedValue = Gender.fromValue(filter.gender).name,
            ) {
                filter = filter.copy(gender = it.value)
            }

            Spacer(modifier = Modifier.height(SpacingToken.medium))

            GenderSelection(
                title = stringResource(Res.string.label_seeking_for),
                selectedValue = Gender.fromValue(filter.interestedIn).name,
            ) {
                filter = filter.copy(interestedIn = it.value)
            }

            Spacer(modifier = Modifier.height(SpacingToken.medium))

            AddressSection(
                modifier = modifier,
                selectedCountry = filter.country,
                selectedState = filter.state,
                selectedCity = filter.city,
                countries = locationState.countries,
                states = locationState.states,
                cities = locationState.cities,
                onCountryChange = {
                    onAction.invoke(UiAction.FetchState(it.value))
                    filter = filter.copy(
                        country = it.value,
                        state = "",
                        city = "",
                    )
                },
                onStateChange = {
                    onAction.invoke(
                        UiAction.FetchCity(
                            country = filter.country,
                            state = it.value
                        )
                    )
                    filter =
                        filter.copy(state = it.value.ifEmpty { AppConstants.STATE_ALL }, city = "")
                },
                onCityChange = {
                    filter = filter.copy(city = it.value)
                }
            )

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
                    onValueChange = { filter = filter.copy(bodyType = it) },
                    value = filter.bodyType ?: ""
                )

                Spacer(modifier = Modifier.width(SpacingToken.medium))

                AutoCompleteTextField(
                    allOptions = AppConstants.LookingFor,
                    modifier = Modifier.weight(1f),
                    label = stringResource(Res.string.label_looking_for),
                    placeholder = stringResource(Res.string.hint_select_item),
                    onValueChange = { filter = filter.copy(lookingFor = it) },
                    value = filter.lookingFor ?: ""
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
                    onValueChange = { filter = filter.copy(eyes = it) },
                    value = filter.eyes ?: ""
                )

                Spacer(modifier = Modifier.width(SpacingToken.medium))

                AutoCompleteTextField(
                    allOptions = AppConstants.Hairs,
                    modifier = Modifier.weight(1f),
                    label = stringResource(Res.string.label_hair),
                    placeholder = stringResource(Res.string.hint_select_item),
                    onValueChange = { filter = filter.copy(hair = it) },
                    value = filter.hair ?: ""
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
                    onValueChange = { filter = filter.copy(smoking = it) },
                    value = filter.smoking ?: ""
                )

                Spacer(modifier = Modifier.width(SpacingToken.medium))

                AutoCompleteTextField(
                    allOptions = AppConstants.Drinking,
                    modifier = Modifier.weight(1f),
                    label = stringResource(Res.string.label_drinking),
                    placeholder = stringResource(Res.string.hint_select_item),
                    onValueChange = { filter = filter.copy(drinking = it) },
                    value = filter.drinking ?: ""
                )
            }

            Spacer(modifier = Modifier.height(SpacingToken.medium))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                AppCheckbox(
                    checked = filter.isOnlineUser,
                    onCheckedChange = { filter = filter.copy(isOnlineUser = it) },
                    label = stringResource(Res.string.msg_online_user_only)
                )

                Spacer(modifier = Modifier.height(SpacingToken.extraSmall))

                AppCheckbox(
                    checked = filter.isPhotoRequired,
                    onCheckedChange = { filter = filter.copy(isPhotoRequired = it) },
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
                onClick = { onSearchApply.invoke(filter) },
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
@LightPreview
private fun ScreenPreview() {
    FilterUi(
        filterUiState = FilterUiState(),
        onSearchApply = {},
        onResetFilter = {},
        onAction = {},
        locationState = LocationState()
    )
}