package com.friend.personalsetting

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import com.friend.designsystem.spacing.SpacingToken
import com.friend.designsystem.spacing.appPadding
import com.friend.personalsetting.components.AddressUiSection
import com.friend.personalsetting.components.BirthDateSelection
import com.friend.personalsetting.components.GenderSelection
import com.friend.personalsetting.components.InterestedInSelection
import com.friend.ui.common.AppToolbar
import com.friend.ui.common.LoadingUi
import com.friend.ui.components.AppElevatedButton
import com.friend.ui.components.AppOutlineTextField
import com.friend.ui.components.AppScaffold
import com.friend.ui.preview.LightPreview
import com.friend.designsystem.R as Res

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PersonalSettingScreen(
    modifier: Modifier = Modifier,
    state: UiState,
    onAction: (UiAction) -> Unit,
    onBackButtonClicked: () -> Unit,
) {
    AppScaffold(
        contentWindowInsets = WindowInsets.safeDrawing,
        topBar = {
            AppToolbar(
                title = stringResource(Res.string.menu_personal_setting),
                onBackClick = {
                    onBackButtonClicked.invoke()
                })
        }
    ) { padding ->
        Column(
            modifier = modifier
                .fillMaxSize()              // from Scaffold
                .padding(padding)
                .consumeWindowInsets(padding)    // prevent double-inset consumption downstream
                .navigationBarsPadding()         // keep content above system nav bar
                .imePadding()                    // lift content when keyboard shows
                .verticalScroll(rememberScrollState()) // simple, contents are small; LazyColumn not necessary
                .appPadding(SpacingToken.small),
        ) {
            AppOutlineTextField(
                text = state.form.name.value,
                modifier = modifier.fillMaxWidth(),
                title = stringResource(Res.string.label_full_name),
                placeholder = stringResource(Res.string.hint_full_name),
                onValueChange = { onAction.invoke(UiAction.OnChangeName(it)) },
                keyboardOptions = KeyboardOptions(
                    imeAction = ImeAction.Next,
                ),
            )

            Spacer(modifier = modifier.height(SpacingToken.medium))

            AppOutlineTextField(
                text = state.form.email.value,
                modifier = modifier.fillMaxWidth(),
                title = stringResource(Res.string.label_email),
                placeholder = stringResource(Res.string.hint_email),
                onValueChange = { onAction.invoke(UiAction.OnChangeEmail(it)) },
                keyboardOptions = KeyboardOptions(
                    imeAction = ImeAction.Next,
                ),
            )

            Spacer(modifier = modifier.height(SpacingToken.medium))

            BirthDateSelection(
                selectedDate = state.form.dateOfBirth.value,
                onSelected = {
                    onAction.invoke(UiAction.SelectBirthDate(it))
                },
                showDatePicker = state.showDatePicker,
                setShowDatePicker = {
                    onAction.invoke(UiAction.ShowDatePicker(it))
                }
            )

            Spacer(modifier = modifier.height(SpacingToken.medium))

            GenderSelection(
                selectedGender = state.form.gender?.name ?: "",
                onSelected = {
                    onAction.invoke(UiAction.SelectGender(it))
                }
            )

            Spacer(modifier = modifier.height(SpacingToken.medium))

            InterestedInSelection(
                modifier = modifier,
                selectedGender = state.form.interestedIn?.name ?: "",
                onSelected = {
                    onAction.invoke(UiAction.SelectInterestedIn(it))
                }
            )

            Spacer(modifier = modifier.height(SpacingToken.medium))

            AddressUiSection(
                modifier = modifier,
                postCode = state.form.postCode,
                selectedCountry = state.form.country ?: "",
                selectedState = state.form.state ?: "",
                selectedCity = state.form.city ?: "",
                countries = state.countries,
                states = state.states,
                cities = state.cities,
                onPostCodeChange = {
                    onAction.invoke(UiAction.OnChangePostCode(it))
                },
                onCountryChange = {
                    onAction.invoke(UiAction.OnSelectCountry(it.value))
                },
                onStateChange = {
                    onAction.invoke(UiAction.OnSelectState(it.value))
                },
                onCityChange = {
                    onAction.invoke(UiAction.OnSelectCity(it.value))
                }
            )

            Spacer(modifier = modifier.height(SpacingToken.huge))

            AppElevatedButton(
                modifier = modifier.fillMaxWidth(),
                isLoading = state.isSubmitting,
                text = stringResource(Res.string.action_save_changes),
                onClick = {
                    onAction.invoke(UiAction.PerformProfileUpdate)
                },
            )
        }

        if (state.isLoading)
            LoadingUi()
    }
}

@Composable
@LightPreview
private fun ScreenPreview() {
    PersonalSettingScreen(
        onBackButtonClicked = {},
        state = UiState(),
        onAction = {}
    )
}