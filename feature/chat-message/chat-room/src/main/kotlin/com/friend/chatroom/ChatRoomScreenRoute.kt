package com.friend.chatroom

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.friend.entity.chatmessage.ChatListItemApiEntity

@Composable
fun ChatRoomScreenRoute(
    chat: ChatListItemApiEntity,
    onBackButtonClicked: () -> Unit,
    viewModel: ChatRoomViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    LaunchedEffect(Unit) {
        viewModel.action(UiActon.ResetState)
        viewModel.action(UiActon.FetchMessages(chat.toUsername))
    }

    ChatRoomScreen(
        uiState = uiState,
        chat = chat,
        onBackButtonClicked = onBackButtonClicked
    )
}