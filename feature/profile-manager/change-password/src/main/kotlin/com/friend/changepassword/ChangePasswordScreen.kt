package com.friend.changepassword

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
import com.friend.designsystem.spacing.appPaddingVertical
import com.friend.ui.common.AppToolbar
import com.friend.ui.components.AppElevatedButton
import com.friend.ui.components.AppOutlineTextField
import com.friend.ui.components.AppScaffold
import com.friend.ui.preview.LightPreview
import com.friend.designsystem.R as Res

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChangePasswordScreen(
    uiState: UiState,
    onAction: (UiAction) -> Unit,
    onBackButtonClicked: () -> Unit,
) {
    AppScaffold(
        contentWindowInsets = WindowInsets.safeDrawing,
        topBar = {
            AppToolbar(
                title = stringResource(Res.string.menu_change_password),
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
                .appPaddingVertical(SpacingToken.extraLarge)
                .appPadding(SpacingToken.medium),
        ) {
            AppOutlineTextField(
                text = uiState.oldPassword.value,
                error = if (uiState.oldPassword.isDirty) stringResource(Res.string.error_invalid_password) else null,
                modifier = Modifier.fillMaxWidth(),
                title = stringResource(Res.string.label_current_password),
                placeholder = stringResource(Res.string.hint_current_password),
                onValueChange = { onAction.invoke(UiAction.OnOldPasswordChange(it)) },
                isPassword = true,
                keyboardOptions = KeyboardOptions(
                    imeAction = ImeAction.Next,
                ),
            )

            Spacer(modifier = Modifier.height(SpacingToken.medium))

            AppOutlineTextField(
                text = uiState.newPassword.value,
                error = if (uiState.newPassword.isDirty) stringResource(Res.string.error_invalid_password) else null,
                modifier = Modifier.fillMaxWidth(),
                title = stringResource(Res.string.label_new_password),
                placeholder = stringResource(Res.string.hint_new_password),
                onValueChange = { onAction.invoke(UiAction.OnNewPasswordChange(it)) },
                isPassword = true,
                keyboardOptions = KeyboardOptions(
                    imeAction = ImeAction.Next,
                ),
            )

            Spacer(modifier = Modifier.height(SpacingToken.medium))

            AppOutlineTextField(
                text = uiState.confirmPassword.value,
                error = if (uiState.confirmPassword.isDirty) stringResource(Res.string.error_invalid_password) else null,
                modifier = Modifier.fillMaxWidth(),
                title = stringResource(Res.string.label_confirm_password),
                placeholder = stringResource(Res.string.hint_confirm_password),
                onValueChange = { onAction.invoke(UiAction.OnConfirmPasswordChange(it)) },
                isPassword = true,
                keyboardOptions = KeyboardOptions(
                    imeAction = ImeAction.Done,
                ),
            )

            Spacer(modifier = Modifier.height(SpacingToken.medium))

            AppElevatedButton(
                isLoading = uiState.isFormSubmitting,
                modifier = Modifier.fillMaxWidth(),
                text = stringResource(Res.string.action_change_password),
                onClick = {
                    onAction.invoke(UiAction.PerformPasswordChanged)
                },
            )
        }
    }
}

@Composable
@LightPreview
private fun ScreenPreview() {
    ChangePasswordScreen(
        uiState = UiState(),
        onAction = {},
        onBackButtonClicked = {}
    )
}
