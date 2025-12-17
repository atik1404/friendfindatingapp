package com.friend.splash

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import com.friend.designsystem.spacing.SpacingToken
import com.friend.designsystem.theme.textColors
import com.friend.designsystem.typography.AppTypography
import com.friend.ui.common.ErrorUi
import com.friend.ui.common.LoadingAnimation
import com.friend.ui.components.AppScaffold
import com.friend.ui.components.AppText
import com.friend.ui.components.LocalImageLoader
import com.friend.ui.preview.LightPreview
import com.friend.designsystem.R as Res

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SplashScreen(
    uiState: UiState,
    onEvent: (UiEvent) -> Unit
) {
    AppScaffold(
        contentWindowInsets = WindowInsets(0)
    ) { padding ->
        when (uiState) {
            is UiState.Error -> ErrorUi(message = uiState.message) {
                onEvent.invoke(UiEvent.FetchProfile)
            }

            UiState.Idle -> {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(padding)
                ) {
                    LocalImageLoader(
                        imageResId = Res.drawable.img_splash_background,
                        modifier = Modifier
                            .fillMaxSize()
                    )

                    Column(
                        modifier = Modifier
                            .padding(padding)
                            .align(Alignment.Center),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        AnimatedVisibilityImage(true)

                        Spacer(modifier = Modifier.height(SpacingToken.medium))

                        AppText(
                            stringResource(Res.string.msg_dating_app),
                            fontWeight = FontWeight.Bold,
                            textStyle = AppTypography.titleLarge,
                            alignment = TextAlign.Center,
                            textColor = MaterialTheme.textColors.white
                        )

                        Spacer(modifier = Modifier.height(SpacingToken.medium))

                        LoadingAnimation()
                    }
                }
            }
        }
    }
}

@Composable
fun AnimatedVisibilityImage(visible: Boolean) {
    AnimatedVisibility(
        visible = visible,
        enter = fadeIn() + scaleIn(initialScale = 0.95f),
        exit = fadeOut() + scaleOut(targetScale = 0.95f)
    ) {
        LocalImageLoader(
            imageResId = Res.drawable.friendfin,
        )
    }
}

@Composable
@LightPreview
fun ScreenPreview() {
    SplashScreen(uiState = UiState.Idle) {}
}