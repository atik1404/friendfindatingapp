package com.friend.profilecompletion

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
import com.friend.profilecompletion.components.PersonalDetails
import com.friend.ui.common.AppToolbar
import com.friend.ui.components.AppChipMultiWithTitle
import com.friend.ui.components.AppElevatedButton
import com.friend.ui.components.AppOutlineTextField
import com.friend.ui.components.AppScaffold
import com.friend.ui.preview.LightPreview
import com.friend.designsystem.R as Res

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileCompletionScreen(
    state: UiState,
    onEvent: (UiAction) -> Unit,
    onBackButtonClicked: () -> Unit,
) {
    AppScaffold(
        contentWindowInsets = WindowInsets.safeDrawing,
        topBar = {
            AppToolbar(
                title = stringResource(Res.string.title_personal_information),
                onBackClick = {
                    onBackButtonClicked.invoke()
                })
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()              // from Scaffold
                .padding(padding)
                .consumeWindowInsets(padding)    // prevent double-inset consumption downstream
                .navigationBarsPadding()         // keep content above system nav bar
                .imePadding()                    // lift content when keyboard shows
                .verticalScroll(rememberScrollState()) // simple, contents are small; LazyColumn not necessary
                .appPadding(SpacingToken.small),
        ) {
            PersonalDetails(
                initialSelectionData = state.selectionData,
                height = state.height,
                weight = state.weight,
                eyes = state.eyes,
                hair = state.hair,
                smoking = state.smoking,
                drinking = state.drinking,
                bodyType = state.bodyType,
                lookingFor = state.lookingFor,
                onHeightChange = {
                    onEvent(UiAction.HeightChanged(it))
                },
                onWeightChange = {
                    onEvent(UiAction.WeightChanged(it))
                },
                onEyesChange = {
                    onEvent(UiAction.EyesChanged(it))
                },
                onHairChange = {
                    onEvent(UiAction.HairChanged(it))
                },
                onSmokingChange = {
                    onEvent(UiAction.SmokingChanged(it))
                },
                onDrinkingChange = {
                    onEvent(UiAction.DrinkingChanged(it))
                },
                onBodyTypeChange = {
                    onEvent(UiAction.BodyTypeChanged(it))
                },
                onLookingForChange = {
                    onEvent(UiAction.LookingForChanged(it))
                },
            )

            Spacer(modifier = Modifier.height(SpacingToken.medium))

            AppOutlineTextField(
                text = state.title.value,
                error = if (state.title.isDirty) stringResource(Res.string.error_invalid_title) else null,
                modifier = Modifier.fillMaxWidth(),
                title = stringResource(Res.string.label_title),
                placeholder = stringResource(Res.string.hint_title),
                onValueChange = {
                    onEvent(UiAction.TitleChanged(it))
                },
                keyboardOptions = KeyboardOptions(
                    imeAction = ImeAction.Next,
                    autoCorrectEnabled = false,
                ),
            )

            Spacer(modifier = Modifier.height(SpacingToken.medium))

            AppOutlineTextField(
                text = state.aboutYou.value,
                error = if (state.aboutYou.isDirty) stringResource(Res.string.error_invalid_about_you) else null,
                modifier = Modifier.fillMaxWidth(),
                maxLines = 3,
                title = stringResource(Res.string.label_about_yourself),
                placeholder = stringResource(Res.string.hint_about_you),
                onValueChange = {
                    onEvent(UiAction.AboutYouChanged(it))
                },
                keyboardOptions = KeyboardOptions(
                    imeAction = ImeAction.Next,
                    autoCorrectEnabled = false,
                ),
            )

            Spacer(modifier = Modifier.height(SpacingToken.medium))

            AppOutlineTextField(
                text = state.whatsUp.value,
                error = if (state.whatsUp.isDirty) stringResource(Res.string.error_invalid_whats_up) else null,
                modifier = Modifier.fillMaxWidth(),
                maxLines = 3,
                title = stringResource(Res.string.label_whats_up),
                placeholder = stringResource(Res.string.hint_whats_up),
                onValueChange = {
                    onEvent(UiAction.WhatsUpChanged(it))
                },
                keyboardOptions = KeyboardOptions(
                    imeAction = ImeAction.Done,
                    autoCorrectEnabled = false,
                ),
            )


            Spacer(modifier = Modifier.height(SpacingToken.medium))

            AppChipMultiWithTitle(
                title = stringResource(Res.string.label_interest),
                items = state.selectionData.interests,
                selected = state.interests.toSet(),
                label = { it },
                onSelectionChange = {
                    onEvent(UiAction.InterestsChanged(it))
                }
            )

            Spacer(modifier = Modifier.height(SpacingToken.medium))

            AppElevatedButton(
                modifier = Modifier.fillMaxWidth(),
                text = stringResource(Res.string.action_save),
                isLoading = state.isSubmitting,
                onClick = {
                    onEvent(UiAction.FormSubmit)
                }
            )
        }
    }
}

@Composable
@LightPreview
private fun ScreenPreview() {
    ProfileCompletionScreen(
        onBackButtonClicked = {},
        state = UiState(),
        onEvent = {}
    )
}