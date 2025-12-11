package com.friend.registration

import com.friend.common.base.BaseViewModel
import com.friend.common.constant.Gender
import com.friend.domain.apiusecase.credential.PostRegistrationApiUseCase
import com.friend.domain.apiusecase.search.FetchCityApiUseCase
import com.friend.domain.apiusecase.search.FetchCountriesUseCase
import com.friend.domain.apiusecase.search.FetchStateApiUseCase
import com.friend.domain.base.ApiResult
import com.friend.domain.validator.RegistrationIoResult
import com.friend.entity.search.CityApiEntity
import com.friend.entity.search.CountryApiEntity
import com.friend.entity.search.StateApiEntity
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class RegistrationViewModel @Inject constructor(
    private val fetchCityApiUseCase: FetchCityApiUseCase,
    private val fetchStateApiUseCase: FetchStateApiUseCase,
    private val fetchCountryApiUseCase: FetchCountriesUseCase,
    private val postRegistrationApiUseCase: PostRegistrationApiUseCase,
) : BaseViewModel() {
    val ioError get() = postRegistrationApiUseCase.ioError.receiveAsFlow()

    private val _formUiState = MutableStateFlow(UiState())
    val uiState: StateFlow<UiState> = _formUiState.asStateFlow()

    private val _uiEvent = Channel<UiEvent>()
    val uiEvent = _uiEvent.receiveAsFlow()

    val action: (UiAction) -> Unit = {
        when (it) {
            is UiAction.FetchCity -> fetchCities()
            UiAction.FetchCountry -> fetchCountries()
            is UiAction.FetchState -> fetchStates()
            UiAction.FormValidation -> performRegistration()
            is UiAction.OnChangeUserName -> onChangeUserName(it.value)
            is UiAction.OnChangeEmail -> onChangeEmail(it.value)
            is UiAction.OnChangeName -> onChangeName(it.value)
            is UiAction.OnChangePassword -> onChangePassword(it.value)
            is UiAction.OnChangePostCode -> onChangePostCode(it.value)
            is UiAction.OnSelectCity -> onChangeCity(it.value)
            is UiAction.OnSelectCountry -> onChangeCountry(it.value)
            is UiAction.OnSelectState -> onChangeState(it.value)
            is UiAction.SelectBirthDate -> onChangeBirthDate(it.value)
            is UiAction.SelectGender -> onChangeGender(it.value)
            is UiAction.SelectInterestedIn -> onChangeInterest(it.value)
            is UiAction.CheckPrivacyPolicy -> onAgreedPolicy(it.value)
            is UiAction.ShowDatePicker -> onShowDatePicker(it.isVisible)
        }
    }

    init {
        validation()
    }

    private fun fetchCountries() {
        execute {
            fetchCountryApiUseCase.execute().collect { result ->
                when (result) {
                    is ApiResult.Error -> _uiEvent.send(UiEvent.ShowToastMessage(result.message))
                    is ApiResult.Loading -> setLoading(result.loading)
                    is ApiResult.Success -> {}
                }
            }
        }
    }

    private fun fetchStates() {
        execute {
            val current = _formUiState.value
            val selectedCountry = current.form.country?.value ?: ""
            fetchStateApiUseCase.execute(selectedCountry).collect { result ->
                when (result) {
                    is ApiResult.Error -> _uiEvent.send(UiEvent.ShowToastMessage(result.message))
                    is ApiResult.Loading -> setLoading(result.loading)
                    is ApiResult.Success -> {}
                }
            }
        }
    }

    private fun fetchCities() {
        execute {
            val current = _formUiState.value
            val selectedCountry = current.form.country?.value ?: ""
            val selectedState = current.form.state?.value ?: ""

            fetchCityApiUseCase.execute(
                FetchCityApiUseCase.Params(
                    selectedCountry,
                    selectedState
                )
            )
                .collect { result ->
                    when (result) {
                        is ApiResult.Error -> _uiEvent.send(UiEvent.ShowToastMessage(result.message))
                        is ApiResult.Loading -> setLoading(result.loading)
                        is ApiResult.Success -> {}
                    }
                }
        }
    }

    private fun performRegistration() {
        execute {
            val current = _formUiState.value

            val params = PostRegistrationApiUseCase.Params(
                username = current.form.username.value,
                password = current.form.password.value,
                email = current.form.email.value,
                name = current.form.name.value,
                zipCode = current.form.postCode.value,
                gender = current.form.gender.value,
                interestedIn = current.form.interestedIn.value,
                birthdate = current.form.dateOfBirth.value,
                birthdate2 = current.form.dateOfBirth.value,
                country = current.form.country?.value ?: "",
                state = current.form.state?.value ?: "",
                city = current.form.city?.value ?: "",
            )

            postRegistrationApiUseCase.execute(params).collect { result ->
                when (result) {
                    is ApiResult.Error -> _uiEvent.send(UiEvent.ShowToastMessage(result.message))
                    is ApiResult.Loading -> setLoading(result.loading)
                    is ApiResult.Success -> {}
                }
            }
        }
    }

    private fun onChangeUserName(value: String) = updateForm {
        it.copy(username = it.username.onChange(newValue = value))
    }

    private fun onChangeName(value: String) = updateForm {
        it.copy(name = it.name.onChange(newValue = value))
    }

    private fun onChangeEmail(value: String) = updateForm {
        it.copy(email = it.email.onChange(newValue = value))
    }

    private fun onChangePassword(value: String) = updateForm {
        it.copy(password = it.password.onChange(newValue = value))
    }

    private fun onChangePostCode(value: String) = updateForm {
        it.copy(postCode = it.postCode.onChange(newValue = value))
    }

    private fun onChangeCity(value: CityApiEntity) = updateForm { it.copy(city = value) }
    private fun onChangeState(value: StateApiEntity) = updateForm { it.copy(state = value) }
    private fun onChangeCountry(value: CountryApiEntity) = updateForm { it.copy(country = value) }

    private fun onChangeBirthDate(value: String) = updateForm {
        it.copy(dateOfBirth = it.dateOfBirth.onChange(newValue = value))
    }

    private fun onChangeGender(value: Gender) = updateForm { it.copy(gender = value) }
    private fun onChangeInterest(value: Gender) = updateForm { it.copy(interestedIn = value) }
    private fun onAgreedPolicy(value: Boolean) = updateForm { it.copy(isAgree = value) }

    private fun onShowDatePicker(value: Boolean) {
        execute {
            _formUiState.update { state ->
                state.copy(showDatePicker = value)
            }
        }
    }


    private fun setLoading(value: Boolean) {
        _formUiState.update { it.copy(isLoading = value) }
    }

    /** Update only the form part of the UiState in a single place to reduce repetition. */
    private inline fun updateForm(transform: (FormData) -> FormData) {
        _formUiState.update { state -> state.copy(form = transform(state.form)) }
    }

    private fun validation() {
        execute {
            ioError.collect { error ->
                when (error) {
                    RegistrationIoResult.InvalidName -> updateForm {
                        it.copy(
                            name = it.name.copy(
                                isValid = false
                            )
                        )
                    }

                    RegistrationIoResult.InvalidUsername -> updateForm {
                        it.copy(
                            username = it.username.copy(
                                isValid = false
                            )
                        )
                    }

                    RegistrationIoResult.InvalidEmail -> updateForm {
                        it.copy(
                            email = it.email.copy(
                                isValid = false
                            )
                        )
                    }

                    RegistrationIoResult.InvalidPassword -> updateForm {
                        it.copy(
                            password = it.password.copy(
                                isValid = false
                            )
                        )
                    }

                    RegistrationIoResult.InvalidGender -> _uiEvent.send(UiEvent.ShowToastMessage("Invalid Gender"))
                    RegistrationIoResult.InvalidInterested -> _uiEvent.send(
                        UiEvent.ShowToastMessage(
                            "Invalid interested in"
                        )
                    )

                    RegistrationIoResult.InvalidBirthDate -> updateForm {
                        it.copy(
                            dateOfBirth = it.dateOfBirth.copy(
                                isValid = false
                            )
                        )
                    }

                    RegistrationIoResult.InvalidCountry -> _uiEvent.send(UiEvent.ShowToastMessage("Invalid country"))
                    RegistrationIoResult.InvalidState -> _uiEvent.send(UiEvent.ShowToastMessage("Invalid state"))
                    RegistrationIoResult.InvalidCity -> _uiEvent.send(UiEvent.ShowToastMessage("Invalid city"))
                    RegistrationIoResult.InvalidPostCode -> updateForm {
                        it.copy(
                            postCode = it.postCode.copy(
                                isValid = false
                            )
                        )
                    }
                }
            }
        }
    }
}