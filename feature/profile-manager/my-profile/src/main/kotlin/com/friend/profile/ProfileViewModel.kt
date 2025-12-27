package com.friend.profile

import com.friend.common.base.BaseViewModel
import com.friend.domain.apiusecase.profilemanager.FetchProfileApiUseCase
import com.friend.domain.apiusecase.profilemanager.PostProfileImageApiUseCase
import com.friend.domain.base.ApiResult
import com.friend.sharedpref.SharedPrefHelper
import com.friend.sharedpref.SpKey
import com.friend.ui.common.UiText
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import java.io.File
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val fetchProfileApiUseCase: FetchProfileApiUseCase,
    private val postProfileImageApiUseCase: PostProfileImageApiUseCase,
    private val sharedPrefHelper: SharedPrefHelper,
) : BaseViewModel() {
    private val _uiSate = MutableStateFlow<UiState>(UiState.Loading)
    val uiState = _uiSate

    private val _imageUiSate = MutableStateFlow(ImageUiState())
    val imageUiSate = _imageUiSate.asStateFlow()

    private val _uiEvent = Channel<UiEvent>()
    val uiEvent = _uiEvent.receiveAsFlow()

    val action: (UiAction) -> Unit = {
        when (it) {
            UiAction.FetchProfile -> fetchProfile()
            is UiAction.UpdateProfilePicture -> performProfileImageUpdate(it.image)
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

    private fun performProfileImageUpdate(image: File) {
        execute {
            val params = PostProfileImageApiUseCase.Params(
                username = sharedPrefHelper.getString(SpKey.userName),
                name = sharedPrefHelper.getString(SpKey.fullName),
                image = image
            )
            postProfileImageApiUseCase.execute(params).collect { result ->
                when (result) {
                    is ApiResult.Error -> _imageUiSate.update { it.copy(error = result.message) }
                    is ApiResult.Loading -> _imageUiSate.update { it.copy(isLoading = result.loading) }
                    is ApiResult.Success -> {
                        _uiEvent.send(UiEvent.ShowToastMessage(UiText.Dynamic(result.data)))
                        _imageUiSate.update { it.copy(error = null) }
                        fetchProfile()
                    }
                }
            }
        }
    }
}