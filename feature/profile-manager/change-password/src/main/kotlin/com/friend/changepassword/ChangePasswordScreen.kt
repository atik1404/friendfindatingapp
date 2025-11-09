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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
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
import com.friend.ui.preview.LightDarkPreview
import com.friend.designsystem.R as Res

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChangePasswordScreen(
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
        var currentPassword by rememberSaveable { mutableStateOf("") }
        var newPassword by rememberSaveable { mutableStateOf("") }
        var confirmPassword by rememberSaveable { mutableStateOf("") }
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
                text = currentPassword,
                modifier = Modifier.fillMaxWidth(),
                title = stringResource(Res.string.label_current_password),
                placeholder = stringResource(Res.string.hint_current_password),
                onValueChange = { currentPassword = it },
                isPassword = true,
                keyboardOptions = KeyboardOptions(
                    imeAction = ImeAction.Done,
                ),
            )

            Spacer(modifier = Modifier.height(SpacingToken.medium))

            AppOutlineTextField(
                text = newPassword,
                modifier = Modifier.fillMaxWidth(),
                title = stringResource(Res.string.label_new_password),
                placeholder = stringResource(Res.string.hint_new_password),
                onValueChange = { newPassword = it },
                isPassword = true,
                keyboardOptions = KeyboardOptions(
                    imeAction = ImeAction.Done,
                ),
            )

            Spacer(modifier = Modifier.height(SpacingToken.medium))

            AppOutlineTextField(
                text = confirmPassword,
                modifier = Modifier.fillMaxWidth(),
                title = stringResource(Res.string.label_confirm_password),
                placeholder = stringResource(Res.string.hint_confirm_password),
                onValueChange = { confirmPassword = it },
                isPassword = true,
                keyboardOptions = KeyboardOptions(
                    imeAction = ImeAction.Done,
                ),
            )

            Spacer(modifier = Modifier.height(SpacingToken.medium))

            AppElevatedButton(
                modifier = Modifier.fillMaxWidth(),
                text = stringResource(Res.string.action_change_password),
                onClick = {
                    onBackButtonClicked.invoke()
                },
            )
        }
    }
}

@Composable
@LightDarkPreview
private fun ScreenPreview() {
    ChangePasswordScreen(
        onBackButtonClicked = {}
    )
}
