package com.friend.chatlist

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.friend.entity.chatmessage.ChatListItemApiEntity

@Composable
fun ChatListScreenRoute(
    onBackButtonClicked: () -> Unit,
    navigateToChatRoom: (ChatListItemApiEntity) -> Unit,
    viewModel: ChatListViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    LaunchedEffect(Unit) {
        viewModel.action.invoke(UiAction.FetchChatList)
    }

    ChatListScreen(
        uiState = uiState,
        action = viewModel.action,
        onBackButtonClicked = onBackButtonClicked,
        navigateToChatRoom = navigateToChatRoom
    )
}