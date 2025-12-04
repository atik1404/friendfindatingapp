package com.friend.ui.common

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.friend.designsystem.spacing.RadiusToken
import com.friend.designsystem.spacing.SpacingToken
import com.friend.designsystem.spacing.appPadding
import com.friend.designsystem.spacing.appPaddingHorizontal
import com.friend.designsystem.theme.backgroundColors
import com.friend.designsystem.theme.textColors
import com.friend.designsystem.typography.AppTypography
import com.friend.ui.components.AppOutlinedButton
import com.friend.ui.components.AppText
import com.friend.ui.components.LocalImageLoader
import com.friend.ui.preview.LightPreview
import com.friend.designsystem.R as Res

@Composable
fun ErrorUi(
    modifier: Modifier = Modifier,
    message: String,
    title: String = stringResource(Res.string.title_something_wrong),
    onRetry: () -> Unit,
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.backgroundColors.primary)
            .appPadding(SpacingToken.medium),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        // Image / Icon
        LocalImageLoader(
            imageResId = Res.drawable.banner_error_image_new,
            contentScale = ContentScale.FillWidth,
            modifier = Modifier
                .height(300.dp)
                .fillMaxWidth()
                .background(MaterialTheme.backgroundColors.primary)
        )

        Spacer(modifier = Modifier.height(SpacingToken.huge))

        // Title
        AppText(
            text = title,
            textStyle = AppTypography.bodyLarge,
            fontWeight = FontWeight.Bold,
            textColor = MaterialTheme.textColors.error
        )

        Spacer(modifier = Modifier.height(SpacingToken.medium))
        AppText(
            text = message,
            modifier = Modifier.appPaddingHorizontal(SpacingToken.medium),
            fontWeight = FontWeight.Light,
            textStyle = AppTypography.bodySmall,
            textColor = MaterialTheme.textColors.secondary
        )

        Spacer(modifier = Modifier.height(SpacingToken.huge))

        // Retry button
        AppOutlinedButton(
            text = "Retry",
            modifier = modifier.fillMaxWidth(),
            onClick = {
                onRetry.invoke()
            },
        )
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