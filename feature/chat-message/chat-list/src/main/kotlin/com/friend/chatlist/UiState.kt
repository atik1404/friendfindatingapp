package com.friend.chatlist

import com.friend.entity.chatmessage.ChatListItemApiEntity

sealed interface UiState {
    data class Success(val data: List<ChatListItemApiEntity>) : UiState
    data class Error(val message: String) : UiState
    object NoDataFound : UiState
    data object Loading : UiState
}

sealed interface LoadingState {
    data object Loading : LoadingState
    data object LoadingMore : LoadingState
}

sealed interface UiEvent {

}

sealed interface UiAction {
    data object FetchChatList : UiAction
    data object LoadMore : UiAction
    data object SearchByKeyword : UiAction
}