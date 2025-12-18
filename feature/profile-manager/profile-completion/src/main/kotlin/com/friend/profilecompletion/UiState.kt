package com.friend.profilecompletion

import com.friend.common.constant.AppConstants
import com.friend.domain.base.TextInput
import com.friend.ui.common.UiText

data class SelectionData(
    val height: List<String> = AppConstants.Height,
    val weight: List<String> = AppConstants.Weight,
    val eyes: List<String> = AppConstants.Eyes,
    val hair: List<String> = AppConstants.Hairs,
    val smoking: List<String> = AppConstants.Smoking,
    val drinking: List<String> = AppConstants.Drinking,
    val bodyType: List<String> = AppConstants.BodyTypes,
    val lookingFor: List<String> = AppConstants.LookingFor,
    val interests: List<String> = AppConstants.interest,
)

data class UiState(
    val height: String = "",
    val weight: String = "",
    val eyes: String = "",
    val hair: String = "",
    val smoking: String = "",
    val drinking: String = "",
    val bodyType: String = "",
    val lookingFor: String = "",
    val title: TextInput = TextInput(),
    val aboutYou: TextInput = TextInput(),
    val whatsUp: TextInput = TextInput(),
    val interests: List<String> = emptyList(),
    val selectionData: SelectionData = SelectionData(),
    val isSubmitting: Boolean = false,
    val isLoading: Boolean = false,
    val isFailedToFetchProfile: Boolean = false,
    val apiErrorMessage: String = ""
)

sealed interface UiEvent {
    data class ShowToastMessage(val message: UiText) : UiEvent
    data object NavigateToHome : UiEvent
}

sealed interface UiAction {
    data class HeightChanged(val value: String) : UiAction
    data class WeightChanged(val value: String) : UiAction
    data class EyesChanged(val value: String) : UiAction
    data class HairChanged(val value: String) : UiAction
    data class SmokingChanged(val value: String) : UiAction
    data class DrinkingChanged(val value: String) : UiAction
    data class BodyTypeChanged(val value: String) : UiAction
    data class LookingForChanged(val value: String) : UiAction
    data class TitleChanged(val value: String) : UiAction
    data class AboutYouChanged(val value: String) : UiAction
    data class WhatsUpChanged(val value: String) : UiAction
    data class InterestsChanged(val value: Set<String>) : UiAction
    object ResetState : UiAction
    object SetDefaultData : UiAction
    object FormSubmit : UiAction
    object FetchProfile : UiAction
}