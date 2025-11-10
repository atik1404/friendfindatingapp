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
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import com.friend.designsystem.spacing.SpacingToken
import com.friend.designsystem.theme.textColors
import com.friend.designsystem.typography.AppTypography
import com.friend.ui.components.AppText
import com.friend.ui.components.LocalImageLoader
import com.friend.ui.preview.LightPreview
import kotlinx.coroutines.delay
import com.friend.designsystem.R as Res

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SplashScreen(
    navigateToLoginScreen: () -> Unit = {},
    navigateToHomeScreen: () -> Unit = {},
) {
    // navigate to login after 2 seconds
    LaunchedEffect(Unit) {
        delay(2000L)
        navigateToLoginScreen()
    }
    Scaffold(
        contentWindowInsets = WindowInsets(0)
    ) { padding ->
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
            }
        }
    }
}

@Composable
fun AnimatedVisibilityImage(visible: Boolean) {
    AnimatedVisibility(
        visible = visible,
        enter = fadeIn() + scaleIn(initialScale = 0.95f),
        exit  = fadeOut() + scaleOut(targetScale = 0.95f)
    ) {
        LocalImageLoader(
            imageResId = Res.drawable.friendfin,
        )
    }
}

@Composable
@LightPreview
fun SplashScreenPreview() {
    SplashScreen()
}