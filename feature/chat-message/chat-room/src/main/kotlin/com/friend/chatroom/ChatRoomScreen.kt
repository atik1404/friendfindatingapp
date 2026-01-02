package com.friend.chatroom

import AppDivider
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import com.friend.chatroom.components.MessageFormUi
import com.friend.chatroom.components.MessagesUiSection
import com.friend.chatroom.components.TopBarUiSection
import com.friend.designsystem.spacing.SpacingToken
import com.friend.designsystem.spacing.appPaddingVertical
import com.friend.designsystem.theme.dividerColors
import com.friend.entity.chatmessage.ChatListItemApiEntity
import com.friend.ui.common.LoadingAnimation
import com.friend.ui.components.AppScaffold
import com.friend.ui.preview.LightPreview

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChatRoomScreen(
    uiState: UiState,
    chat: ChatListItemApiEntity,
    onBackButtonClicked: () -> Unit,
) {
    AppScaffold(
        contentWindowInsets = WindowInsets.safeDrawing,
    ) { padding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .consumeWindowInsets(padding)
                .navigationBarsPadding()
                .imePadding()
        ) {
            if (uiState.isLoading)
                LoadingAnimation(
                    modifier = Modifier.align(alignment = Alignment.Center)
                )

            ConstraintLayout(
                modifier = Modifier
                    .fillMaxSize()
            ) {
                val (topbar, divider, messageList, messageSendUi) = createRefs()

                TopBarUiSection(
                    userName = chat.fullName,
                    userImage = chat.userImage,
                    modifier = Modifier.constrainAs(topbar) {
                        top.linkTo(parent.top)
                        start.linkTo(parent.start)
                        end.linkTo(parent.end)
                    }
                ) {
                    onBackButtonClicked.invoke()
                }
                AppDivider(
                    modifier = Modifier.constrainAs(divider) {
                        top.linkTo(topbar.bottom)
                        start.linkTo(parent.start)
                        end.linkTo(parent.end)
                    },
                    color = MaterialTheme.dividerColors.primary
                )

                MessagesUiSection(
                    message = uiState.messages,
                    modifier = Modifier
                        .constrainAs(messageList) {
                            top.linkTo(divider.bottom)
                            bottom.linkTo(messageSendUi.top)
                            start.linkTo(parent.start)
                            end.linkTo(parent.end)
                            width = Dimension.fillToConstraints
                            height = Dimension.fillToConstraints
                        }
                        .appPaddingVertical(SpacingToken.extraSmall)
                )

                MessageFormUi(
                    modifier = Modifier
                        .constrainAs(messageSendUi) {
                            bottom.linkTo(parent.bottom)
                            start.linkTo(parent.start)
                            end.linkTo(parent.end)
                        }
                )
            }
        }
    }
}

@Composable
@LightPreview
private fun ScreenPreview() {
    ChatRoomScreen(
        uiState = UiState(),
        chat = ChatListItemApiEntity(
            toUsername = "Tom Cruise",
            notificationToken = "",
            userImage = "",
            fullName = "Tom Cruise",
            lastMessage = "Hi, How are you?",
            dateTime = "2025-12-16"
        )
    ) {

    }
}