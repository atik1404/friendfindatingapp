package com.friend.home.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.friend.designsystem.spacing.SpacingToken
import com.friend.designsystem.spacing.appPadding
import com.friend.designsystem.spacing.appPaddingSymmetric
import com.friend.designsystem.theme.surfaceColors
import com.friend.designsystem.theme.textColors
import com.friend.designsystem.typography.AppTypography
import com.friend.ui.components.AppText
import com.friend.ui.components.NetworkImageLoader

@Composable
fun PersonItemCardSection(
    modifier: Modifier,
) {
    Box(
        contentAlignment = Alignment.BottomCenter,
        modifier = modifier.appPadding(SpacingToken.micro)
    ) {
        NetworkImageLoader(
            "https://images.mubicdn.net/images/cast_member/2184/cache-2992-1547409411/image-w856.jpg",
            modifier = modifier
                .fillMaxSize()
                .height(200.dp),
            shape = RoundedCornerShape(SpacingToken.extraSmall)
        )

        AppText(
            text = "Tom Cruise",
            textStyle = AppTypography.bodyMedium,
            fontWeight = FontWeight.Bold,
            textColor = MaterialTheme.textColors.white,
            modifier = modifier
                .appPaddingSymmetric(
                    horizontal = SpacingToken.medium,
                    vertical = SpacingToken.micro
                )
                .background(
                    MaterialTheme.surfaceColors.tertiary.copy(alpha = .1f),
                    shape = RoundedCornerShape(SpacingToken.medium)
                )
                .appPaddingSymmetric(
                    horizontal = SpacingToken.medium,
                    vertical = SpacingToken.micro
                ),
            alignment = TextAlign.Center,
            overflow = TextOverflow.Ellipsis
        )
    }
}