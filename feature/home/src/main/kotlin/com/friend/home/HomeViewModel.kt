package com.friend.home

import com.friend.common.base.BaseViewModel
import com.friend.common.constant.AppConstants
import com.friend.common.constant.Gender
import com.friend.domain.apiusecase.search.FetchFriendSuggestionApiUseCase
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
) : BaseViewModel() {
    private val _fullName = MutableStateFlow("")
    val fullName: StateFlow<String> = _fullName.asStateFlow()

    private val _profilePicture = MutableStateFlow("")
    val profilePicture: StateFlow<String> = _profilePicture.asStateFlow()

    private val _uiState =
        MutableStateFlow(UiState())
    val uiState: StateFlow<UiState> = _uiState.asStateFlow()

    private val _filterUiState =
        MutableStateFlow(FilterUiState())
    val filterUiState: StateFlow<FilterUiState> = _filterUiState.asStateFlow()

    val action: (UiAction) -> Unit = {
        when (it) {
            UiAction.FetchFriendSuggestion -> fetchFriendSuggestions()
            is UiAction.BodyTypeChanged -> onChangeBodyType(it.value)
            is UiAction.DrinkingChanged -> onChangeDrinking(it.value)
            is UiAction.EyesChanged -> onChangeEyes(it.value)
            is UiAction.HairChanged -> onChangeHair(it.value)
            is UiAction.HeightChanged -> onChangeHeight(it.value)
            is UiAction.OnChangeFromAge -> onChangeFromAge(it.value)
            is UiAction.OnChangeGender -> onChangeGender(it.value)
            is UiAction.OnChangeInterested -> onChangeInterested(it.value)
            is UiAction.OnChangeToAge -> onChangeToAge(it.value)
            is UiAction.OnChangeUsername -> onChangeUserName(it.value)
            is UiAction.SmokingChanged -> onChangeSmoking(it.value)
            is UiAction.WeightChanged -> onChangeWeight(it.value)
            is UiAction.OnlineUserChanged -> onChangeOnlineUser(it.value)
            is UiAction.PhotoRequiredChanged -> onChangePhotoRequired(it.value)
            is UiAction.LookingForChanged -> onChangeLookingFor(it.value)
            UiAction.SetCurrentUserInfo -> setCurrentUserInfo()
            UiAction.ResetFilter -> onResetFilter()
            UiAction.OnFilterApply -> onFilterApply()
        }
    }

    private fun setCurrentUserInfo() {
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
                state = filterBy.state,
                city = filterBy.city,
                username = filterBy.username,
                isOnlineUser = filterBy.isOnlineUser,
                isPhotoRequired = filterBy.isPhotoRequired,
                bodyType = filterBy.bodyType,
                lookingFor = filterBy.lookingFor,
                eyes = filterBy.eyes,
                hair = filterBy.hair,
                smoking = filterBy.smoking,
                drinking = filterBy.drinking,
            )


            fetchFriendSuggestionApiUseCase.execute(params).collect { result ->
                when (result) {
                    is ApiResult.Error -> {
                        if (currentState.data.isEmpty())
                            _uiState.value = _uiState.value.copy(error = result.message)
                    }

                    is ApiResult.Loading -> {
                        if (currentState.pageNo == 1)
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

    private fun onChangeUserName(value: String) = updateForm { it.copy(username = value) }

    private fun onChangeFromAge(value: String) = updateForm { it.copy(fromAge = value) }

    private fun onChangeToAge(value: String) = updateForm { it.copy(toAge = value) }

    private fun onChangeHeight(value: String) = updateForm { it.copy(height = value) }

    private fun onChangeWeight(value: String) = updateForm { it.copy(weight = value) }

    private fun onChangeLookingFor(value: String) = updateForm { it.copy(lookingFor = value) }
    private fun onChangeBodyType(value: String) = updateForm { it.copy(bodyType = value) }

    private fun onChangeEyes(value: String) = updateForm { it.copy(eyes = value) }

    private fun onChangeHair(value: String) = updateForm { it.copy(hair = value) }

    private fun onChangeSmoking(value: String) = updateForm { it.copy(smoking = value) }

    private fun onChangeDrinking(value: String) = updateForm { it.copy(drinking = value) }

    private fun onChangePhotoRequired(value: Boolean) =
        updateForm { it.copy(isPhotoRequired = value) }

    private fun onChangeOnlineUser(value: Boolean) = updateForm { it.copy(isOnlineUser = value) }

    private fun onChangeGender(value: Gender) = updateForm { it.copy(gender = value.value) }

    private fun onResetFilter() {
        execute {
            _filterUiState.value = FilterUiState()
        }
    }

    private fun onFilterApply() {
        execute {
            _uiState.value = _uiState.value.copy(
                pageNo = 1,
                hasMorePage = true,
                data = emptyList()
            )
            fetchFriendSuggestions()
        }
    }

    private fun onChangeInterested(value: Gender) =
        updateForm { it.copy(interestedIn = value.value) }

    /** Update only the form part of the UiState in a single place to reduce repetition. */
    private inline fun updateForm(transform: (FilterUiState) -> FilterUiState) {
        _filterUiState.update { state -> transform(state) }
    }
}