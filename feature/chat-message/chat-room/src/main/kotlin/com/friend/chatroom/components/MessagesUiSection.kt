package com.friend.chatroom.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.text.font.FontWeight
import com.friend.common.dateparser.DateTimePatterns
import com.friend.common.dateparser.DateTimeUtils
import com.friend.designsystem.spacing.RadiusToken
import com.friend.designsystem.spacing.SpacingToken
import com.friend.designsystem.spacing.appPadding
import com.friend.designsystem.spacing.appPaddingHorizontal
import com.friend.designsystem.spacing.appPaddingOnly
import com.friend.designsystem.theme.surfaceColors
import com.friend.designsystem.theme.textColors
import com.friend.designsystem.typography.AppTypography
import com.friend.entity.chatmessage.MessageEntity
import com.friend.ui.components.AppText

@Composable
fun MessagesUiSection(
    message: List<MessageEntity>,
    modifier: Modifier
) {
    LazyColumn(
        reverseLayout = true,
        modifier = modifier
            .appPaddingHorizontal(SpacingToken.small)
    ) {
        items(
            items = message,
            key = { it.messageId }
        ) { message ->
            MessageItem(
                modifier = Modifier,
                message = message
            )
        }
    }
}

@Composable
fun MessageItem(modifier: Modifier, message: MessageEntity) {
    val backgroundColor = bubbleColors(message.isMyMessage).first
    val contentColor = bubbleColors(message.isMyMessage).second
    val bubbleShape = bubbleShape(message.isMyMessage)
    val alignment = if (message.isMyMessage) Alignment.End else Alignment.Start

    Column(
        modifier = modifier
            .fillMaxWidth()
            .appPaddingOnly(top = SpacingToken.medium),
        horizontalAlignment = alignment
    ) {
        AppText(
            text = DateTimeUtils.parseToPattern(message.dateTime, DateTimePatterns.MDY_TEXT_COMMA),
            textStyle = AppTypography.bodySmall,
            fontWeight = FontWeight.Medium,
            textColor = MaterialTheme.textColors.primary,
            modifier = Modifier.align(Alignment.CenterHorizontally)
        )

        Spacer(
            modifier = Modifier.height(SpacingToken.medium)
        )

        AppText(
            text = message.fromUsername,
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
                text = message.body,
                textStyle = AppTypography.bodyMedium,
                textColor = contentColor,
                maxLines = 50
            )

            Spacer(
                modifier = Modifier.height(SpacingToken.micro)
            )

            AppText(
                text = DateTimeUtils.parseToPattern(
                    message.dateTime,
                    DateTimePatterns.TIME_12_HM_AMPM
                ),
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