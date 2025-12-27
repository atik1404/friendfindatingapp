package com.friend.profile

import com.friend.entity.profilemanager.ProfileApiEntity
import com.friend.ui.common.UiText
import java.io.File

sealed interface UiState {
    data object Loading : UiState
    data class ApiError(val message: String) : UiState
    data class ShowProfileData(val profile: ProfileApiEntity) : UiState
}

data class ImageUiState(
    val isLoading: Boolean = false,
    val error: String? = null,
)

sealed interface UiEvent {
    data class ShowToastMessage(val uiText: UiText) : UiEvent
}

sealed interface UiAction {
    data object FetchProfile : UiAction
    data class UpdateProfilePicture(val image: File) : UiAction
}