package com.friend.chatroom

import com.friend.entity.chatmessage.MessageEntity
import com.friend.ui.common.UiText

data class UiState(
    val messages: List<MessageEntity> = emptyList(),
    val isLoading: Boolean = false,
    val error: String = "",
)

sealed interface UiEvent {
    data class ShowToastMessage(val message: UiText) : UiEvent
}

sealed interface UiActon {
    data class FetchMessages(val username: String) : UiActon
    data object ResetState: UiActon
}