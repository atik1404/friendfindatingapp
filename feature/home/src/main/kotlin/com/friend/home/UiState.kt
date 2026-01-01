package com.friend.home

import com.friend.entity.search.FriendSuggestionApiEntity

data class FilterParameters(
    val gender: Int? = null,
    val fromAge: Int? = null,
    val toAge: Int? = null,
    val country: String? = null,
    val state: String? = null,
    val city: String? = null,
    val username: String? = null,
    val isOnlineUser: String? = null,
    val bodyType: String? = null,
    val lookingFor: String? = null,
    val eyes: String? = null,
    val hair: String? = null,
    val smoking: String? = null,
    val drinking: String? = null,
)

data class UiState(
    val isLoading: Boolean = false,
    val data: List<FriendSuggestionApiEntity> = emptyList(),
    val error: String = "",
    val isLoadingMore: Boolean = false,
    val pageNo: Int = 1,
    val hasMorePage: Boolean = true,
    val filterBy: FilterParameters = FilterParameters(),
)

sealed class UiEvent {
    object NavigateToProfileScreen : UiEvent()
    object NavigateToOverviewScreen : UiEvent()
    object NavigateToChatMessageScreen : UiEvent()
}

sealed class UiAction {
    object FetchFriendSuggestion : UiAction()
}