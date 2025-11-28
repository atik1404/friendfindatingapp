package com.friend.login

import AppDivider
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
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
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
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
import com.friend.ui.components.ColoredTextSegment
import com.friend.ui.components.LocalImageLoader
import com.friend.ui.components.MultiColorText
import com.friend.ui.preview.LightPreview
import com.friend.ui.showToastMessage
import kotlinx.coroutines.delay
import timber.log.Timber
import com.friend.designsystem.R as Res

/**
 * Top-level login screen scaffold.
 *
 * - Uses WindowInsets.safeDrawing to respect system bars.
 * - Adds navigation/IME padding so content isn't hidden by the keyboard/system nav.
 * - ConstraintLayout orchestrates sections: banner, social btn, divider, form, footer, ads.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LoginScreen(
    navigateToRegistration: () -> Unit,
    navigateToForgotPassword: () -> Unit,
    navigateToHome: () -> Unit,
) {
    val currentContext = LocalContext.current
    AppScaffold(
        contentWindowInsets = WindowInsets.safeDrawing,
    ) { padding ->
        ConstraintLayout(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)                // from Scaffold
                .consumeWindowInsets(padding)    // prevent double-inset consumption downstream
                .navigationBarsPadding()         // keep content above system nav bar
                .imePadding()                    // lift content when keyboard shows
                .verticalScroll(rememberScrollState()) // simple, contents are small; LazyColumn not necessary
        ) {
            // Guideline for top banner relative height
            val guideline = createGuidelineFromTop(0.25f)

            // References for layout children
            val (bannerImage, googleLoginBtn, loginTypeDivider, loginUi, copyrightUi, bannerAds) =
                createRefs()

            LocalImageLoader(
                imageResId = Res.drawable.img_login_illustration,
                modifier = Modifier.constrainAs(bannerImage) {
                    top.linkTo(parent.top)
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
                    .appPaddingOnly(top = SpacingToken.medium)
                    .constrainAs(googleLoginBtn) {
                        top.linkTo(bannerImage.bottom)
                        start.linkTo(parent.start)
                        end.linkTo(parent.end)
                        width = Dimension.fillToConstraints
                    },
                onClick = {
                    currentContext.showToastMessage(
                        "This is toast message",
                    )
                }
            )

            // Visual divider with "OR"
            LoginDivider(
                modifier = Modifier
                    .constrainAs(loginTypeDivider) {
                        top.linkTo(googleLoginBtn.bottom)
                        start.linkTo(parent.start)
                        end.linkTo(parent.end)
                        width = Dimension.fillToConstraints
                    },
            )

            // Username/password form
            LoginForm(
                modifier = Modifier
                    .constrainAs(loginUi) {
                        top.linkTo(loginTypeDivider.bottom)
                        start.linkTo(parent.start)
                        end.linkTo(parent.end)
                        width = Dimension.fillToConstraints
                    },
                onForgotPasswordClick = navigateToForgotPassword,
                onSignUpClick = navigateToRegistration
            )

            // Footer: copyright
            CopyrightText(
                modifier = Modifier.constrainAs(copyrightUi) {
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                    top.linkTo(loginUi.bottom)
                    bottom.linkTo(bannerAds.top, margin = SpacingToken.tiny)
                    width = Dimension.preferredWrapContent
                    height = Dimension.preferredWrapContent
                }
            )

            // Ad placeholder
            BannerAds(
                modifier = Modifier.constrainAs(bannerAds) {
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                    bottom.linkTo(parent.bottom)
                    width = Dimension.fillToConstraints
                }
            )
        }
    }
}

/**
 * Google sign-in outlined button.
 * Default [modifier] makes it easy to reuse elsewhere.
 */
@Composable
fun GoogleLoginButton(
    modifier: Modifier = Modifier,
    onClick: () -> Unit,
) {

    var isLoading by rememberSaveable { mutableStateOf(false) }
    AppOutlinedButton(
        modifier = modifier
            .fillMaxWidth()
            .appPaddingHorizontal(SpacingToken.large),
        text = stringResource(Res.string.action_login_with_google),
        onClick = onClick,
        isLoading = isLoading
    )
}

