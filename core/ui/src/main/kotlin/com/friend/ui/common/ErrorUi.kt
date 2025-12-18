package com.friend.ui.common

import android.annotation.SuppressLint
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
import com.airbnb.lottie.compose.LottieCompositionSpec.RawRes
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

enum class ErrorType {
    EMPTY_DATA,
    API_ERROR,
    NETWORK_ERROR,
}

@SuppressLint("ConfigurationScreenWidthHeight")
@Composable
fun ErrorUi(
    modifier: Modifier = Modifier,
    errorType: ErrorType = ErrorType.API_ERROR,
    message: String,
    onRetry: () -> Unit,
) {
    val configuration = LocalConfiguration.current
    val screenHeightDp = configuration.screenHeightDp.dp

    // responsive lottie height (min 220dp, max 380dp, ~35% of screen height)
    val lottieHeight = (screenHeightDp * 0.35f).coerceIn(220.dp, 380.dp)

    val isNetworkError = message.contains("internet")

    val networkErrorMessage = stringResource(Res.string.error_network_error)

    val errorAnimation =
        RawRes(getErrorAnimation(if (isNetworkError) ErrorType.NETWORK_ERROR else errorType))
    val title =
        stringResource(getErrorTitle(if (isNetworkError) ErrorType.NETWORK_ERROR else errorType))

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.backgroundColors.primary)
            .appPadding(SpacingToken.medium)
            .verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Spacer(modifier = Modifier.weight(.2f))

        val composition by rememberLottieComposition(errorAnimation)

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
            text = if (isNetworkError) networkErrorMessage else message,
            modifier = Modifier
                .appPaddingHorizontal(SpacingToken.medium),
            fontWeight = FontWeight.Light,
            textStyle = AppTypography.bodyMedium,
            textColor = MaterialTheme.textColors.secondary,
            maxLines = 3,
            alignment = TextAlign.Center,
        )

        Spacer(modifier = Modifier.height(SpacingToken.huge))

        AppOutlinedButton(
            text = stringResource(Res.string.action_retry),
            modifier = Modifier.fillMaxWidth(),
            onClick = onRetry
        )

        Spacer(modifier = Modifier.weight(1f))
    }
}

private fun getErrorAnimation(errorType: ErrorType): Int {
    return when (errorType) {
        ErrorType.EMPTY_DATA -> Res.raw.anim_data_empty
        ErrorType.API_ERROR -> Res.raw.anim_api_error
        ErrorType.NETWORK_ERROR -> Res.raw.anim_network_error
    }
}

private fun getErrorTitle(errorType: ErrorType): Int {
    return when (errorType) {
        ErrorType.EMPTY_DATA -> Res.string.title_data_empty
        ErrorType.API_ERROR -> Res.string.title_something_wrong
        ErrorType.NETWORK_ERROR -> Res.string.title_network_error
    }
}


@Composable
@LightPreview
private fun ScreenPreview() {
    ErrorUi(
        message = "Please check your internet connection and try again",
        onRetry = {}
    )
}