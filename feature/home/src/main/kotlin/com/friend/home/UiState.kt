package com.friend.home

import com.friend.entity.search.CityApiEntity
import com.friend.entity.search.CountryApiEntity
import com.friend.entity.search.FriendSuggestionApiEntity
import com.friend.entity.search.StateApiEntity

data class FilterUiState(
    val gender: Int = 1,
    val interestedIn: Int = 2,
    val fromAge: String = "18",
    val toAge: String = "99",
    val country: String = "",
    val state: String = "",
    val city: String = "",
    val username: String = "",
    val isOnlineUser: Boolean = false,
    val isPhotoRequired: Boolean = false,
    val bodyType: String? = null,
    val lookingFor: String? = null,
    val eyes: String? = null,
    val hair: String? = null,
    val smoking: String? = null,
    val drinking: String? = null,
    val isSearchApply: Boolean = false,
)

data class UiState(
    val isLoading: Boolean = false,
    val data: List<FriendSuggestionApiEntity> = emptyList(),
    val error: String = "",
    val isLoadingMore: Boolean = false,
    val pageNo: Int = 0,
    val hasMorePage: Boolean = true,
)

data class LocationState(
    val countries: List<CountryApiEntity> = emptyList(),
    val states: List<StateApiEntity> = emptyList(),
    val cities: List<CityApiEntity> = emptyList(),
)

sealed interface UiAction {
    object CurrentUserInfo : UiAction
    object FetchFriendSuggestion : UiAction
    object ResetFilter : UiAction
    data class OnFilterApply(val value: FilterUiState) : UiAction
    data class FetchState(val country: String) : UiAction
    data class FetchCity(val country: String, val state: String) : UiAction
}