package com.friend.forwardmessage

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.friend.ui.common.asString
import com.friend.ui.showToastMessage

@Composable
fun ForwardMessageScreenRoute(
    messages: List<String>,
    onBackButtonClicked: () -> Unit,
    viewModel: ForwardMessageViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val context = LocalContext.current

    LaunchedEffect(Unit) {
        viewModel.action.invoke(UiAction.FetchChatList)
        viewModel.uiEvent.collect { event ->
            when (event) {
                UiEvent.ForwardMessageComplete -> onBackButtonClicked.invoke()
                is UiEvent.ShowToastMessage -> context.showToastMessage(
                    event.message.asString(
                        context
                    )
                )
            }
        }
    }

    ForwardMessageScreen(
        uiState = uiState,
        action = {
            viewModel.action.invoke(it)
        },
        onBackButtonClicked = onBackButtonClicked,
        onForwardMessage = {
            viewModel.action.invoke(UiAction.ForwardMessages(messages))
        }
    )
}