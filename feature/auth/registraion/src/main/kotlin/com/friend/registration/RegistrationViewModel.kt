package com.friend.registration

import com.friend.common.base.BaseViewModel
import com.friend.common.constant.Gender
import com.friend.common.extfun.getLocalIpAddress
import com.friend.domain.apiusecase.credential.PostLoginApiUseCase
import com.friend.domain.apiusecase.credential.PostRegistrationApiUseCase
import com.friend.domain.apiusecase.search.FetchCityApiUseCase
import com.friend.domain.apiusecase.search.FetchCountriesUseCase
import com.friend.domain.apiusecase.search.FetchStateApiUseCase
import com.friend.domain.base.ApiResult
import com.friend.domain.validator.RegistrationIoResult
import com.friend.entity.search.CityApiEntity
import com.friend.entity.search.CountryApiEntity
import com.friend.entity.search.StateApiEntity
import com.friend.sharedpref.SharedPrefHelper
import com.friend.sharedpref.SpKey
import com.friend.ui.common.UiText
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject
import com.friend.designsystem.R as Res

@HiltViewModel
class RegistrationViewModel @Inject constructor(
    private val fetchCityApiUseCase: FetchCityApiUseCase,
    private val fetchStateApiUseCase: FetchStateApiUseCase,
    private val fetchCountryApiUseCase: FetchCountriesUseCase,
    private val postRegistrationApiUseCase: PostRegistrationApiUseCase,
    private val postLoginApiUseCase: PostLoginApiUseCase,
    private val sharedPrefHelper: SharedPrefHelper
) : BaseViewModel() {
    val ioError get() = postRegistrationApiUseCase.ioError.receiveAsFlow()

    private val _formUiState = MutableStateFlow(UiState())
    val uiState: StateFlow<UiState> = _formUiState.asStateFlow()

    private val _uiEvent = Channel<UiEvent>()
    val uiEvent = _uiEvent.receiveAsFlow()

    val action: (UiAction) -> Unit = {
        when (it) {
            UiAction.FetchCountry -> fetchCountries()
            UiAction.PerformRegistration -> performRegistration()
            UiAction.PerformLogin -> performLoginApi()
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
            UiAction.ResetState -> _formUiState.value = UiState()
        }
    }

    init {
        bindIoError()
    }

    private fun fetchCountries() {
        execute {
            fetchCountryApiUseCase.execute().collect { result ->
                when (result) {
                    is ApiResult.Error -> setToastMessage(UiText.Dynamic(result.message))
                    is ApiResult.Loading -> setLoading(result.loading)
                    is ApiResult.Success -> {
                        _formUiState.update {
                            it.copy(countries = result.data)
                        }
                    }
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
                    is ApiResult.Error -> setToastMessage(UiText.Dynamic(result.message))
                    is ApiResult.Loading -> setLoading(result.loading)
                    is ApiResult.Success -> {
                        _formUiState.update {
                            it.copy(states = result.data)
                        }
                    }
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
                        is ApiResult.Error -> setToastMessage(UiText.Dynamic(result.message))
                        is ApiResult.Loading -> setLoading(result.loading)
                        is ApiResult.Success -> {
                            _formUiState.update {
                                it.copy(cities = result.data)
                            }
                        }
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
                gender = current.form.gender?.value ?: -1,
                interestedIn = current.form.interestedIn?.value ?: -1,
                birthdate = current.form.dateOfBirth.value,
                country = current.form.country?.value ?: "-1",
                state = current.form.state?.value ?: "-1",
                city = current.form.city?.value ?: "-1",
                userIP = getLocalIpAddress()
            )

            postRegistrationApiUseCase.execute(params).collect { result ->
                when (result) {
                    is ApiResult.Error -> setToastMessage(UiText.Dynamic(result.message))
                    is ApiResult.Loading -> _formUiState.update { it.copy(isSubmitting = result.loading) }
                    is ApiResult.Success -> {
                        performLoginApi()
                        setToastMessage(UiText.Dynamic(result.data))
                    }
                }
            }
        }
    }

    private fun performLoginApi() {
        val current = _formUiState.value
        val params = PostLoginApiUseCase.Params(
            username = current.form.username.value,
            password = current.form.password.value,
        )

        execute {
            postLoginApiUseCase.execute(params).collect { result ->
                when (result) {
                    is ApiResult.Success -> {
                        cacheUserData()
                        _uiEvent.send(UiEvent.NavigateToProfileCompletion)
                    }

                    is ApiResult.Loading -> setLoading(result.loading)
                    is ApiResult.Error -> {
                        _formUiState.update {
                            it.copy(
                                isLoginFailed = true,
                                loginFailedMessage = result.message
                            )
                        }
                    }
                }
            }
        }
    }

    private fun cacheUserData() {
        val current = _formUiState.value
        with(sharedPrefHelper) {
            putString(SpKey.userName, current.form.username.value)
            putString(SpKey.fullName, current.form.name.value)
            putString(SpKey.email, current.form.email.value)
            putString(SpKey.zipCode, current.form.postCode.value)
            putString(SpKey.gender, current.form.gender?.name ?: "")
            putString(SpKey.interestedIn, current.form.interestedIn?.name ?: "")
            putString(SpKey.dateOfBirth, current.form.dateOfBirth.value)
            putString(SpKey.country, current.form.country?.value ?: "")
            putString(SpKey.state, current.form.state?.value ?: "")
            putString(SpKey.city, current.form.city?.value ?: "")
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
    private fun onChangeState(value: StateApiEntity) {
        updateForm { it.copy(state = value, city = null) }
        fetchCities()
    }

    private fun onChangeCountry(value: CountryApiEntity) {
        updateForm { it.copy(country = value, state = null, city = null) }
        fetchStates()
    }

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
        _formUiState.update {
            it.copy(
                isLoading = value,
                isLoginFailed = false,
                loginFailedMessage = ""
            )
        }
    }

    /** Update only the form part of the UiState in a single place to reduce repetition. */
    private inline fun updateForm(transform: (FormData) -> FormData) {
        _formUiState.update { state -> state.copy(form = transform(state.form)) }
    }

    private fun bindIoError() {
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

                    RegistrationIoResult.InvalidGender -> setToastMessage(UiText.StringRes(Res.string.error_invalid_gender))
                    RegistrationIoResult.InvalidInterested -> setToastMessage(UiText.StringRes(Res.string.error_invalid_interest_in))
                    RegistrationIoResult.InvalidBirthDate -> setToastMessage(UiText.StringRes(Res.string.error_invalid_birth_date))
                    RegistrationIoResult.InvalidCountry -> setToastMessage(UiText.StringRes(Res.string.error_invalid_country))
                    RegistrationIoResult.InvalidState -> setToastMessage(UiText.StringRes(Res.string.error_invalid_state))
                    RegistrationIoResult.InvalidCity -> setToastMessage(UiText.StringRes(Res.string.error_invalid_city))
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

    private fun setToastMessage(message: UiText) {
        execute {
            _uiEvent.send(UiEvent.ShowToastMessage(message))
        }
    }
}