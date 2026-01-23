package com.friend.chatroom.components.conversation

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.friend.designsystem.spacing.SpacingToken
import com.friend.entity.chatmessage.ConversationEntity

@Composable
fun ConversationBody(
    message: ConversationEntity,
    contentColor: Color,
    alignment: Alignment.Horizontal,
    // audioController: AudioPlayerController
) {
    Column(
        horizontalAlignment = alignment
    ) {
        if (message.body.isNotEmpty() || message.imageUrl.isNotEmpty()) {
            if (message.imageUrl.isNotEmpty()) {
                ImageMessageContent(
                    url = message.imageUrl,
                    modifier = Modifier
                        .width(200.dp)
                        .height(180.dp)
                )
            }

            if (message.body.isNotEmpty() && message.imageUrl.isNotEmpty()) {
                Spacer(modifier = Modifier.height(SpacingToken.medium))
            }

            if (message.body.isNotEmpty()) {
                TextMessageContent(
                    message = message.body,
                    contentColor = contentColor,
                )
            }
            return
        }

        if (message.audioUrl.isNotEmpty()) {
            AudioMessageContent(
                id = message.messageId,
                url = message.audioUrl,
                duration = message.audioDuration.toLong(),
                //audioController =  rememberAudioPlayerController()
            )
            return
        }

        if (message.videoUrl.isNotEmpty()) {
            VideoMessageContent(
                url = message.videoUrl,
                modifier = Modifier
                    .width(220.dp)
                    .height(180.dp)
            )
        }
    }
}