/**
 * Centered "OR" with full-bleed dividers on both sides.
 * Uses weight on dividers so the label is always centered.
 */
@Composable
fun LoginDivider(
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .appPaddingOnly(
                top = SpacingToken.medium,
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

/**
 * Email/Password form with minimal validation and good keyboard UX.
 * - Moves focus Next from Email -> Password.
 * - IME Done on Password triggers submit.
 * - Submit button enabled only if fields are non-empty (add your own email format checks).
 */
@Composable
fun LoginForm(
    modifier: Modifier = Modifier,
    onForgotPasswordClick: () -> Unit,
    onSignUpClick: () -> Unit,
    viewModel: LoginViewModel = hiltViewModel(),
) {
    val state by viewModel.uiState.collectAsState()
    val currentContext = LocalContext.current

    // Collect one-time effects
    LaunchedEffect(key1 = true) {
        viewModel.uiEffect.collect { effect ->
            when (effect) {
                is LoginUiEffect.ShowMessage -> {
                    currentContext.showToastMessage(effect.message)
                }

                is LoginUiEffect.NavigateToHome -> {
                    Timber.e("Navigate to home")
                }
            }
        }
    }

    Column(
        modifier = modifier
            .appPadding(SpacingToken.medium) // outer breathing room
            .border(
                width = StrokeTokens.thin,
                color = MaterialTheme.strokeColors.primary,
                shape = RoundedCornerShape(RadiusToken.large)
            )
            .appPadding(SpacingToken.medium), // inner padding inside the card
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        AppOutlineTextField(
            text = state.email,
            modifier = Modifier.fillMaxWidth(),
            title = stringResource(Res.string.label_username),
            placeholder = stringResource(Res.string.hint_user_name),
            onValueChange = {
                viewModel.action(LoginUiEvent.EmailChanged(it))
            },
            keyboardOptions = KeyboardOptions(
                imeAction = ImeAction.Next,
            ),
        )

        Spacer(Modifier.height(SpacingToken.medium))

        AppOutlineTextField(
            text = state.password,
            modifier = Modifier.fillMaxWidth(),
            title = stringResource(Res.string.label_password),
            placeholder = stringResource(Res.string.hint_password),
            onValueChange = {
                viewModel.action(LoginUiEvent.PasswordChanged(it))
            },
            isPassword = true,
            keyboardOptions = KeyboardOptions(
                imeAction = ImeAction.Done,
            ),
        )

        Spacer(modifier = Modifier.height(SpacingToken.medium))

        AppElevatedButton(
            modifier = Modifier.fillMaxWidth(),
            text = stringResource(Res.string.action_login),
            isLoading = state.isLoading,
            onClick = {
                viewModel.action(LoginUiEvent.Submit)
            },
        )

        // “Forgot password” aligned to end
        AppTextButton(
            text = stringResource(Res.string.action_forgot_password),
            modifier = Modifier.wrapContentWidth(Alignment.End),
            onClick = {
                onForgotPasswordClick.invoke()
            }
        )

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
                onClick = {
                    onSignUpClick.invoke()
                }
            ),
        )

        MultiColorText(segments = segments)
    }
}

/**
 * Footer copyright text.
 */
@Composable
fun CopyrightText(
    modifier: Modifier = Modifier
) {
    AppText(
        modifier = modifier,
        text = stringResource(Res.string.msg_copyright),
        fontWeight = FontWeight.Light,
        textStyle = AppTypography.bodySmall,
        textColor = MaterialTheme.textColors.secondary,
    )
}

/**
 * Simple banner-ads placeholder block.
 * Replace with your actual AdView composable.
 */
@Composable
fun BannerAds(modifier: Modifier = Modifier) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .height(50.dp)
            .background(Color.Gray)
            .border(1.dp, Color.Black),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        AppText(
            text = "Banner ads",
            alignment = TextAlign.Center
        )
    }
}

@Composable
@LightPreview
fun LoginScreenPreview() {
    LoginScreen(
        navigateToRegistration = { },
        navigateToForgotPassword = { },
        navigateToHome = {}
    )
}
