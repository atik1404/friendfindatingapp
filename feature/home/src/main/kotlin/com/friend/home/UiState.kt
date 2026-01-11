package com.friend.home

import com.friend.common.constant.Gender
import com.friend.entity.search.FriendSuggestionApiEntity

data class FilterUiState(
    val gender: Int = 1,
    val interestedIn: Int = 2,
    val fromAge: String = "18",
    val toAge: String = "99",
    val country: String? = null,
    val state: String? = null,
    val city: String? = null,
    val username: String = "",
    val isOnlineUser: Boolean = false,
    val isPhotoRequired: Boolean = false,
    val bodyType: String? = null,
    val lookingFor: String? = null,
    val eyes: String? = null,
    val hair: String? = null,
    val height: String? = null,
    val weight: String? = null,
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