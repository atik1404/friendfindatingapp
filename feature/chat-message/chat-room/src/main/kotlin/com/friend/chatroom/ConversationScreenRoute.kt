package com.friend.chatroom

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.friend.entity.chatmessage.ChatItemApiEntity
import com.friend.ui.common.asString
import com.friend.ui.showToastMessage

@Composable
fun ConversationScreenRoute(
    chat: ChatItemApiEntity,
    onBackButtonClicked: () -> Unit,
    onNavigateToProfileScreen: (String) -> Unit,
    onNavigateToReportScreen: (String) -> Unit,
    onNavigateToForwardMessageScreen: (List<String>) -> Unit,
    viewModel: ConversationViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val context = LocalContext.current

    LaunchedEffect(Unit) {
        viewModel.action(UiAction.FetchMessages(chat.toUsername))
        viewModel.uiEvent.collect { event ->
            when (event) {
                is UiEvent.ShowToastMessage ->
                    context.showToastMessage(event.message.asString(context))
                UiEvent.DeleteMessageComplete -> viewModel.action(UiAction.FetchMessages(chat.toUsername))
            }
        }
    }

    ConversationScreen(
        uiState = uiState,
        chat = chat,
        onBackButtonClicked = onBackButtonClicked,
        onNavigateToProfileScreen = {
            onNavigateToProfileScreen.invoke(chat.toUsername)
        },
        onAction = {
            viewModel.action(it)
        },
        onNavigateToReportScreen = {
            onNavigateToReportScreen.invoke(chat.toUsername)
        },
        onNavigateToForwardMessageScreen = {
            onNavigateToForwardMessageScreen.invoke(uiState.conversations.filter { it.isItemSelected }
                .map { it.messageId })
        }
    )
}