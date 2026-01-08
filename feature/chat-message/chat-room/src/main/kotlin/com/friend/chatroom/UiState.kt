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
    val isAnyItemSelected: Boolean get() = messages.any { it.isItemSelected }
}

sealed interface UiEvent {
    data class ShowToastMessage(val message: UiText) : UiEvent
    data object DeleteMessageComplete: UiEvent
}

sealed interface UiAction {
    data class FetchMessages(val username: String) : UiAction
    data class SearchMessage(val username: String, val keyword: String) : UiAction
    data object OnClearSearch : UiAction
    data object OnClearMessageSelection : UiAction
    data object DeleteMessages : UiAction
    data class UpdateMessageSelectionStatus(val item: MessageEntity) : UiAction
}