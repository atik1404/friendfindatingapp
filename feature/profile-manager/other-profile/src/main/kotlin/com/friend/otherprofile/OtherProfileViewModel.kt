package com.friend.otherprofile

import com.friend.common.base.BaseViewModel
import com.friend.domain.apiusecase.profilemanager.FetchOtherProfileApiUseCase
import com.friend.domain.base.ApiResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import javax.inject.Inject

@HiltViewModel
class OtherProfileViewModel @Inject constructor(
    private val fetchOtherProfileApiUseCase: FetchOtherProfileApiUseCase
) : BaseViewModel() {
    private val _uiSate = MutableStateFlow<UiState>(UiState.Loading)
    val uiState = _uiSate

    val action: (UiAction) -> Unit = {
        when (it) {
            is UiAction.FetchProfile -> fetchProfile(it.username)
            UiAction.UpdateProfilePicture -> {}
        }
    }

    private fun fetchProfile(username: String) {
        execute {
            fetchOtherProfileApiUseCase.execute(username).collect { result ->
                when (result) {
                    is ApiResult.Error -> _uiSate.value = UiState.ApiError(result.message)
                    is ApiResult.Loading -> _uiSate.value = UiState.Loading
                    is ApiResult.Success -> _uiSate.value = UiState.ShowProfileData(result.data)
                }
            }
        }
    }
}