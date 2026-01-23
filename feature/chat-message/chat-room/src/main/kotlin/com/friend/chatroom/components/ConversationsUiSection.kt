package com.friend.chatroom.components

import AppDivider
import androidx.compose.foundation.background
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import com.friend.chatroom.bottomsheet.ImagePreviewDialog
import com.friend.chatroom.components.conversation.ConversationBody
import com.friend.chatroom.components.conversation.bubbleColorPair
import com.friend.chatroom.components.conversation.bubbleCornerShape
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
import com.friend.entity.chatmessage.ConversationEntity
import com.friend.ui.components.AppCheckbox
import com.friend.ui.components.AppText
import com.friend.ui.preview.LightPreview

@Composable
fun ConversionsUiSection(
    modifier: Modifier,
    listState: LazyListState,
    message: List<ConversationEntity>,
    isItemSelectionEnable: Boolean,
    onLongPress: () -> Unit,
    onItemSelected: (ConversationEntity) -> Unit,
    onNavigateToPlayerScreen: (String) -> Unit,
) {
    val audioController = rememberAudioPlayerController()
    var showDialog by remember { mutableStateOf(false) }
    var image by remember { mutableStateOf("") }

    LazyColumn(
        state = listState,
        reverseLayout = true,
        modifier = modifier
            .appPaddingHorizontal(SpacingToken.small),
        contentPadding = PaddingValues(bottom = SpacingToken.medium)
    ) {
        items(
            items = message,
            key = { it.messageId }
        ) { message ->
            ConversationItemBubble(
                modifier = Modifier
                    .combinedClickable(
                        onClick = {
                            if (isItemSelectionEnable)
                                onItemSelected.invoke(message)

                            if (message.videoUrl.isNotEmpty() && !isItemSelectionEnable)
                                onNavigateToPlayerScreen.invoke(message.videoUrl)

                            if (message.imageUrl.isNotEmpty() && !isItemSelectionEnable) {
                                image = message.imageUrl
                                showDialog = true
                            }
                        },
                        onLongClick = onLongPress,
                    ),
                message = message,
                isItemSelectionEnable = isItemSelectionEnable,
                onItemSelected = {
                    onItemSelected.invoke(message)
                },
                onClick = {
                    if (message.videoUrl.isNotEmpty() && !isItemSelectionEnable)
                        onNavigateToPlayerScreen.invoke(message.videoUrl)
                },
                audioController = audioController
            )
        }
    }

    if (showDialog) {
        ImagePreviewDialog(
            onDismiss = { showDialog = false },
            url = image
        )
    }
}

@Composable
fun ConversationItemBubble(
    modifier: Modifier = Modifier,
    message: ConversationEntity,
    isItemSelectionEnable: Boolean = false,
    onItemSelected: () -> Unit = {},
    onClick: () -> Unit,
    audioController: AudioPlayerController
) {
    val backgroundColor = bubbleColorPair(message.isMyMessage).first
    val contentColor = bubbleColorPair(message.isMyMessage).second
    val bubbleShape = bubbleCornerShape(message.isMyMessage)
    val alignment = if (message.isMyMessage) Alignment.End else Alignment.Start

    Row(
        verticalAlignment = Alignment.CenterVertically
    ) {
        if (isItemSelectionEnable)
            AppCheckbox(
                checked = message.isItemSelected,
                onCheckedChange = {
                    onItemSelected.invoke()
                }
            )
        Column(
            modifier = modifier
                .fillMaxWidth()
                .appPaddingOnly(top = SpacingToken.medium),
            horizontalAlignment = alignment
        ) {
            if (message.readableDateTime.isNotEmpty()) {
                DateDivider(message.readableDateTime)
                Spacer(
                    modifier = Modifier.height(SpacingToken.medium)
                )
            }

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
                ConversationBody(
                    message = message,
                    contentColor = contentColor,
                    alignment = alignment,
                    audioController = audioController,
                    onClick = onClick
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
    ConversationItemBubble(
        audioController = rememberAudioPlayerController(),
        message = ConversationEntity(
            messageId = "",
            isMyMessage = true,
            fromUsername = "John Doe",
            body = "",
            imageUrl = "",
            audioUrl = "",
            audioDuration = 10,
            videoUrl = "fsadfsfdasdf",
            videoDuration = 0,
            dateTime = "04:99 PM",
            readableDateTime = "",
        ),
        onClick = {}
    )
}


@Composable
@LightPreview
private fun DatePreview() {
    DateDivider(
        date = "Today"
    )
}