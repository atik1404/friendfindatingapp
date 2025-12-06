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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
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
import com.friend.ui.components.AppCheckbox
import com.friend.ui.components.AppElevatedButton
import com.friend.ui.components.AppScaffold
import com.friend.ui.preview.LightPreview
import timber.log.Timber
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
                userName = state.form.username.value,
                fullName = state.form.name,
                isInvalidUserName = !state.form.username.isValid,
                onUserNameChange = {
                    uiAction.invoke(UiAction.OnChangeUserName(it))
                },
                onFullNameChange = {

                }
            )

            Spacer(modifier = Modifier.height(SpacingToken.medium))

            EmailSection(modifier = modifier)

            Spacer(modifier = Modifier.height(SpacingToken.medium))

            PasswordSection(modifier = modifier)

            Spacer(modifier = Modifier.height(SpacingToken.medium))

            GenderSelection(modifier = modifier)

            Spacer(modifier = Modifier.height(SpacingToken.medium))

            InterestedInSelection(modifier = modifier)

            Spacer(modifier = Modifier.height(SpacingToken.medium))

            BirthDateSelection(modifier = modifier)

            Spacer(modifier = Modifier.height(SpacingToken.medium))

            AddressSection(modifier = modifier)

            Spacer(modifier = Modifier.height(SpacingToken.medium))

            var checked by rememberSaveable { mutableStateOf(false) }

            AppCheckbox(
                checked = checked,
                onCheckedChange = { checked = it },
                label = stringResource(Res.string.msg_agree_with_term_condition)
            )

            Spacer(modifier = modifier.height(SpacingToken.medium))

            AppElevatedButton(
                modifier = modifier.fillMaxWidth(),
                enabled = checked,
                text = stringResource(Res.string.action_sign_up),
                onClick = {
                    uiAction.invoke(UiAction.FormValidation)
                },
            )
        }
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