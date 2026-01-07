package com.friend.chatroom

import AppDivider
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.friend.chatroom.components.MessagesUiSection
import com.friend.chatroom.components.SearchResultCountUi
import com.friend.chatroom.components.TopBarUiSection
import com.friend.chatroom.components.UserInputAndAttachment
import com.friend.designsystem.spacing.SpacingToken
import com.friend.designsystem.spacing.appPaddingOnly
import com.friend.designsystem.theme.dividerColors
import com.friend.entity.chatmessage.ChatListItemApiEntity
import com.friend.ui.common.LoadingAnimation
import com.friend.ui.common.LoadingUi
import com.friend.ui.components.AppScaffold
import com.friend.ui.preview.LightPreview

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChatRoomScreen(
    modifier: Modifier = Modifier,
    uiState: UiState,
    chat: ChatListItemApiEntity,
    onBackButtonClicked: () -> Unit,
    onNavigateToProfileScreen: () -> Unit,
    onAction: (UiActon) -> Unit,
) {
    AppScaffold(
        contentWindowInsets = WindowInsets.safeDrawing,
    ) { padding ->
        Box(
            modifier = modifier
                .fillMaxSize()
                .padding(padding)
                .consumeWindowInsets(padding)
                .navigationBarsPadding()
                .imePadding()
        ) {
            Column(
                modifier = modifier
                    .fillMaxSize()
            ) {
                TopBarUiSection(
                    modifier = modifier
                        .statusBarsPadding()
                        .navigationBarsPadding()
                        .imePadding(),
                    userName = chat.fullName,
                    userImage = chat.userImage,
                    onProfileImageClicked = onNavigateToProfileScreen,
                    onBackButtonClicked = onBackButtonClicked,
                    onSearchCanceled = {
                        onAction.invoke(UiActon.OnClearSearch)
                        onAction.invoke(UiActon.FetchMessages(chat.toUsername))
                    },
                    onSearchApply = { onAction.invoke(UiActon.SearchMessage(chat.toUsername, it)) }
                )

                AppDivider(
                    color = MaterialTheme.dividerColors.primary
                )

                MessagesUiSection(
                    message = uiState.messages,
                    modifier = modifier
                        .fillMaxWidth()
                        .weight(1f),
                )

                UserInputAndAttachment(
                    modifier = modifier
                        .appPaddingOnly(top = SpacingToken.medium),
                )
            }

            if (uiState.isLoading)
                LoadingUi()

            if (uiState.isSearchEnabled)
                Column(
                    modifier = modifier
                        .align(alignment = Alignment.TopCenter)
                        .appPaddingOnly(top = SpacingToken.hugePlusPlusPlus),
                ) {
                    SearchResultCountUi(
                        count = uiState.messages.size,
                        keyword = uiState.searchKey
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
        ),
        onBackButtonClicked = {},
        onNavigateToProfileScreen = {},
        onAction = {},
    )
}