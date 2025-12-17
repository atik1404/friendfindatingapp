package com.friend.login

import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import com.friend.designsystem.spacing.SpacingToken
import com.friend.designsystem.spacing.appPaddingOnly
import com.friend.domain.base.TextInput
import com.friend.login.components.BannerAds
import com.friend.login.components.CopyrightText
import com.friend.login.components.GoogleLoginButton
import com.friend.login.components.LoginForm
import com.friend.login.components.LoginOptionDivider
import com.friend.ui.common.LoadingUi
import com.friend.ui.components.AppScaffold
import com.friend.ui.components.LocalImageLoader
import com.friend.ui.preview.LightPreview
import com.friend.designsystem.R as Res

/**
 * Login screen for FriendFin.
 *
 * This screen:
 * - Shows an illustration at the top
 * - Provides Google login
 * - Provides email/password login form
 * - Shows copyright text
 * - Renders a banner ad at the bottom
 *
 * Layout is built with [ConstraintLayout] and supports:
 * - Safe drawing insets
 * - Navigation bar padding
 * - IME (keyboard) padding
 * - Vertical scrolling for smaller screens
 *
 * @param state Current UI state for the login form.
 * @param onEvent Callback for handling user events (e.g., input change, login click).
 * @param navigateToRegistration Navigate to the registration/sign-up screen.
 * @param navigateToForgotPassword Navigate to the “Forgot password” screen.
 * @param onGoogleLoginClick Called when the Google login button is clicked.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LoginScreen(
    state: UiState,
    onEvent: (UiAction) -> Unit,
    navigateToRegistration: () -> Unit,
    navigateToForgotPassword: () -> Unit,
    onGoogleLoginClick: () -> Unit,
) {
    // App-level scaffold: handles top-level padding, status/navigation bars etc.
    AppScaffold(
        contentWindowInsets = WindowInsets.safeDrawing,
    ) { padding ->
        ConstraintLayout(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)                // Apply scaffold-provided padding
                .consumeWindowInsets(padding)    // Consume to avoid double-insets downstream
                .navigationBarsPadding()         // Keep content above navigation bar
                .imePadding()                    // Push content above keyboard
                .verticalScroll(rememberScrollState()) // Allow scroll on small screens
        ) {
            // Guideline to control the relative height of the banner image area
            val guideline = createGuidelineFromTop(0.25f)

            // Constraint references for each child composable
            val (bannerImage, googleLoginBtn, loginTypeDivider, loginUi, copyrightUi, bannerAds) =
                createRefs()

            // Top illustration image
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

            // Google sign-in button
            GoogleLoginButton(
                modifier = Modifier
                    .appPaddingOnly(top = SpacingToken.medium)
                    .constrainAs(googleLoginBtn) {
                        top.linkTo(bannerImage.bottom)
                        start.linkTo(parent.start)
                        end.linkTo(parent.end)
                        width = Dimension.fillToConstraints
                    },
                onClick = onGoogleLoginClick
            )

            // Divider between Google login and email/password login options
            LoginOptionDivider(
                modifier = Modifier.constrainAs(loginTypeDivider) {
                    top.linkTo(googleLoginBtn.bottom)
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                    width = Dimension.fillToConstraints
                },
            )

            // Email/password login form section
            LoginForm(
                modifier = Modifier.constrainAs(loginUi) {
                    top.linkTo(loginTypeDivider.bottom)
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                    width = Dimension.fillToConstraints
                },
                username = state.username,
                password = state.password,
                isFormSubmitting = state.isSubmitting,
                onForgotPasswordClick = navigateToForgotPassword,
                onSignUpClick = navigateToRegistration,
                onUsernameChange = { onEvent(UiAction.UsernameChanged(it)) },
                onPasswordChange = { onEvent(UiAction.PasswordChanged(it)) },
                onFormSubmit = { onEvent(UiAction.PerformLogin) }
            )

            // Copyright text shown above banner ads
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

            // Bottom banner ads area
            BannerAds(
                modifier = Modifier.constrainAs(bannerAds) {
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                    bottom.linkTo(parent.bottom)
                    width = Dimension.fillToConstraints
                }
            )
        }

        if (state.isLoading)
            LoadingUi()
    }
}

/**
 * Design-time preview for [LoginScreen].
 *
 * Uses a fake [UiState] to visualize the layout in Android Studio.
 */
@Composable
@LightPreview
fun ScreenPreview() {
    val fakeState = UiState(
        username = TextInput(),
        password = TextInput(),
        isLoading = false,
    )

    LoginScreen(
        state = fakeState,
        onEvent = {},
        navigateToRegistration = {},
        navigateToForgotPassword = {},
        onGoogleLoginClick = {},
    )
}
