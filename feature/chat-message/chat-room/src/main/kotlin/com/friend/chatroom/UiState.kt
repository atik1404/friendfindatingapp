package com.friend.chatroom

import com.friend.entity.chatmessage.ConversationEntity
import com.friend.ui.common.UiText

data class MessageState(
    val message: String = "",
    val image: String = "",
    val audio: String = "",
    val video: String = "",
    val audioDuration: String = "",
    val videoDuration: String = "",
    val isSending: Boolean = false,
)

data class UiState(
    val conversations: List<ConversationEntity> = emptyList(),
    val isLoading: Boolean = false,
    val error: String = "",
    val isSearchEnabled: Boolean = false,
    val searchKey: String = "",
    val message: MessageState = MessageState()
) {
    val isAlreadyFetched: Boolean get() = conversations.isNotEmpty()
    val isAnyItemSelected: Boolean get() = conversations.any { it.isItemSelected }
}

sealed interface UiEvent {
    data class ShowToastMessage(val message: UiText) : UiEvent
    data object DeleteMessageComplete : UiEvent
}

sealed interface UiAction {
    data class FetchMessages(val username: String) : UiAction
    data class SearchMessage(val username: String, val keyword: String) : UiAction
    data object OnClearSearch : UiAction
    data object OnClearMessageSelection : UiAction
    data object DeleteMessages : UiAction
    data class SendMessage(val toUsername: String) : UiAction
    data class OnChangeTextMessage(val message: String) : UiAction
    data class UpdateMessageSelectionStatus(val item: ConversationEntity) : UiAction
}