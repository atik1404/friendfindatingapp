package com.friend.chatroom

import com.friend.entity.chatmessage.MessageEntity
import com.friend.ui.common.UiText

data class UiState(
    val messages: List<MessageEntity> = emptyList(),
    val isLoading: Boolean = false,
    val error: String = "",
    val isSearchEnabled: Boolean = false,
    val searchKey: String = ""
) {
    val isAlreadyFetched: Boolean get() = messages.isNotEmpty()
}

sealed interface UiEvent {
    data class ShowToastMessage(val message: UiText) : UiEvent
    data object DeleteMessageComplete: UiEvent
    data object ForwardMessageComplete: UiEvent
}

sealed interface UiActon {
    data class FetchMessages(val username: String) : UiActon
    data class SearchMessage(val username: String, val keyword: String) : UiActon
    data object OnClearSearch : UiActon
    data object OnClearMessageSelection : UiActon
    data class ForwardMessages(val toUserNames: List<String>) : UiActon
    data object DeleteMessages : UiActon
    data class UpdateMessageSelectionStatus(val item: MessageEntity) : UiActon
}