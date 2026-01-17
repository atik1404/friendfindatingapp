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
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.dp
import com.friend.chatroom.components.ConversionsUiSection
import com.friend.chatroom.components.SearchResultCountUi
import com.friend.chatroom.components.TopBarUiSection
import com.friend.chatroom.components.UserInputAndAttachment
import com.friend.chatroom.components.conversation.JumpToBottom
import com.friend.designsystem.spacing.SpacingToken
import com.friend.designsystem.spacing.appPaddingOnly
import com.friend.designsystem.theme.dividerColors
import com.friend.entity.chatmessage.ChatItemApiEntity
import com.friend.ui.common.LoadingUi
import com.friend.ui.components.AppScaffold
import com.friend.ui.preview.LightPreview
import kotlinx.coroutines.launch

private val JumpToBottomThreshold = 56.dp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ConversationScreen(
    modifier: Modifier = Modifier,
    uiState: UiState,
    chat: ChatItemApiEntity,
    onBackButtonClicked: () -> Unit,
    onNavigateToProfileScreen: () -> Unit,
    onNavigateToReportScreen: () -> Unit,
    onNavigateToForwardMessageScreen: () -> Unit,
    onAction: (UiAction) -> Unit,
) {
    var isItemSelectionEnable by remember { mutableStateOf(false) }

    val listState = rememberLazyListState()
    val scope = rememberCoroutineScope()

    val jumpThreshold = with(LocalDensity.current) {
        JumpToBottomThreshold.toPx()
    }

    val jumpToBottomButtonEnabled by remember {
        derivedStateOf {
            listState.firstVisibleItemIndex != 0 ||
                    listState.firstVisibleItemScrollOffset > jumpThreshold
        }
    }

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
                    fullName = chat.fullName,
                    userImage = chat.userImage,
                    onProfileImageClicked = onNavigateToProfileScreen,
                    onBackButtonClicked = onBackButtonClicked,
                    onSearchCanceled = {
                        onAction.invoke(UiAction.OnClearSearch)
                        onAction.invoke(UiAction.FetchMessages(chat.toUsername))
                    },
                    onSearchApply = {
                        onAction.invoke(
                            UiAction.SearchMessage(
                                chat.toUsername,
                                it
                            )
                        )
                    },
                    onReportAbuse = onNavigateToReportScreen,
                    isItemSelectionEnable = isItemSelectionEnable,
                    onSelectionCancel = {
                        isItemSelectionEnable = false
                        onAction.invoke(UiAction.OnClearMessageSelection)
                    },
                    onForwardMessage = {
                        if (uiState.isAnyItemSelected) {
                            isItemSelectionEnable = false
                            onNavigateToForwardMessageScreen.invoke()
                        }
                    },
                    onDeleteMessage = {
                        if (uiState.isAnyItemSelected) {
                            isItemSelectionEnable = false
                            onAction.invoke(UiAction.DeleteMessages)
                        }
                    }
                )

                AppDivider(
                    color = MaterialTheme.dividerColors.primary
                )

                ConversionsUiSection(
                    listState = listState,
                    message = uiState.conversations,
                    modifier = modifier
                        .fillMaxWidth()
                        .weight(1f),
                    isItemSelectionEnable = isItemSelectionEnable,
                    onLongPress = {
                        isItemSelectionEnable = true
                    },
                    onItemSelected = {
                        onAction.invoke(UiAction.UpdateMessageSelectionStatus(it))
                    }
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
                        count = uiState.conversations.size,
                        keyword = uiState.searchKey
                    )
                }

            JumpToBottom(
                // Only show if the scroller is not at the bottom
                enabled = jumpToBottomButtonEnabled,
                onClicked = {
                    scope.launch {
                        listState.animateScrollToItem(0)
                    }
                },
                modifier = Modifier.align(Alignment.BottomCenter),
            )
        }
    }
}

@Composable
@LightPreview
private fun ScreenPreview() {
    ConversationScreen(
        uiState = UiState(),
        chat = ChatItemApiEntity(
            toUsername = "Tom Cruise",
            notificationToken = "",
            userImage = "",
            fullName = "Tom CruiseTom Cruise",
            lastMessage = "Hi, How are you?",
            dateTime = "2025-12-16"
        ),
        onBackButtonClicked = {},
        onNavigateToProfileScreen = {},
        onAction = {},
        onNavigateToReportScreen = {},
        onNavigateToForwardMessageScreen = {},
    )
}