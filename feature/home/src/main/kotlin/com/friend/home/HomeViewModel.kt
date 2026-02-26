package com.friend.home

import com.friend.common.base.BaseViewModel
import com.friend.common.constant.AppConstants
import com.friend.domain.apiusecase.auth.UpdateFcmTokenApiUseCase
import com.friend.domain.apiusecase.profilemanager.UpdateOnlineStatusApiUseCase
import com.friend.domain.apiusecase.search.FetchCityApiUseCase
import com.friend.domain.apiusecase.search.FetchCountriesUseCase
import com.friend.domain.apiusecase.search.FetchFriendSuggestionApiUseCase
import com.friend.domain.apiusecase.search.FetchStateApiUseCase
import com.friend.domain.base.ApiResult
import com.friend.sharedpref.SharedPrefHelper
import com.friend.sharedpref.SpKey
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val sharedPrefHelper: SharedPrefHelper,
    private val fetchFriendSuggestionApiUseCase: FetchFriendSuggestionApiUseCase,
    private val updateFcmTokenApiUseCase: UpdateFcmTokenApiUseCase,
    private val fetchCityApiUseCase: FetchCityApiUseCase,
    private val fetchStateApiUseCase: FetchStateApiUseCase,
    private val fetchCountryApiUseCase: FetchCountriesUseCase,
    private val updateOnlineStatusApiUseCase: UpdateOnlineStatusApiUseCase,
) : BaseViewModel() {
    private val _fullName = MutableStateFlow("")
    val fullName: StateFlow<String> = _fullName.asStateFlow()

    private val _profilePicture = MutableStateFlow("")
    val profilePicture: StateFlow<String> = _profilePicture.asStateFlow()

    private val _uiState = MutableStateFlow(UiState())
    val uiState: StateFlow<UiState> = _uiState.asStateFlow()

    private val _filterUiState =
        MutableStateFlow(
            FilterUiState(
                country = sharedPrefHelper.getString(SpKey.country),
                state = sharedPrefHelper.getString(SpKey.state),
                city = sharedPrefHelper.getString(SpKey.city),
            )
        )
    val filterUiState: StateFlow<FilterUiState> = _filterUiState.asStateFlow()

    private val _location = MutableStateFlow(LocationState())
    val location: StateFlow<LocationState> = _location.asStateFlow()

    val action: (UiAction) -> Unit = {
        when (it) {
            UiAction.FetchFriendSuggestion -> fetchFriendSuggestions()
            UiAction.CurrentUserInfo -> currentUserInfo()
            UiAction.ResetFilter -> onResetFilter()
            is UiAction.OnFilterApply -> onFilterApply(it.value)
            is UiAction.FetchCity -> fetchCities(it.country, it.state)
            is UiAction.FetchState -> fetchStates(it.country)
        }
    }

    init {
        fetchCountries()
        updateFcmToken()
        fetchFriendSuggestions()
    }

    private fun updateFcmToken() {
        execute {
            updateFcmTokenApiUseCase.execute(sharedPrefHelper.getString(SpKey.fcmToken))
                .collect { result ->
                    when (result) {
                        is ApiResult.Error -> {}
                        is ApiResult.Loading -> {}
                        is ApiResult.Success -> {}
                    }
                }
        }
    }

    private fun currentUserInfo() {
        _fullName.value = sharedPrefHelper.getString(SpKey.fullName)
        _profilePicture.value = sharedPrefHelper.getString(SpKey.profilePicture)
    }

    private fun fetchFriendSuggestions() {
        execute {
            val currentState = _uiState.value
            val filterBy = _filterUiState.value

            val params = FetchFriendSuggestionApiUseCase.Params(
                pageNo = _uiState.value.pageNo,
                gender = filterBy.gender,
                interestedIn = filterBy.interestedIn,
                fromAge = filterBy.fromAge,
                toAge = filterBy.toAge,
                country = filterBy.country,
                state = if (filterBy.state == AppConstants.STATE_ALL) "" else filterBy.state,
                city = filterBy.city,
                username = filterBy.username.trim(),
                isOnlineUser = filterBy.isOnlineUser,
                isPhotoRequired = filterBy.isPhotoRequired,
                bodyType = filterBy.bodyType,
                lookingFor = filterBy.lookingFor,
                eyes = filterBy.eyes,
                hair = filterBy.hair,
                smoking = filterBy.smoking,
                drinking = filterBy.drinking,
                isSearch = filterBy.isSearchApply,
            )


            fetchFriendSuggestionApiUseCase.execute(params).collect { result ->
                when (result) {
                    is ApiResult.Error -> {
                        if (currentState.data.isEmpty())
                            _uiState.value = _uiState.value.copy(error = result.message)
                    }

                    is ApiResult.Loading -> {
                        if (currentState.pageNo == 0)
                            _uiState.value = _uiState.value.copy(
                                isLoading = result.loading,
                                isLoadingMore = false
                            )
                        else _uiState.value =
                            _uiState.value.copy(isLoadingMore = result.loading, isLoading = false)
                    }

                    is ApiResult.Success -> {
                        val currentItems = _uiState.value.data
                        val updatedItems = currentItems + result.data
                        val pageNo = currentState.pageNo + 1
                        val hasMorePage = result.data.size >= AppConstants.DATA_PER_PAGE

                        _uiState.value = _uiState.value.copy(
                            data = updatedItems,
                            pageNo = pageNo,
                            hasMorePage = hasMorePage
                        )
                    }
                }
            }
        }
    }

    private fun onFilterApply(filter: FilterUiState) {
        execute {
            _uiState.value = _uiState.value.copy(
                pageNo = 0,
                hasMorePage = true,
                data = emptyList()
            )
            _filterUiState.value = filter.copy(isSearchApply = true)
            fetchFriendSuggestions()
        }
    }

    private fun onResetFilter() {
        execute {
            _uiState.value = _uiState.value.copy(
                pageNo = 0,
                hasMorePage = true,
                data = emptyList()
            )

            _filterUiState.value = FilterUiState(
                isSearchApply = false,
                country = sharedPrefHelper.getString(SpKey.country),
                state = sharedPrefHelper.getString(SpKey.state),
                city = sharedPrefHelper.getString(SpKey.city),
            )
            fetchFriendSuggestions()
        }
    }

    private fun fetchCountries() {
        execute {
            fetchCountryApiUseCase.execute().collect { result ->
                when (result) {
                    is ApiResult.Error -> {}
                    is ApiResult.Loading -> {}
                    is ApiResult.Success -> {
                        _location.update {
                            it.copy(
                                countries = result.data,
                                states = emptyList(),
                                cities = emptyList()
                            )
                        }
                    }
                }
            }
        }
    }

    private fun fetchStates(selectedCountry: String) {
        execute {
            fetchStateApiUseCase.execute(selectedCountry).collect { result ->
                when (result) {
                    is ApiResult.Error -> {}
                    is ApiResult.Loading -> {}
                    is ApiResult.Success -> {
                        _location.update {
                            it.copy(states = result.data, cities = emptyList())
                        }
                    }
                }
            }
        }
    }

    private fun fetchCities(selectedCountry: String, selectedState: String) {
        execute {
            fetchCityApiUseCase.execute(
                FetchCityApiUseCase.Params(
                    selectedCountry,
                    if (selectedState == AppConstants.STATE_ALL) "" else selectedState
                )
            )
                .collect { result ->
                    when (result) {
                        is ApiResult.Error -> {}
                        is ApiResult.Loading -> {}
                        is ApiResult.Success -> {
                            _location.update {
                                it.copy(cities = result.data)
                            }
                        }
                    }
                }
        }
    }

    fun getUsername() = sharedPrefHelper.getString(SpKey.userName)

    fun updateOnlineStatus() {
        execute {
            updateOnlineStatusApiUseCase.execute().collect { }
        }
    }
}