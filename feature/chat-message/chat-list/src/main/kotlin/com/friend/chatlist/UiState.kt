package com.friend.chatlist

import com.friend.entity.chatmessage.ChatListItemApiEntity

data class UiState(
    val data: List<ChatListItemApiEntity> = emptyList(),
    val filterData: List<ChatListItemApiEntity> = emptyList(),
    val isLoading: Boolean = false,
    val isLoadingMore: Boolean = false,
    val hasMorePage: Boolean = false,
    val error: String = "",
    val searchKeyword: String = "",
    val pageNo: Int = 0
)

sealed interface UiAction {
    data object FetchChatList : UiAction
    data object LoadMore : UiAction
    data class SearchByKeyword(val value: String) : UiAction
    data object ResetState : UiAction
}