package com.friend.chatroom

import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.friend.chatroom.UiAction.*
import com.friend.entity.chatmessage.ChatItemApiEntity
import com.friend.ui.common.asString
import com.friend.ui.showToastMessage
import kotlinx.coroutines.launch

@Composable
fun ConversationScreenRoute(
    toUsername: String,
    fullName: String,
    imageUrl: String,
    onBackButtonClicked: () -> Unit,
    onNavigateToProfileScreen: (String) -> Unit,
    onNavigateToPlayerScreen: (String) -> Unit,
    onNavigateToReportScreen: (String) -> Unit,
    onNavigateToForwardMessageScreen: (List<String>) -> Unit,
    viewModel: ConversationViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val context = LocalContext.current
    val listState = rememberLazyListState()
    val scope = rememberCoroutineScope()

    LaunchedEffect(Unit) {
        viewModel.uiEvent.collect { event ->
            when (event) {
                is UiEvent.ShowToastMessage ->
                    context.showToastMessage(event.message.asString(context))

                UiEvent.DeleteMessageComplete -> viewModel.action(FetchMessages(toUsername))
                UiEvent.ResetScroll -> {
                    scope.launch {
                        listState.animateScrollToItem(0)
                    }
                }
            }
        }
    }

    DisposableEffect(Unit) {
        viewModel.startPolling(toUsername)
        onDispose { viewModel.stopPolling() }
    }

    ConversationScreen(
        uiState = uiState,
        listState = listState,
        toUsername = toUsername,
        fullName = fullName,
        imageUrl = imageUrl,
        onBackButtonClicked = onBackButtonClicked,
        onNavigateToProfileScreen = {
            onNavigateToProfileScreen.invoke(toUsername)
        },
        onAction = {
            viewModel.action(it)
        },
        onNavigateToReportScreen = {
            onNavigateToReportScreen.invoke(toUsername)
        },
        onNavigateToForwardMessageScreen = {
            onNavigateToForwardMessageScreen.invoke(uiState.conversations.filter { it.isItemSelected }
                .map { it.messageId })
        },
        onNavigateToPlayerScreen = onNavigateToPlayerScreen
    )
}