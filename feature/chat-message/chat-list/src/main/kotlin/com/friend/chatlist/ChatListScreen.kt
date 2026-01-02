package com.friend.chatlist

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.friend.chatlist.components.ChatListSection
import com.friend.chatlist.components.SearchBarSection
import com.friend.designsystem.spacing.SpacingToken
import com.friend.designsystem.spacing.appPaddingSymmetric
import com.friend.entity.chatmessage.ChatListItemApiEntity
import com.friend.ui.common.AppToolbar
import com.friend.ui.common.ErrorType
import com.friend.ui.common.ErrorUi
import com.friend.ui.common.LoadingAnimation
import com.friend.ui.components.AppScaffold
import com.friend.ui.preview.LightPreview
import com.friend.designsystem.R as Res

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChatListScreen(
    uiState: UiState,
    action: (UiAction) -> Unit,
    onBackButtonClicked: () -> Unit,
    navigateToChatRoom: (ChatListItemApiEntity) -> Unit
) {
    AppScaffold(
        contentWindowInsets = WindowInsets.safeDrawing,
        topBar = {
            AppToolbar(
                title = stringResource(Res.string.title_chat),
                onBackClick = {
                    onBackButtonClicked.invoke()
                })
        }
    ) { padding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .consumeWindowInsets(padding)
                .navigationBarsPadding()
                .imePadding()
                .appPaddingSymmetric(
                    horizontal = SpacingToken.extraSmall,
                    vertical = SpacingToken.medium
                )
        ) {
            if (uiState.isLoading && uiState.data.isEmpty())
                LoadingAnimation(
                    modifier = Modifier.align(alignment = Alignment.Center)
                )
            Column {
                SearchBarSection(searchKeyword = uiState.searchKeyword){
                    action.invoke(UiAction.SearchByKeyword(it))
                }
                Spacer(modifier = Modifier.height(SpacingToken.medium))

                if (uiState.error.isNotEmpty())
                    ErrorUi(
                        message = uiState.error
                    ) {
                        action.invoke(UiAction.FetchChatList)
                    }

                if (uiState.data.isEmpty() && !uiState.isLoading)
                    ErrorUi(
                        errorType = ErrorType.EMPTY_DATA,
                        message = stringResource(Res.string.error_no_data_found)
                    ) {
                        action.invoke(UiAction.FetchChatList)
                    }
                else ChatListSection(
                    hasMorePage = uiState.hasMorePage,
                    items = uiState.filterData,
                    onLoadMore = {
                        action.invoke(UiAction.FetchChatList)
                    },
                    onItemClicked = { toUsername ->
                        navigateToChatRoom.invoke(toUsername)
                    }
                )
            }

            if (uiState.isLoadingMore && uiState.data.isNotEmpty()) {
                LoadingAnimation(
                    modifier = Modifier
                        .align(alignment = Alignment.BottomCenter)
                )
            }
        }
    }
}

@Composable
@LightPreview
private fun ScreenPreview() {
    ChatListScreen(
        onBackButtonClicked = {},
        navigateToChatRoom = { _ -> },
        uiState = UiState(
            data = listOf(
                ChatListItemApiEntity(
                    toUsername = "Tom Cruise",
                    notificationToken = "",
                    userImage = "",
                    fullName = "Tom Cruise",
                    lastMessage = "Hi, How are you?",
                    dateTime = "2025-12-16T10:25:47Z"
                ), ChatListItemApiEntity(
                    toUsername = "Tom Cruise",
                    notificationToken = "",
                    userImage = "",
                    fullName = "Tom Cruise",
                    lastMessage = "Hi, How are you?",
                    dateTime = "2025-12-16T10:25:47Z"
                ), ChatListItemApiEntity(
                    toUsername = "Tom Cruise",
                    notificationToken = "",
                    userImage = "",
                    fullName = "Tom Cruise",
                    lastMessage = "Hi, How are you?",
                    dateTime = "2025-12-16T10:25:47Z"
                ), ChatListItemApiEntity(
                    toUsername = "Tom Cruise",
                    notificationToken = "",
                    userImage = "",
                    fullName = "Tom Cruise",
                    lastMessage = "Hi, How are you?",
                    dateTime = "2025-12-16T10:25:47Z"
                ), ChatListItemApiEntity(
                    toUsername = "Tom Cruise",
                    notificationToken = "",
                    userImage = "",
                    fullName = "Tom Cruise",
                    lastMessage = "Hi, How are you?",
                    dateTime = "2025-12-16T10:25:47Z"
                ), ChatListItemApiEntity(
                    toUsername = "Tom Cruise",
                    notificationToken = "",
                    userImage = "",
                    fullName = "Tom Cruise",
                    lastMessage = "Hi, How are you?",
                    dateTime = "2025-12-16T10:25:47Z"
                )
            )
        ),
        action = {}
    )
}