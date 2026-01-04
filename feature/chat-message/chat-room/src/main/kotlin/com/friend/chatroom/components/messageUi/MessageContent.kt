package com.friend.chatroom.components.messageUi

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import com.friend.chatroom.utils.AudioPlayerController
import com.friend.designsystem.spacing.SpacingToken
import com.friend.entity.chatmessage.MessageEntity

@Composable
fun MessageContent(
    message: MessageEntity,
    contentColor: Color,
    alignment: Alignment.Horizontal,
    audioController: AudioPlayerController
) {
    Column(
        horizontalAlignment = alignment
    ) {
        if (message.body.isNotEmpty()) {
            if (message.imageUrl.isNotEmpty()) {
                ImageMessageContent(
                    url = message.imageUrl
                )
            }

            Spacer(modifier = Modifier.height(SpacingToken.medium))

            TextMessageContent(
                message = message.body,
                contentColor = contentColor,
            )
            return
        }

        if (message.audioUrl.isNotEmpty()) {
            AudioMessageContent(
                id = message.messageId,
                url = message.audioUrl,
                duration = message.audioDuration.toLong(),
                audioController = audioController
            )
            return
        }
    }
}