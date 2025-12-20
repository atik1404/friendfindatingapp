package com.friend.personalsetting

import com.friend.common.base.BaseViewModel
import com.friend.common.constant.Gender
import com.friend.common.dateparser.DateTimePatterns
import com.friend.common.dateparser.DateTimeUtils
import com.friend.domain.apiusecase.profilemanager.FetchProfileApiUseCase
import com.friend.domain.apiusecase.profilemanager.PostProfileUpdateApiUseCase
import com.friend.domain.apiusecase.search.FetchCityApiUseCase
import com.friend.domain.apiusecase.search.FetchCountriesUseCase
import com.friend.domain.apiusecase.search.FetchStateApiUseCase
import com.friend.domain.base.ApiResult
import com.friend.domain.validator.ProfileCompletionIoResult
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
class PersonalSettingViewmodel @Inject constructor(
    private val updateProfileUseCase: PostProfileUpdateApiUseCase,
    private val fetchCityApiUseCase: FetchCityApiUseCase,
    private val fetchStateApiUseCase: FetchStateApiUseCase,
    private val fetchCountryApiUseCase: FetchCountriesUseCase,
    private val fetchProfileApiUseCase: FetchProfileApiUseCase,
    private val sharedPrefHelper: SharedPrefHelper
) : BaseViewModel() {
    val ioError get() = updateProfileUseCase.ioError.receiveAsFlow()

    private val _formUiState = MutableStateFlow(UiState())
    val uiState: StateFlow<UiState> = _formUiState.asStateFlow()

    private val _uiEvent = Channel<UiEvent>()
    val uiEvent = _uiEvent.receiveAsFlow()

    val action: (UiAction) -> Unit = {
        when (it) {
            UiAction.FetchCountry -> fetchCountries()
            UiAction.PerformProfileUpdate -> performProfileUpdate()
            UiAction.SetDefaultData -> setDefaultValue()
            is UiAction.OnChangeEmail -> onChangeEmail(it.value)
            is UiAction.OnChangeName -> onChangeName(it.value)
            is UiAction.OnChangePassword -> onChangePassword(it.value)
            is UiAction.OnChangePostCode -> onChangePostCode(it.value)
            is UiAction.OnSelectCity -> onChangeCity(it.value)
            is UiAction.OnSelectCountry -> {
                onChangeCountry(it.value)
                fetchStates()
            }

            is UiAction.OnSelectState -> {
                onChangeState(it.value)
                fetchCities()
            }

            is UiAction.SelectBirthDate -> onChangeBirthDate(it.value)
            is UiAction.SelectGender -> onChangeGender(it.value)
            is UiAction.SelectInterestedIn -> onChangeInterest(it.value)
            is UiAction.ShowDatePicker -> onShowDatePicker(it.isVisible)
            UiAction.ResetState -> _formUiState.value = UiState()
        }
    }

    init {
        bindIoError()
    }

    private fun setDefaultValue() {
        onChangeName(sharedPrefHelper.getString(SpKey.fullName))
        onChangeEmail(sharedPrefHelper.getString(SpKey.email))
        onChangePostCode(sharedPrefHelper.getString(SpKey.zipCode))
        onChangeCountry(sharedPrefHelper.getString(SpKey.country))
        onChangeState(sharedPrefHelper.getString(SpKey.state))
        onChangeCity(sharedPrefHelper.getString(SpKey.city))
        onChangeBirthDate(
            DateTimeUtils.parseToPattern(
                sharedPrefHelper.getString(SpKey.dateOfBirth),
                DateTimePatterns.SQL_YMD
            )
        )
        onChangeGender(Gender.toEnum(sharedPrefHelper.getString(SpKey.gender)))
        onChangeInterest(Gender.toEnum(sharedPrefHelper.getString(SpKey.interestedIn)))

        fetchCountries()
        fetchStates()
        fetchCities()
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
            val selectedCountry = current.form.country ?: ""
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
            val selectedCountry = current.form.country ?: ""
            val selectedState = current.form.state ?: ""

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

    private fun performProfileUpdate() {
        execute {
            val current = _formUiState.value

            val params = PostProfileUpdateApiUseCase.Params(
                username = sharedPrefHelper.getString(SpKey.userName),
                email = current.form.email.value,
                name = current.form.name.value,
                zipCode = current.form.postCode.value,
                gender = current.form.gender?.value ?: -1,
                interestedIn = current.form.interestedIn?.value ?: -1,
                birthdate = current.form.dateOfBirth.value,
                country = current.form.country ?: "-1",
                state = current.form.state ?: "-1",
                city = current.form.city ?: "-1",
                height = sharedPrefHelper.getString(SpKey.height),
                weight = sharedPrefHelper.getString(SpKey.weight),
                eyes = sharedPrefHelper.getString(SpKey.eyes),
                hair = sharedPrefHelper.getString(SpKey.hair),
                smoking = sharedPrefHelper.getString(SpKey.smoking),
                drinking = sharedPrefHelper.getString(SpKey.drinking),
                bodyType = sharedPrefHelper.getString(SpKey.bodyType),
                lookingFor = sharedPrefHelper.getString(SpKey.lookingFor),
                aboutYou = sharedPrefHelper.getString(SpKey.aboutYou),
                title = sharedPrefHelper.getString(SpKey.title),
                whatsUp = sharedPrefHelper.getString(SpKey.whatsUp),
                interests = sharedPrefHelper.getString(SpKey.interests)
            )

            updateProfileUseCase.execute(params).collect { result ->
                when (result) {
                    is ApiResult.Error -> setToastMessage(UiText.Dynamic(result.message))
                    is ApiResult.Loading -> _formUiState.update { it.copy(isSubmitting = result.loading) }
                    is ApiResult.Success -> {
                        setToastMessage(UiText.Dynamic(result.data))
                        fetchProfile()
                    }
                }
            }
        }
    }

    private fun fetchProfile() {
        execute {
            fetchProfileApiUseCase.execute().collect { result ->
                when (result) {
                    is ApiResult.Error -> setToastMessage(UiText.Dynamic(result.message))
                    is ApiResult.Loading -> setLoading(result.loading)
                    is ApiResult.Success -> _uiEvent.send(UiEvent.FinishScreen)
                }
            }
        }
    }

    private fun onChangeName(value: String) {
        updateForm {
            it.copy(name = it.name.onChange(newValue = value))
        }
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

    private fun onChangeCity(value: String) = updateForm { it.copy(city = value) }

    private fun onChangeState(value: String) {
        updateForm { it.copy(state = value, city = null) }
    }

    private fun onChangeCountry(value: String) {
        updateForm { it.copy(country = value, state = null, city = null) }
    }

    private fun onChangeBirthDate(value: String) = updateForm {
        it.copy(dateOfBirth = it.dateOfBirth.onChange(newValue = value))
    }

    private fun onChangeGender(value: Gender) = updateForm { it.copy(gender = value) }

    private fun onChangeInterest(value: Gender) = updateForm { it.copy(interestedIn = value) }

    private fun onShowDatePicker(value: Boolean) {
        execute {
            _formUiState.update { state ->
                state.copy(showDatePicker = value)
            }
        }
    }

    /** Update only the form part of the UiState in a single place to reduce repetition. */
    private inline fun updateForm(transform: (FormData) -> FormData) {
        _formUiState.update { state -> state.copy(form = transform(state.form)) }
    }

    private fun setLoading(value: Boolean) {
        _formUiState.update {
            it.copy(
                isLoading = value,
            )
        }
    }

    private fun setToastMessage(message: UiText) {
        execute {
            _uiEvent.send(UiEvent.ShowToastMessage(message))
        }
    }

    private fun bindIoError() {
        execute {
            ioError.collect { error ->
                when (error) {
                    ProfileCompletionIoResult.InvalidName -> updateForm {
                        it.copy(
                            name = it.name.copy(
                                isValid = false
                            )
                        )
                    }

                    ProfileCompletionIoResult.InvalidEmail -> updateForm {
                        it.copy(
                            email = it.email.copy(
                                isValid = false
                            )
                        )
                    }

                    ProfileCompletionIoResult.InvalidGender -> setToastMessage(UiText.StringRes(Res.string.error_invalid_gender))
                    ProfileCompletionIoResult.InvalidInterestedIn -> setToastMessage(UiText.StringRes(Res.string.error_invalid_interest_in))
                    ProfileCompletionIoResult.InvalidBirthDate -> setToastMessage(UiText.StringRes(Res.string.error_invalid_birth_date))
                    ProfileCompletionIoResult.InvalidCountry -> setToastMessage(UiText.StringRes(Res.string.error_invalid_country))
                    ProfileCompletionIoResult.InvalidState -> setToastMessage(UiText.StringRes(Res.string.error_invalid_state))
                    ProfileCompletionIoResult.InvalidCity -> setToastMessage(UiText.StringRes(Res.string.error_invalid_city))
                    ProfileCompletionIoResult.InvalidPostCode -> updateForm {
                        it.copy(
                            postCode = it.postCode.copy(
                                isValid = false
                            )
                        )
                    }

                    else -> {}
                }
            }
        }
    }
}