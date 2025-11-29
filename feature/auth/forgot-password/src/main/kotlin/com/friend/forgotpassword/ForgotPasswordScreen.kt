package com.friend.forgotpassword

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
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.friend.designsystem.spacing.SpacingToken
import com.friend.designsystem.spacing.appPadding
import com.friend.designsystem.theme.textColors
import com.friend.designsystem.typography.AppTypography
import com.friend.ui.common.AppToolbar
import com.friend.ui.components.AppElevatedButton
import com.friend.ui.components.AppOutlineTextField
import com.friend.ui.components.AppScaffold
import com.friend.ui.components.AppText
import com.friend.ui.components.LocalImageLoader
import com.friend.ui.preview.LightPreview
import com.friend.designsystem.R as Res


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ForgotPasswordScreen(
    state: UiState,
    onEvent: (UiEvent) -> Unit,
    onNavigateBack: () -> Unit
) {

    AppScaffold(
        contentWindowInsets = WindowInsets.safeDrawing,
        topBar = {
            AppToolbar(
                title = stringResource(Res.string.title_forgot_password),
                onBackClick = {
                    onNavigateBack.invoke()
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
                .appPadding(SpacingToken.large)
                .verticalScroll(rememberScrollState()), // Allow scroll on small screens,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            LocalImageLoader(
                imageResId = Res.drawable.img_login_illustration,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp),
                contentScale = ContentScale.Fit
            )

            Spacer(Modifier.height(SpacingToken.large))

            AppText(
                text = stringResource(Res.string.msg_reset_your_password),
                textStyle = AppTypography.headlineSmall,
                fontWeight = FontWeight.Bold,
                modifier = Modifier
            )

            Spacer(Modifier.height(SpacingToken.medium))

            AppText(
                text = stringResource(Res.string.msg_forgot_password_instruction),
                fontWeight = FontWeight.Light,
                modifier = Modifier,
                maxLines = 3
            )

            Spacer(Modifier.height(SpacingToken.large))
            AppOutlineTextField(
                text = state.email,
                error = if (!state.isEmailValid) stringResource(Res.string.error_invalid_email) else null,
                modifier = Modifier.fillMaxWidth(),
                title = stringResource(Res.string.label_email),
                placeholder = stringResource(Res.string.hint_email),
                onValueChange = { onEvent(UiEvent.EmailChanged(it)) },
                keyboardOptions = KeyboardOptions(
                    imeAction = ImeAction.Done,
                    keyboardType = KeyboardType.Email
                ),
            )

            Spacer(modifier = Modifier.height(SpacingToken.large))

            AppElevatedButton(
                modifier = Modifier.fillMaxWidth(),
                text = stringResource(Res.string.action_send_link),
                isLoading = state.isLoading,
                onClick = {
                    onEvent(UiEvent.FormValidator)
                },
            )

            Spacer(modifier = Modifier.height(SpacingToken.large))

            AppText(
                text = stringResource(Res.string.msg_copyright),
                fontWeight = FontWeight.Light,
                textStyle = AppTypography.bodySmall,
                textColor = MaterialTheme.textColors.secondary,
                alignment = TextAlign.Center
            )
        }
    }
}

@Composable
@LightPreview
private fun ScreenPreview() {
    ForgotPasswordScreen(
        state = UiState(),
        onEvent = {},
        onNavigateBack = {}
    )
}