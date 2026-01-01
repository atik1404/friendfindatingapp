package com.friend.home

import com.friend.common.base.BaseViewModel
import com.friend.common.constant.AppConstants
import com.friend.domain.apiusecase.search.FetchFriendSuggestionApiUseCase
import com.friend.domain.base.ApiResult
import com.friend.sharedpref.SharedPrefHelper
import com.friend.sharedpref.SpKey
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    sharedPrefHelper: SharedPrefHelper,
    private val fetchFriendSuggestionApiUseCase: FetchFriendSuggestionApiUseCase,
) : BaseViewModel() {
    private val _fullName = MutableStateFlow("")
    val fullName: StateFlow<String> = _fullName.asStateFlow()

    private val _profilePicture = MutableStateFlow("")
    val profilePicture: StateFlow<String> = _profilePicture.asStateFlow()

    private val _uiState =
        MutableStateFlow(UiState())
    val uiState: StateFlow<UiState> = _uiState.asStateFlow()

    val action: (UiAction) -> Unit = {
        when (it) {
            UiAction.FetchFriendSuggestion -> fetchFriendSuggestions()
        }
    }

    init {
        _fullName.value = sharedPrefHelper.getString(SpKey.fullName)
        _profilePicture.value = sharedPrefHelper.getString(SpKey.profilePicture)
    }

    private fun fetchFriendSuggestions() {
        execute {
            val currentState = _uiState.value
            val filterBy = currentState.filterBy

            val params = FetchFriendSuggestionApiUseCase.Params(
                pageNo = _uiState.value.pageNo,
                gender = filterBy.gender,
                fromAge = filterBy.fromAge,
                toAge = filterBy.toAge,
                country = filterBy.country,
                state = filterBy.state,
                city = filterBy.city,
                username = filterBy.username,
                isOnlineUser = filterBy.isOnlineUser,
                bodyType = filterBy.bodyType,
                lookingFor = filterBy.lookingFor,
                eyes = filterBy.eyes,
                hair = filterBy.hair,
                smoking = filterBy.smoking,
                drinking = filterBy.drinking,
            )


            fetchFriendSuggestionApiUseCase.execute(params).collect { result ->
                when (result) {
                    is ApiResult.Error -> _uiState.value =
                        _uiState.value.copy(error = result.message)

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
}