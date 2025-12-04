package com.friend.home

import com.friend.entity.search.FriendSuggestionApiEntity

sealed interface UiState {
    data object Idle : UiState
    data object Loading : UiState
    data class Success(val data: List<FriendSuggestionApiEntity>) : UiState
    data class Error(val message: String) : UiState
    object NoDataFound : UiState
}

sealed class UiEvent {
    object NavigateToProfileScreen : UiEvent()
    object NavigateToOverviewScreen : UiEvent()
    object NavigateToChatMessageScreen : UiEvent()
}

sealed class UiAction {
    object FetchFriendSuggestion : UiAction()
}