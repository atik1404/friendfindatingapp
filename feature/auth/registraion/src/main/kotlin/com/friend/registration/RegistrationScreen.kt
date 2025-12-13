package com.friend.registration

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
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.friend.designsystem.spacing.SpacingToken
import com.friend.designsystem.spacing.appPadding
import com.friend.registration.component.AddressSection
import com.friend.registration.component.BirthDateSelection
import com.friend.registration.component.EmailSection
import com.friend.registration.component.GenderSelection
import com.friend.registration.component.InterestedInSelection
import com.friend.registration.component.NameSection
import com.friend.registration.component.PasswordSection
import com.friend.ui.common.AppToolbar
import com.friend.ui.common.LoadingUi
import com.friend.ui.components.AppCheckbox
import com.friend.ui.components.AppElevatedButton
import com.friend.ui.components.AppScaffold
import com.friend.ui.preview.LightPreview
import com.friend.designsystem.R as Res

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RegistrationScreen(
    state: UiState,
    uiAction: (UiAction) -> Unit,
    modifier: Modifier = Modifier,
    onBackButtonClicked: () -> Unit,
) {
    AppScaffold(
        contentWindowInsets = WindowInsets.safeDrawing,
        topBar = {
            AppToolbar(
                title = stringResource(Res.string.title_registration),
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

            NameSection(
                modifier = modifier,
                userName = state.form.username,
                fullName = state.form.name,
                onUserNameChange = {
                    uiAction.invoke(UiAction.OnChangeUserName(it))
                },
                onFullNameChange = {
                    uiAction.invoke(UiAction.OnChangeName(it))
                }
            )

            Spacer(modifier = modifier.height(SpacingToken.medium))

            EmailSection(
                text = state.form.email,
                onValueChange = {
                    uiAction.invoke(UiAction.OnChangeEmail(it))
                },
                modifier = modifier,
            )

            Spacer(modifier = modifier.height(SpacingToken.medium))

            PasswordSection(
                text = state.form.password,
                onValueChange = {
                    uiAction.invoke(UiAction.OnChangePassword(it))
                },
                modifier = modifier
            )

            Spacer(modifier = modifier.height(SpacingToken.medium))

            GenderSelection(
                modifier = modifier,
                selectedGender = state.form.gender?.name ?: "",
                onSelected = {
                    uiAction.invoke(UiAction.SelectGender(it))
                }
            )

            Spacer(modifier = modifier.height(SpacingToken.medium))

            InterestedInSelection(
                modifier = modifier,
                selectedGender = state.form.interestedIn?.name ?: "",
                onSelected = {
                    uiAction.invoke(UiAction.SelectInterestedIn(it))
                }
            )

            Spacer(modifier = modifier.height(SpacingToken.medium))

            BirthDateSelection(
                modifier = modifier,
                selectedDate = state.form.dateOfBirth.value,
                onSelected = {
                    uiAction.invoke(UiAction.SelectBirthDate(it))
                },
                showDatePicker = state.showDatePicker,
                setShowDatePicker = {
                    uiAction.invoke(UiAction.ShowDatePicker(it))
                }
            )

            Spacer(modifier = modifier.height(SpacingToken.medium))

            AddressSection(
                modifier = modifier,
                postCode = state.form.postCode,
                selectedCountry = state.form.country?.name ?: "",
                selectedState = state.form.state?.name ?: "",
                selectedCity = state.form.city?.name ?: "",
                countries = state.countries,
                states = state.states,
                cities = state.cities,
                onPostCodeChange = {
                    uiAction.invoke(UiAction.OnChangePostCode(it))
                },
                onCountryChange = {
                    uiAction.invoke(UiAction.OnSelectCountry(it))
                },
                onStateChange = {
                    uiAction.invoke(UiAction.OnSelectState(it))
                },
                onCityChange = {
                    uiAction.invoke(UiAction.OnSelectCity(it))
                }
            )

            Spacer(modifier = modifier.height(SpacingToken.medium))

            AppCheckbox(
                checked = state.form.isAgree,
                onCheckedChange = {
                    uiAction.invoke(UiAction.CheckPrivacyPolicy(it))
                },
                label = stringResource(Res.string.msg_agree_with_term_condition)
            )

            Spacer(modifier = modifier.height(SpacingToken.medium))

            AppElevatedButton(
                modifier = modifier.fillMaxWidth(),
                enabled = state.form.isAgree,
                text = stringResource(Res.string.action_sign_up),
                isLoading = state.isSubmitting,
                onClick = {
                    uiAction.invoke(UiAction.FormValidation)
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
    RegistrationScreen(
        onBackButtonClicked = {},
        state = UiState(),
        uiAction = {}
    )
}