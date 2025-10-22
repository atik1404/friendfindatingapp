package com.friend.login

import AppDivider
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import com.friend.designsystem.spacing.RadiusToken
import com.friend.designsystem.spacing.SpacingToken
import com.friend.designsystem.spacing.StrokeTokens
import com.friend.designsystem.spacing.appPadding
import com.friend.designsystem.spacing.appPaddingHorizontal
import com.friend.designsystem.spacing.appPaddingOnly
import com.friend.designsystem.theme.dividerColors
import com.friend.designsystem.theme.strokeColors
import com.friend.designsystem.theme.textColors
import com.friend.designsystem.typography.AppTypography
import com.friend.ui.components.AppElevatedButton
import com.friend.ui.components.AppOutlineTextField
import com.friend.ui.components.AppOutlinedButton
import com.friend.ui.components.AppScaffold
import com.friend.ui.components.AppText
import com.friend.ui.components.AppTextButton
import com.friend.ui.components.LoadLocalImage
import com.friend.ui.preview.LightDarkPreview
import com.friend.designsystem.R as Res

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LoginScreen() {
    AppScaffold(
        contentWindowInsets = WindowInsets(0),
    ) { padding ->
        ConstraintLayout(
            modifier = Modifier.fillMaxSize()
        ) {
            val guideline = createGuidelineFromTop(.3f)
            val (bannerImage, loginTypeDivider, googleLoginBtn, loginUi, copyrightUi) = createRefs()

            LoadLocalImage(
                imageResId = Res.drawable.img_login_illustration,
                modifier = Modifier.constrainAs(bannerImage) {
                    top.linkTo(parent.top, margin = SpacingToken.large)
                    bottom.linkTo(guideline)
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                    width = Dimension.fillToConstraints
                    height = Dimension.fillToConstraints
                },
                contentScale = ContentScale.Fit
            )

            GoogleLoginButton(
                modifier = Modifier
                    .constrainAs(googleLoginBtn) {
                        top.linkTo(bannerImage.bottom)
                    },
            )

            LoginDivider(
                modifier = Modifier
                    .constrainAs(loginTypeDivider) {
                        top.linkTo(googleLoginBtn.bottom)
                    },
            )

            LoginForm(
                modifier = Modifier
                    .constrainAs(loginUi) {
                        top.linkTo(loginTypeDivider.bottom)
                        start.linkTo(parent.start)
                        end.linkTo(parent.end)
                        height = Dimension.preferredWrapContent
                    },
            )

            CopyrightText(
                modifier = Modifier
                    .constrainAs(copyrightUi) {
                        start.linkTo(parent.start)
                        end.linkTo(parent.end)
                        bottom.linkTo(parent.bottom, margin = SpacingToken.small)
                        width = Dimension.preferredWrapContent
                        height = Dimension.preferredWrapContent
                    }
            )
        }
    }
}

@Composable
fun GoogleLoginButton(modifier: Modifier) {
    AppOutlinedButton(
        modifier = modifier
            .fillMaxWidth()
            .appPaddingHorizontal(SpacingToken.large),
        text = stringResource(Res.string.action_login_with_google),
        onClick = {

        },
    )
}

@Composable
fun LoginDivider(modifier: Modifier) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .appPaddingOnly(
                top = SpacingToken.large,
                start = SpacingToken.large,
                end = SpacingToken.large
            ),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        AppDivider(
            modifier = Modifier.weight(1f),
            color = MaterialTheme.dividerColors.primary
        )

        AppText(
            text = stringResource(Res.string.label_or),
            fontWeight = FontWeight.Medium,
            textColor = MaterialTheme.textColors.primary,
            modifier = Modifier.appPaddingHorizontal(horizontal = SpacingToken.small)
        )

        AppDivider(
            modifier = Modifier.weight(1f),
            color = MaterialTheme.dividerColors.primary
        )
    }
}

@Composable
fun LoginForm(modifier: Modifier) {
    var email by rememberSaveable { mutableStateOf("") }
    var password by rememberSaveable { mutableStateOf("") }
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
            text = email,
            modifier = Modifier.fillMaxWidth(),
            title = stringResource(Res.string.label_username),
            placeholder = stringResource(Res.string.hint_user_name),
            onValueChange = {
                email = it
            },
            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next)
        )

        Spacer(Modifier.height(SpacingToken.medium))

        AppOutlineTextField(
            text = password,
            modifier = Modifier.fillMaxWidth(),
            title = stringResource(Res.string.label_password),
            placeholder = stringResource(Res.string.hint_password),
            onValueChange = {
                password = it
            },
            isPassword = true
        )

        Spacer(modifier = Modifier.height(SpacingToken.medium))

        AppElevatedButton(
            modifier = Modifier.fillMaxWidth(),
            text = stringResource(Res.string.action_login),
            onClick = {

            },
        )

        Spacer(modifier = Modifier.height(SpacingToken.medium))

        AppTextButton(
            text = stringResource(Res.string.action_forgot_password),
            modifier = Modifier.wrapContentWidth(Alignment.End),
            onClick = {}
        )

        AppText(
            text = stringResource(Res.string.action_no_account),
            fontWeight = FontWeight.Medium,
            textColor = MaterialTheme.textColors.secondary,
            modifier = Modifier
        )

        Spacer(modifier = Modifier.height(SpacingToken.medium))
    }
}

@Composable
fun CopyrightText(
    modifier: Modifier
) {
    AppText(
        modifier = modifier,
        text = stringResource(Res.string.msg_copyright),
        fontWeight = FontWeight.Light,
        textStyle = AppTypography.bodySmall,
        textColor = MaterialTheme.textColors.secondary,
    )
}

@Composable
@LightDarkPreview
fun LoginScreenPreview() {
    LoginScreen()
}