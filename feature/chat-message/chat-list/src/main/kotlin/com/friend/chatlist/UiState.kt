package com.friend.chatlist

import com.friend.entity.chatmessage.ChatListItemApiEntity

sealed interface UiState {
    data object Idle : UiState
    data object Loading : UiState
    data class Success(val data: List<ChatListItemApiEntity>) : UiState
    data class Error(val message: String) : UiState
    object NoDataFound : UiState
}

sealed interface UiEvent {

}

sealed interface UiAction {
    data object FetchChatList : UiAction
    data object SearchByKeyword : UiAction
}