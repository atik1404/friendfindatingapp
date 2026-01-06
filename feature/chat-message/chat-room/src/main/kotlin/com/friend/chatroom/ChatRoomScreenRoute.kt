package com.friend.chatroom

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.friend.common.dateparser.DateTimeParser
import com.friend.common.dateparser.DateTimePatterns
import com.friend.entity.chatmessage.ChatListItemApiEntity
import com.friend.ui.common.asString
import com.friend.ui.showToastMessage
import timber.log.Timber

@Composable
fun ChatRoomScreenRoute(
    chat: ChatListItemApiEntity,
    onBackButtonClicked: () -> Unit,
    onNavigateToProfileScreen: (String) -> Unit,
    viewModel: ChatRoomViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val context = LocalContext.current

    LaunchedEffect(Unit) {
        viewModel.action(UiActon.FetchMessages(chat.toUsername))
        viewModel.uiEvent.collect { event ->
            when (event) {
                is UiEvent.ShowToastMessage ->
                    context.showToastMessage(event.message.asString(context))
            }
        }
    }

    ChatRoomScreen(
        uiState = uiState,
        chat = chat,
        onBackButtonClicked = onBackButtonClicked,
        onNavigateToProfileScreen = {
            onNavigateToProfileScreen.invoke(chat.toUsername)
        },
        onAction = {
            viewModel.action(it)
        }
    )
}