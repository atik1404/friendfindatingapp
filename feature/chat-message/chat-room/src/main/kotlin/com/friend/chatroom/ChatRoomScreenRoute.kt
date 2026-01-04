package com.friend.chatroom

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.friend.common.dateparser.DateTimeParser
import com.friend.common.dateparser.DateTimePatterns
import com.friend.entity.chatmessage.ChatListItemApiEntity
import timber.log.Timber

@Composable
fun ChatRoomScreenRoute(
    chat: ChatListItemApiEntity,
    onBackButtonClicked: () -> Unit,
    viewModel: ChatRoomViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    LaunchedEffect(Unit) {
        //viewModel.action(UiActon.ResetState)
        Timber.e("dateFormat: ${DateTimeParser.parseToPattern("2026-01-02T10:57:03.56",
            DateTimePatterns.MDY_TEXT_COMMA)}")
        Timber.e("dateFormat: ${DateTimeParser.parseToPattern("2026-01-02T12:30:28.363",
            DateTimePatterns.MDY_TEXT_COMMA)}")
        viewModel.action(UiActon.FetchMessages(chat.toUsername))
    }

    ChatRoomScreen(
        uiState = uiState,
        chat = chat,
        onBackButtonClicked = onBackButtonClicked
    )
}