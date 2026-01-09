package com.friend.chatlist

import com.friend.entity.chatmessage.ChatListItemApiEntity

data class UiState(
    val data: List<ChatListItemApiEntity> = emptyList(),
    val isLoading: Boolean = false,
    val isLoadingMore: Boolean = false,
    val hasMorePage: Boolean = false,
    val error: String = "",
    val searchKeyword: String = "",
    val pageNo: Int = 0
) {
    val isError: Boolean get() = error.isNotEmpty()
    val isFetchSuccess: Boolean get() = data.isNotEmpty()
    val isDataEmpty: Boolean get() = data.isEmpty() && !isLoading && !isLoadingMore
    val filteredItems: List<ChatListItemApiEntity>
        get() = if (searchKeyword.isBlank()) data
        else data.filter {
            it.fullName.contains(searchKeyword, ignoreCase = true)
        } // implement matches()
}

sealed interface UiAction {
    data object FetchChatList : UiAction
    data object LoadMore : UiAction
    data class SearchByKeyword(val value: String) : UiAction
}