package com.friend.forwardmessage

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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Send
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.friend.designsystem.spacing.SpacingToken
import com.friend.designsystem.spacing.appPaddingSymmetric
import com.friend.entity.chatmessage.ChatListItemApiEntity
import com.friend.forwardmessage.components.ChatListSection
import com.friend.forwardmessage.components.SearchBarSection
import com.friend.ui.common.AppToolbar
import com.friend.ui.common.ErrorType
import com.friend.ui.common.ErrorUi
import com.friend.ui.common.LoadingAnimation
import com.friend.ui.components.AppScaffold
import com.friend.ui.preview.LightPreview
import com.friend.designsystem.R as Res

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ForwardMessageScreen(
    uiState: UiState,
    action: (UiAction) -> Unit,
    onBackButtonClicked: () -> Unit,
    onForwardMessage: () -> Unit
) {
    AppScaffold(
        contentWindowInsets = WindowInsets.safeDrawing,
        topBar = {
            AppToolbar(
                title = stringResource(Res.string.title_friend_fin),
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
            if (uiState.isError)
                ErrorUi(
                    modifier = Modifier.fillMaxSize(),
                    message = uiState.error
                ) {
                    action.invoke(UiAction.FetchChatList)
                }

            if (uiState.isDataEmpty)
                ErrorUi(
                    modifier = Modifier.fillMaxSize(),
                    errorType = ErrorType.EMPTY_DATA,
                    message = stringResource(Res.string.error_no_data_found)
                ) {
                    action.invoke(UiAction.FetchChatList)
                }

            if (uiState.isFetchSuccess) {
                Column {
                    SearchBarSection(searchKeyword = uiState.searchKeyword) {
                        action.invoke(UiAction.SearchByKeyword(it))
                    }
                    Spacer(modifier = Modifier.height(SpacingToken.medium))

                    ChatListSection(
                        hasMorePage = uiState.hasMorePage,
                        items = uiState.filteredItems,
                        selectedUsers = uiState.selectedUsers,
                        onLoadMore = {
                            action.invoke(UiAction.FetchChatList)
                        },
                        onItemClicked = { entity ->
                            action.invoke(UiAction.UpdateSelection(entity.toUsername))
                        }
                    )
                }
            }

            if (uiState.isLoadingMore && uiState.isFetchSuccess) {
                LoadingAnimation(
                    modifier = Modifier
                        .align(alignment = Alignment.BottomCenter)
                )
            }

            if (uiState.isLoading)
                LoadingAnimation(
                    modifier = Modifier.align(alignment = Alignment.Center)
                )

            FabDrawableIcon(
                modifier = Modifier.align(Alignment.BottomEnd)
            ) {
                onForwardMessage.invoke()
            }
        }
    }
}

@Composable
fun FabDrawableIcon(
    modifier: Modifier = Modifier,
    onClick: () -> Unit) {
    FloatingActionButton(modifier = modifier, onClick = onClick) {
        Icon(
            imageVector = Icons.Filled.Send,
            contentDescription = "Action"
        )
    }
}

@Composable
@LightPreview
private fun ScreenPreview() {
    ForwardMessageScreen(
        onBackButtonClicked = {},
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
        action = {},
        onForwardMessage = {}
    )
}