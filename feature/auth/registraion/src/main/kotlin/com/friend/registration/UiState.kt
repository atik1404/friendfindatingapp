package com.friend.registration

data class FormData(
    val username: String = "",
    val name: String = "",
    val email: String = "",
    val password: String = "",
    val gender: String = "",
    val interestedIn: String = "",
    val dateOfBirth: String = "",
    val country: String = "",
    val state: String = "",
    val city: String = "",
    val postCode: String = "",
    val isAgree: Boolean = false,
    val isLoading: Boolean = false,
)

sealed interface UiState {
    data class Default(val data: FormData) : UiState
    object Loading : UiState
}

sealed interface UiEvent {
    data class ShowToastMessage(val message: String) : UiEvent
}

sealed class UiAction {
    data class CheckPrivacyPolicy(val value: Boolean) : UiAction()
    data class SelectBirthDate(val value: String) : UiAction()
    object FormValidation : UiAction()
    object FetchCountry : UiAction()
    data class FetchState(val country: String) : UiAction()
    data class FetchCity(val country: String, val state: String) : UiAction()
    data class SelectGender(val value: String) : UiAction()
    data class SelectInterestedIn(val value: String) : UiAction()
    data class OnChangeUserName(val value: String) : UiAction()
    data class OnChangeName(val value: String) : UiAction()
    data class OnChangeEmail(val value: String) : UiAction()
    data class OnChangePassword(val value: String) : UiAction()
    data class OnSelectCountry(val value: String) : UiAction()
    data class OnSelectState(val value: String) : UiAction()
    data class OnSelectCity(val value: String) : UiAction()
    data class OnChangePostCode(val value: String) : UiAction()
}