package com.friend.ui.common

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.LottieConstants
import com.airbnb.lottie.compose.rememberLottieComposition
import com.friend.designsystem.spacing.SpacingToken
import com.friend.designsystem.spacing.appPadding
import com.friend.designsystem.spacing.appPaddingHorizontal
import com.friend.designsystem.theme.backgroundColors
import com.friend.designsystem.theme.textColors
import com.friend.designsystem.typography.AppTypography
import com.friend.ui.components.AppOutlinedButton
import com.friend.ui.components.AppText
import com.friend.ui.preview.LightPreview
import com.friend.designsystem.R as Res

@Composable
fun ErrorUi(
    modifier: Modifier = Modifier,
    message: String,
    title: String = stringResource(Res.string.title_something_wrong),
    onRetry: () -> Unit,
) {
    val configuration = LocalConfiguration.current
    val screenHeightDp = configuration.screenHeightDp.dp

    // responsive lottie height (min 220dp, max 380dp, ~35% of screen height)
    val lottieHeight = (screenHeightDp * 0.35f).coerceIn(220.dp, 380.dp)

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.backgroundColors.primary)
            .appPadding(SpacingToken.medium)
            .verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {

        Spacer(modifier = Modifier.weight(.2f))

        val composition by rememberLottieComposition(
            LottieCompositionSpec.RawRes(Res.raw.no_data)
        )

        LottieAnimation(
            composition = composition,
            iterations = LottieConstants.IterateForever,
            modifier = Modifier
                .height(lottieHeight)
                .fillMaxWidth()
        )

        Spacer(modifier = Modifier.weight(.2f))

        AppText(
            text = title,
            textStyle = AppTypography.titleMedium,
            fontWeight = FontWeight.Bold,
            textColor = MaterialTheme.textColors.error,
            alignment = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(SpacingToken.medium))

        AppText(
            text = message,
            modifier = Modifier
                .fillMaxWidth()
                .appPaddingHorizontal(SpacingToken.medium),
            fontWeight = FontWeight.Light,
            textStyle = AppTypography.bodyMedium,
            textColor = MaterialTheme.textColors.secondary,
            maxLines = 3,
            alignment = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(SpacingToken.huge))

        AppOutlinedButton(
            text = "Retry",
            modifier = Modifier.fillMaxWidth(),
            onClick = onRetry
        )

        Spacer(modifier = Modifier.weight(1f))
    }
}


@Composable
@LightPreview
private fun ScreenPreview() {
    ErrorUi(
        title = "No internet connection",
        message = "Please check your internet connection and try again",
        onRetry = {}
    )
}