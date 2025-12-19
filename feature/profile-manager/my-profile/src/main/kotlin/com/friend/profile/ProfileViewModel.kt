package com.friend.profile

import com.friend.common.base.BaseViewModel
import com.friend.domain.apiusecase.profilemanager.FetchProfileApiUseCase
import com.friend.domain.base.ApiResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val fetchProfileApiUseCase: FetchProfileApiUseCase
) : BaseViewModel() {
    private val _uiSate = MutableStateFlow<UiState>(UiState.Loading)
    val uiState = _uiSate

    val action: (UiAction) -> Unit = {
        when (it) {
            UiAction.FetchProfile -> fetchProfile()
            UiAction.UpdateProfilePicture -> {}
        }
    }

    private fun fetchProfile() {
        execute {
            fetchProfileApiUseCase.execute().collect { result ->
                when (result) {
                    is ApiResult.Error -> _uiSate.value = UiState.ApiError(result.message)
                    is ApiResult.Loading -> _uiSate.value = UiState.Loading
                    is ApiResult.Success -> _uiSate.value = UiState.ShowProfileData(result.data)
                }
            }
        }
    }
}