package com.friend.chatroom.components.conversation

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import com.friend.designsystem.spacing.RadiusToken
import com.friend.designsystem.theme.surfaceColors
import com.friend.designsystem.theme.textColors

/** --- Bubble shape & color helpers --- */
@Composable
fun bubbleCornerShape(isMe: Boolean): Shape {
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
fun bubbleColorPair(isMe: Boolean): Pair<Color, Color> {
    return if (isMe) {
        // sender (me)
        MaterialTheme.surfaceColors.primary.copy(alpha = .7f) to MaterialTheme.textColors.white
    } else {
        // receiver
        MaterialTheme.surfaceColors.yellowLight.copy(alpha = .5f) to MaterialTheme.textColors.primary
    }
}
