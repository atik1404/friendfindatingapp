package com.friend.otherprofile

import com.friend.entity.profilemanager.OtherProfileApiEntity
import com.friend.ui.common.UiText

sealed interface UiState {
    data object Loading : UiState
    data class ApiError(val message: String) : UiState
    data class ShowProfileData(val data: OtherProfileApiEntity) : UiState
}

sealed interface UiEvent {
    data class ShowToastMessage(val uiText: UiText) : UiEvent
}

sealed interface UiAction {
    data class FetchProfile(val username: String) : UiAction
    data object UpdateProfilePicture : UiAction
}