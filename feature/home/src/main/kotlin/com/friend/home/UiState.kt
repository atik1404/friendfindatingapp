package com.friend.home

import com.friend.common.constant.Gender
import com.friend.entity.search.FriendSuggestionApiEntity

data class FilterUiState(
    val gender: Int? = null,
    val interestedIn: Int? = null,
    val fromAge: String? = null,
    val toAge: String? = null,
    val country: String? = null,
    val state: String? = null,
    val city: String? = null,
    val username: String? = null,
    val isOnlineUser: Boolean? = null,
    val isPhotoRequired: Boolean? = null,
    val bodyType: String? = null,
    val lookingFor: String? = null,
    val eyes: String? = null,
    val hair: String? = null,
    val height: String? = null,
    val weight: String? = null,
    val smoking: String? = null,
    val drinking: String? = null,
)

data class UiState(
    val isLoading: Boolean = false,
    val data: List<FriendSuggestionApiEntity> = emptyList(),
    val error: String = "",
    val isLoadingMore: Boolean = false,
    val pageNo: Int = 0,
    val hasMorePage: Boolean = true,
)

sealed class UiEvent {
    object NavigateToProfileScreen : UiEvent()
    object NavigateToOverviewScreen : UiEvent()
    object NavigateToChatMessageScreen : UiEvent()
}

sealed interface UiAction {
    object SetCurrentUserInfo : UiAction
    object FetchFriendSuggestion : UiAction
    object ResetFilter : UiAction
    object OnFilterApply : UiAction
    data class OnChangeUsername(val value: String) : UiAction
    data class OnChangeGender(val value: Gender) : UiAction
    data class OnChangeInterested(val value: Gender) : UiAction
    data class OnChangeFromAge(val value: String) : UiAction
    data class OnChangeToAge(val value: String) : UiAction
    data class HeightChanged(val value: String) : UiAction
    data class WeightChanged(val value: String) : UiAction
    data class EyesChanged(val value: String) : UiAction
    data class HairChanged(val value: String) : UiAction
    data class SmokingChanged(val value: String) : UiAction
    data class DrinkingChanged(val value: String) : UiAction
    data class BodyTypeChanged(val value: String) : UiAction
    data class LookingForChanged(val value: String) : UiAction
    data class PhotoRequiredChanged(val value: Boolean) : UiAction
    data class OnlineUserChanged(val value: Boolean) : UiAction
}