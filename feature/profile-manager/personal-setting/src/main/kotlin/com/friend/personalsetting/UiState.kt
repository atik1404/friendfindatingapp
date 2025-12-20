package com.friend.personalsetting

import com.friend.common.constant.Gender
import com.friend.domain.base.TextInput
import com.friend.entity.search.CityApiEntity
import com.friend.entity.search.CountryApiEntity
import com.friend.entity.search.StateApiEntity
import com.friend.ui.common.UiText

data class FormData(
    val name: TextInput = TextInput(),
    val email: TextInput = TextInput(),
    val password: TextInput = TextInput(),
    val dateOfBirth: TextInput = TextInput(),
    val postCode: TextInput = TextInput(),
    val gender: Gender? = null,
    val interestedIn: Gender? = null,
    val country: String? = null,
    val state: String? = null,
    val city: String? = null,
)

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
    data object FinishScreen : UiEvent
}

sealed class UiAction {
    data class SelectBirthDate(val value: String) : UiAction()
    data class ShowDatePicker(val isVisible: Boolean) : UiAction()
    object PerformProfileUpdate : UiAction()
    object SetDefaultData : UiAction()
    object FetchCountry : UiAction()
    data class SelectGender(val value: Gender) : UiAction()
    data class SelectInterestedIn(val value: Gender) : UiAction()
    data class OnChangeName(val value: String) : UiAction()
    data class OnChangeEmail(val value: String) : UiAction()
    data class OnChangePassword(val value: String) : UiAction()
    data class OnSelectCountry(val value: String) : UiAction()
    data class OnSelectState(val value: String) : UiAction()
    data class OnSelectCity(val value: String) : UiAction()
    data class OnChangePostCode(val value: String) : UiAction()
    object ResetState : UiAction()
}