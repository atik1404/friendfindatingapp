package com.friend.overview.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.Verified
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import com.friend.designsystem.spacing.IconSizeToken
import com.friend.designsystem.spacing.ImageSizeToken
import com.friend.designsystem.spacing.SpacingToken
import com.friend.designsystem.spacing.StrokeTokens
import com.friend.designsystem.spacing.appPadding
import com.friend.designsystem.spacing.appPaddingSymmetric
import com.friend.designsystem.theme.backgroundColors
import com.friend.designsystem.theme.strokeColors
import com.friend.designsystem.theme.surfaceColors
import com.friend.designsystem.theme.textColors
import com.friend.designsystem.typography.AppTypography
import com.friend.ui.components.AppIconButton
import com.friend.ui.components.AppText
import com.friend.ui.components.NetworkImageLoader

@Composable
fun ProfileSummaryUi(
    modifier: Modifier,
    onClick: () -> Unit
){
    Card(
        modifier = modifier,
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.backgroundColors.white,  // background
            contentColor = MaterialTheme.colorScheme.onPrimaryContainer    // text/icon color
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .appPaddingSymmetric(
                    horizontal = SpacingToken.medium,
                    vertical = SpacingToken.large
                ),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            val picSize = ImageSizeToken.profilePictureSmall
            Box(
                modifier = modifier.size(picSize)
            ) {
                NetworkImageLoader(
                    url = "https://images.mubicdn.net/images/cast_member/2184/cache-2992-1547409411/image-w856.jpg",
                    shape = CircleShape,
                    modifier = Modifier.matchParentSize()
                )

                // Overlay edit button at bottom-right (RTL-aware via BottomEnd)
                AppIconButton(
                    vectorIcon = Icons.Default.Verified,
                    tint = MaterialTheme.surfaceColors.grayLight,
                    onClick = {},
                    modifier = Modifier
                        .align(Alignment.BottomEnd)
                        .size(IconSizeToken.small) // tweak as you like
                        .clip(CircleShape)
                        .background(MaterialTheme.surfaceColors.greenBase)
                        .border(
                            width = StrokeTokens.hairline,
                            color = MaterialTheme.strokeColors.primary,
                            shape = CircleShape
                        )
                        .appPadding(SpacingToken.minimum) // inner padding for icon touch target
                )
            }

            Spacer(
                modifier = Modifier.width(SpacingToken.medium)
            )

            Column {
                AppText(
                    text = "Tom Cruise",
                    textStyle = AppTypography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    textColor = MaterialTheme.textColors.primary,
                )

                Spacer(
                    modifier = Modifier.height(SpacingToken.micro)
                )

                AppText(
                    text = "tom@gmail.com",
                    textStyle = AppTypography.bodyMedium,
                    fontWeight = FontWeight.Light,
                    textColor = MaterialTheme.textColors.primary,
                )
            }

            Spacer(
                modifier = Modifier.weight(1f)
            )

            AppIconButton(
                onClick = onClick,
                vectorIcon = Icons.Default.KeyboardArrowRight
            )
        }
    }
}