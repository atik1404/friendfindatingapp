package com.friend.chatroom.components.messageUi

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import com.friend.designsystem.typography.AppTypography
import com.friend.ui.components.AppText

@Composable
fun TextMessageContent(message: String, contentColor: Color) {
    AppText(
        text = message,
        textStyle = AppTypography.bodyMedium,
        textColor = contentColor,
        maxLines = 50
    )
}
