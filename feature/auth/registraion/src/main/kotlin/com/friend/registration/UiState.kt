package com.friend.registration

import com.friend.common.constant.Gender
import com.friend.domain.base.TextInput
import com.friend.entity.search.CityApiEntity
import com.friend.entity.search.CountryApiEntity
import com.friend.entity.search.StateApiEntity
import com.friend.ui.common.UiText

data class FormData(
    val username: TextInput = TextInput(),
    val name: TextInput = TextInput(),
    val email: TextInput = TextInput(),
    val password: TextInput = TextInput(),
    val dateOfBirth: TextInput = TextInput(),
    val postCode: TextInput = TextInput(),
    val gender: Gender ? = null,
    val interestedIn: Gender ? = null,
    val country: CountryApiEntity? = null,
    val state: StateApiEntity? = null,
    val city: CityApiEntity? = null,
    val isAgree: Boolean = false,
) {
    val isFormValid: Boolean
        get() = username.isValid
}

data class UiState(
    val form: FormData = FormData(),

    // POST loading (submit button)
    val isSubmitting: Boolean = false,
    val showDatePicker: Boolean = false,

    // GET loading (for full-screen loader)
    val isLoading: Boolean = false,

    // API data
    val countries: List<CountryApiEntity> = emptyList(),
    val states: List<StateApiEntity> = emptyList(),
    val cities: List<CityApiEntity> = emptyList(),
)

sealed interface UiEvent {
    data class ShowToastMessage(val message: UiText) : UiEvent
    data object NavigateToProfileCompletion : UiEvent
}

sealed class UiAction {
    data class CheckPrivacyPolicy(val value: Boolean) : UiAction()
    data class SelectBirthDate(val value: String) : UiAction()
    data class ShowDatePicker(val isVisible: Boolean) : UiAction()
    object FormValidation : UiAction()
    object FetchCountry : UiAction()
    data class SelectGender(val value: Gender) : UiAction()
    data class SelectInterestedIn(val value: Gender) : UiAction()
    data class OnChangeUserName(val value: String) : UiAction()
    data class OnChangeName(val value: String) : UiAction()
    data class OnChangeEmail(val value: String) : UiAction()
    data class OnChangePassword(val value: String) : UiAction()
    data class OnSelectCountry(val value: CountryApiEntity) : UiAction()
    data class OnSelectState(val value: StateApiEntity) : UiAction()
    data class OnSelectCity(val value: CityApiEntity) : UiAction()
    data class OnChangePostCode(val value: String) : UiAction()
    object ResetState : UiAction()
}