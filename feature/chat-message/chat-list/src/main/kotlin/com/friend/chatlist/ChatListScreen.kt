package com.friend.chatlist

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
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.friend.chatlist.components.ChatListSection
import com.friend.chatlist.components.SearchBarSection
import com.friend.designsystem.spacing.SpacingToken
import com.friend.designsystem.spacing.appPaddingSymmetric
import com.friend.entity.chatmessage.ChatListItemApiEntity
import com.friend.ui.common.AppToolbar
import com.friend.ui.common.ErrorUi
import com.friend.ui.common.LoadingUi
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
        Column(
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
            SearchBarSection()
            Spacer(modifier = Modifier.height(SpacingToken.medium))

            when (uiState) {
                is UiState.Error -> ErrorUi(
                    message = uiState.message
                ) {
                    action.invoke(UiAction.FetchChatList)
                }

                UiState.Idle -> {}
                UiState.Loading -> LoadingUi()
                UiState.NoDataFound -> ErrorUi(
                    message = stringResource(Res.string.error_no_data_found)
                ) {
                    action.invoke(UiAction.FetchChatList)
                }

                is UiState.Success -> ChatListSection(
                    items = uiState.data
                ) { toUsername ->
                    navigateToChatRoom.invoke(toUsername)
                }
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
        uiState = UiState.NoDataFound,
        action = {}
    )
}