package com.friend.chatroom.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.text.font.FontWeight
import com.friend.designsystem.spacing.RadiusToken
import com.friend.designsystem.spacing.SpacingToken
import com.friend.designsystem.spacing.appPadding
import com.friend.designsystem.spacing.appPaddingOnly
import com.friend.designsystem.theme.surfaceColors
import com.friend.designsystem.theme.textColors
import com.friend.designsystem.typography.AppTypography
import com.friend.ui.components.AppText

@Composable
fun MessageItem(modifier: Modifier, isMyMessage: Boolean) {
    val backgroundColor = bubbleColors(isMyMessage).first
    val contentColor = bubbleColors(isMyMessage).second
    val bubbleShape = bubbleShape(isMyMessage)
    val alignment = if (isMyMessage) Alignment.End else Alignment.Start

    Column(
        modifier = modifier
            .fillMaxWidth()
            .appPaddingOnly(top = SpacingToken.medium),
        horizontalAlignment = alignment
    ) {
        AppText(
            text = "September 23, 2025",
            textStyle = AppTypography.bodySmall,
            fontWeight = FontWeight.Medium,
            textColor = MaterialTheme.textColors.primary,
            modifier = Modifier.align(Alignment.CenterHorizontally)
        )

        Spacer(
            modifier = Modifier.height(SpacingToken.medium)
        )

        AppText(
            text = "Atik Faysal",
            textStyle = AppTypography.bodySmall,
            fontWeight = FontWeight.Light,
            textColor = MaterialTheme.textColors.primary,
        )

        Spacer(
            modifier = Modifier.height(SpacingToken.micro)
        )

        Column(
            modifier = Modifier
                .background(
                    color = backgroundColor,
                    shape = bubbleShape
                )
                .appPadding(SpacingToken.tiny),
            horizontalAlignment = alignment
        ) {
            AppText(
                text = "Hey, How are you Atik?Hey, How are you Atik?",
                textStyle = AppTypography.bodyMedium,
                textColor = contentColor,
                maxLines = 50
            )

            Spacer(
                modifier = Modifier.height(SpacingToken.micro)
            )

            AppText(
                text = "04:18 AM",
                textStyle = AppTypography.bodySmall,
                fontWeight = FontWeight.Light,
                textColor = contentColor,
            )
        }
    }
}

/** --- Bubble shape & color helpers --- */
@Composable
private fun bubbleShape(isMe: Boolean): Shape {
    return if (isMe) {
        RoundedCornerShape(
            topEnd = RadiusToken.medium,
            topStart = RadiusToken.xxl,
            bottomEnd = RadiusToken.medium,
        )
    } else {
        RoundedCornerShape(
            topEnd = RadiusToken.xxl,
            topStart = RadiusToken.medium,
            bottomStart = RadiusToken.medium,
        )
    }
}

@Composable
private fun bubbleColors(isMe: Boolean): Pair<Color, Color> {
    return if (isMe) {
        // sender (me)
        MaterialTheme.surfaceColors.primary.copy(alpha = .7f) to MaterialTheme.textColors.white
    } else {
        // receiver
        MaterialTheme.surfaceColors.yellowLight.copy(alpha = .5f) to MaterialTheme.textColors.primary
    }
}