package com.friend.chatroom.components

import AppDivider
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import com.friend.chatroom.components.messageUi.MessageContent
import com.friend.chatroom.components.messageUi.bubbleColorPair
import com.friend.chatroom.components.messageUi.bubbleCornerShape
import com.friend.chatroom.utils.AudioPlayerController
import com.friend.chatroom.utils.rememberAudioPlayerController
import com.friend.common.dateparser.DateTimeParser
import com.friend.common.dateparser.DateTimePatterns
import com.friend.designsystem.spacing.SpacingToken
import com.friend.designsystem.spacing.appPadding
import com.friend.designsystem.spacing.appPaddingHorizontal
import com.friend.designsystem.spacing.appPaddingOnly
import com.friend.designsystem.theme.dividerColors
import com.friend.designsystem.theme.textColors
import com.friend.designsystem.typography.AppTypography
import com.friend.entity.chatmessage.MessageEntity
import com.friend.ui.components.AppText
import com.friend.ui.preview.LightPreview

@Composable
fun MessagesUiSection(
    message: List<MessageEntity>,
    modifier: Modifier
) {
    //val audioController = rememberAudioPlayerController()

    LazyColumn(
        reverseLayout = true,
        modifier = modifier
            .appPaddingHorizontal(SpacingToken.small)
    ) {
        items(
            items = message,
            key = { it.messageId }
        ) { message ->
            MessageBubble(
                modifier = Modifier,
                message = message,
                //audioController = audioController
            )
        }
    }
}

@Composable
fun MessageBubble(
    modifier: Modifier = Modifier,
    message: MessageEntity,
    //audioController: AudioPlayerController
) {
    val backgroundColor = bubbleColorPair(message.isMyMessage).first
    val contentColor = bubbleColorPair(message.isMyMessage).second
    val bubbleShape = bubbleCornerShape(message.isMyMessage)
    val alignment = if (message.isMyMessage) Alignment.End else Alignment.Start

    Column(
        modifier = modifier
            .fillMaxWidth()
            .appPaddingOnly(top = SpacingToken.medium),
        horizontalAlignment = alignment
    ) {
        if (message.readableDateTime.isNotEmpty()) {
            DateDivider(message.readableDateTime)
        }

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
            MessageContent(
                message = message,
                contentColor = contentColor,
                alignment = alignment,
                //audioController = audioController
            )

            Spacer(
                modifier = Modifier.height(SpacingToken.micro)
            )

            AppText(
                text = DateTimeParser.parseToPattern(
                    message.dateTime,
                    outputPattern = DateTimePatterns.TIME_12_HM_AMPM,
                ),
                textStyle = AppTypography.bodySmall,
                fontWeight = FontWeight.Light,
                textColor = contentColor,
            )
        }
    }
}

@Composable
private fun DateDivider(date: String) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        AppDivider(
            modifier = Modifier.weight(1f),
            color = MaterialTheme.dividerColors.primary
        )
        AppText(
            text = date,
            textStyle = AppTypography.bodySmall,
            fontWeight = FontWeight.Medium,
            textColor = MaterialTheme.textColors.primary,
            modifier = Modifier
                .appPaddingHorizontal(SpacingToken.medium)
        )
        AppDivider(
            modifier = Modifier.weight(1f),
            color = MaterialTheme.dividerColors.primary
        )
    }
}

@Composable
@LightPreview
private fun ScreenPreview() {
    MessageBubble(
        //audioController =  rememberAudioPlayerController(),
        message = MessageEntity(
            messageId = "",
            isMyMessage = true,
            fromUsername = "John Doe",
            body = "",
            imageUrl = "",
            audioUrl = "fds",
            audioDuration = 10,
            videoUrl = "",
            videoDuration = 0,
            dateTime = "04:99 PM",
            readableDateTime = ""
        )
    )
}


@Composable
@LightPreview
private fun DatePreview() {
    DateDivider(
        date = "Today"
    )
}