package com.friend.forwardmessage

import com.friend.entity.chatmessage.ChatItemApiEntity
import com.friend.ui.common.UiText

data class UiState(
    val data: List<ChatItemApiEntity> = emptyList(),
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
    val filteredItems: List<ChatItemApiEntity>
        get() = if (searchKeyword.isBlank()) data
        else data.filter {
            it.fullName.contains(searchKeyword, ignoreCase = true)
        }
}

sealed interface UiEvent {
    data class ShowToastMessage(val message: UiText) : UiEvent
    data object ForwardMessageComplete : UiEvent
}

sealed interface UiAction {
    data object FetchChatList : UiAction
    data object LoadMore : UiAction
    data class SearchByKeyword(val value: String) : UiAction
    data class UpdateSelection(val username: String) : UiAction
    data class ForwardMessages(val messageIds: List<String>) : UiAction
}