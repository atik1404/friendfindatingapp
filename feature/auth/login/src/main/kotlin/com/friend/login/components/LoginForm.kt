package com.friend.login.components

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import com.friend.designsystem.spacing.RadiusToken
import com.friend.designsystem.spacing.SpacingToken
import com.friend.designsystem.spacing.StrokeTokens
import com.friend.designsystem.spacing.appPadding
import com.friend.designsystem.theme.strokeColors
import com.friend.designsystem.theme.textColors
import com.friend.designsystem.typography.AppTypography
import com.friend.domain.base.TextInput
import com.friend.ui.components.AppElevatedButton
import com.friend.ui.components.AppOutlineTextField
import com.friend.ui.components.AppTextButton
import com.friend.ui.components.ColoredTextSegment
import com.friend.ui.components.MultiColorText
import com.friend.designsystem.R as Res

@Composable
fun LoginForm(
    modifier: Modifier = Modifier,
    username: TextInput,
    password: TextInput,
    isFormSubmitting: Boolean,
    onUsernameChange: (String) -> Unit,
    onPasswordChange: (String) -> Unit,
    onForgotPasswordClick: () -> Unit,
    onSignUpClick: () -> Unit,
    onFormSubmit: () -> Unit,
) {
    Column(
        modifier = modifier
            .appPadding(SpacingToken.medium)
            .border(
                width = StrokeTokens.thin,
                color = MaterialTheme.strokeColors.primary,
                shape = RoundedCornerShape(RadiusToken.large)
            )
            .appPadding(SpacingToken.medium),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        AppOutlineTextField(
            text = username.value,
            error = if (username.isDirty)
                stringResource(Res.string.error_invalid_username) else null,
            modifier = Modifier.fillMaxWidth(),
            title = stringResource(Res.string.label_username),
            placeholder = stringResource(Res.string.hint_user_name),
            onValueChange = onUsernameChange,
            keyboardOptions = KeyboardOptions(
                imeAction = ImeAction.Next,
            ),
        )

        Spacer(Modifier.height(SpacingToken.medium))

        AppOutlineTextField(
            text = password.value,
            error = if (password.isDirty)
                stringResource(Res.string.error_invalid_password) else null,
            modifier = Modifier.fillMaxWidth(),
            title = stringResource(Res.string.label_password),
            placeholder = stringResource(Res.string.hint_password),
            onValueChange = onPasswordChange,
            isPassword = true,
            keyboardOptions = KeyboardOptions(
                imeAction = ImeAction.Done,
            ),
        )

        Spacer(modifier = Modifier.height(SpacingToken.extraLarge))

        AppElevatedButton(
            modifier = Modifier.fillMaxWidth(),
            text = stringResource(Res.string.action_login),
            isLoading = isFormSubmitting,
            onClick = onFormSubmit,
        )

        AppTextButton(
            text = stringResource(Res.string.action_forgot_password),
            modifier = Modifier.wrapContentWidth(Alignment.End),
            onClick = onForgotPasswordClick
        )

        SignUpContent {
            onSignUpClick.invoke()
        }
    }
}

@Composable
private fun SignUpContent(
    onSignUpClick: () -> Unit,
) {
    val segments = listOf(
        ColoredTextSegment(
            text = stringResource(Res.string.action_no_account),
            color = MaterialTheme.textColors.secondary,
            style = AppTypography.bodyMedium,
            addSpace = true
        ),
        ColoredTextSegment(
            text = stringResource(Res.string.action_sign_up),
            color = MaterialTheme.textColors.brand,
            style = AppTypography.bodyMedium.copy(fontWeight = FontWeight.Bold),
            onClick = onSignUpClick
        ),
    )

    MultiColorText(segments = segments)
}
