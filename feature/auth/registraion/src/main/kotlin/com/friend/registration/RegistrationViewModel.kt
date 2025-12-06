package com.friend.registration

import com.friend.common.base.BaseViewModel
import com.friend.common.constant.Gender
import com.friend.domain.apiusecase.credential.PostRegistrationApiUseCase
import com.friend.domain.apiusecase.search.FetchCityApiUseCase
import com.friend.domain.apiusecase.search.FetchCountriesUseCase
import com.friend.domain.apiusecase.search.FetchStateApiUseCase
import com.friend.domain.base.ApiResult
import com.friend.entity.search.CityApiEntity
import com.friend.entity.search.CountryApiEntity
import com.friend.entity.search.StateApiEntity
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject

@HiltViewModel
class RegistrationViewModel @Inject constructor(
    private val fetchCityApiUseCase: FetchCityApiUseCase,
    private val fetchStateApiUseCase: FetchStateApiUseCase,
    private val fetchCountryApiUseCase: FetchCountriesUseCase,
    private val postRegistrationApiUseCase: PostRegistrationApiUseCase,
) : BaseViewModel() {

    private val _formUiState = MutableStateFlow(FormData())
    val uiState = _formUiState.asStateFlow()

    private val _countries = MutableStateFlow(listOf<CountryApiEntity>())
    val countries = _countries.asStateFlow()

    private val _states = MutableStateFlow(listOf<StateApiEntity>())
    val states = _states.asStateFlow()

    private val _cities = MutableStateFlow(listOf<CityApiEntity>())
    val cities = _cities.asStateFlow()

    private val _uiEvent = Channel<UiEvent>()
    val uiEvent = _uiEvent.receiveAsFlow()

    val action: (UiAction) -> Unit = {
        when (it) {
            is UiAction.CheckPrivacyPolicy -> onAgreedPolicy(it.value)
            is UiAction.FetchCity -> fetchCities()
            UiAction.FetchCountry -> fetchCountries()
            is UiAction.FetchState -> fetchStates()
            UiAction.FormValidation -> performRegistration()
            is UiAction.OnChangeEmail -> onChangeEmail(it.value)
            is UiAction.OnChangeName -> onChangeName(it.value)
            is UiAction.OnChangePassword -> onChangePassword(it.value)
            is UiAction.OnChangePostCode -> onChangePostCode(it.value)
            is UiAction.OnChangeUserName -> onChangeUserName(it.value)
            is UiAction.OnSelectCity -> onChangeCity(it.value)
            is UiAction.OnSelectCountry -> onChangeCountry(it.value)
            is UiAction.OnSelectState -> onChangeState(it.value)
            is UiAction.SelectBirthDate -> onChangeBirthDate(it.value)
            is UiAction.SelectGender -> onChangeGender(it.value)
            is UiAction.SelectInterestedIn -> onChangeInterest(it.value)
        }
    }

    private fun fetchCountries() {
        execute {
            fetchCountryApiUseCase.execute().collect { result ->
                when (result) {
                    is ApiResult.Error -> _uiEvent.send(UiEvent.ShowToastMessage(result.message))
                    is ApiResult.Loading -> isLoading(result.loading)
                    is ApiResult.Success -> _countries.value = result.data
                }
            }
        }
    }

    private fun fetchStates() {
        execute {
            val current = _formUiState.value
            fetchStateApiUseCase.execute(current.country).collect { result ->
                when (result) {
                    is ApiResult.Error -> _uiEvent.send(UiEvent.ShowToastMessage(result.message))
                    is ApiResult.Loading -> isLoading(result.loading)
                    is ApiResult.Success -> _states.value = result.data
                }
            }
        }
    }

    private fun fetchCities() {
        execute {
            val current = _formUiState.value
            fetchCityApiUseCase.execute(FetchCityApiUseCase.Params(current.country, current.state))
                .collect { result ->
                    when (result) {
                        is ApiResult.Error -> _uiEvent.send(UiEvent.ShowToastMessage(result.message))
                        is ApiResult.Loading -> isLoading(result.loading)
                        is ApiResult.Success -> _cities.value = result.data
                    }
                }
        }
    }

    private fun performRegistration() {
        execute {
            val current = _formUiState.value
            val params = PostRegistrationApiUseCase.Params(
                username = current.username,
                password = current.password,
                email = current.email,
                name = current.name,
                gender = Gender.toValue(current.gender),
                interestedIn = Gender.toValue(current.interestedIn),
                birthdate = current.dateOfBirth,
                birthdate2 = current.dateOfBirth,
                country = current.country,
                state = current.state,
                zipCode = current.postCode,
                city = current.city,
            )

            postRegistrationApiUseCase.execute(params).collect { result ->
                when (result) {
                    is ApiResult.Error -> _uiEvent.send(UiEvent.ShowToastMessage(result.message))
                    is ApiResult.Loading -> isLoading(result.loading)
                    is ApiResult.Success -> {}
                }
            }
        }
    }

    private fun onChangeUserName(value: String) {
        _formUiState.update {
            it.copy(username = value)
        }
    }

    private fun onChangeName(value: String) {
        _formUiState.update {
            it.copy(name = value)
        }
    }

    private fun onChangeEmail(value: String) {
        _formUiState.update {
            it.copy(email = value)
        }
    }

    private fun onChangePassword(value: String) {
        _formUiState.update {
            it.copy(password = value)
        }
    }

    private fun onChangePostCode(value: String) {
        _formUiState.update {
            it.copy(postCode = value)
        }
    }

    private fun onChangeCity(value: String) {
        _formUiState.update {
            it.copy(city = value)
        }
    }

    private fun onChangeState(value: String) {
        _formUiState.update {
            it.copy(state = value)
        }
    }

    private fun onChangeCountry(value: String) {
        _formUiState.update {
            it.copy(country = value)
        }
    }

    private fun onChangeBirthDate(value: String) {
        _formUiState.update {
            it.copy(dateOfBirth = value)
        }
    }

    private fun onChangeGender(value: String) {
        _formUiState.update {
            it.copy(gender = value)
        }
    }

    private fun onChangeInterest(value: String) {
        _formUiState.update {
            it.copy(interestedIn = value)
        }
    }

    private fun onAgreedPolicy(value: Boolean) {
        _formUiState.update {
            it.copy(isAgree = value)
        }
    }

    private fun isLoading(value: Boolean) {
        _formUiState.update {
            it.copy(isLoading = value)
        }
    }
}