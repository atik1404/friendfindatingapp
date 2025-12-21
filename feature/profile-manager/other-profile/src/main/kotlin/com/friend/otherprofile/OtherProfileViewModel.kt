package com.friend.otherprofile

import androidx.compose.runtime.mutableStateOf
import com.friend.common.base.BaseViewModel
import com.friend.domain.apiusecase.profilemanager.FetchOtherProfileApiUseCase
import com.friend.domain.apiusecase.profilemanager.PostBlockUnblockApiUseCase
import com.friend.domain.base.ApiResult
import com.friend.sharedpref.SharedPrefHelper
import com.friend.sharedpref.SpKey
import com.friend.ui.common.UiText
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import javax.inject.Inject

@HiltViewModel
class OtherProfileViewModel @Inject constructor(
    private val fetchOtherProfileApiUseCase: FetchOtherProfileApiUseCase,
    private val postBlockUnblockApiUseCase: PostBlockUnblockApiUseCase,
    private val sharedPrefHelper: SharedPrefHelper
) : BaseViewModel() {
    private val _uiSate = MutableStateFlow<UiState>(UiState.Loading)
    val uiState = _uiSate

    private val _isBlocked = mutableStateOf(false)
    val isBlocked = _isBlocked

    private val _uiEvent = Channel<UiEvent>()
    val uiEvent = _uiEvent.receiveAsFlow()

    val action: (UiAction) -> Unit = {
        when (it) {
            is UiAction.FetchProfile -> fetchProfile(it.username)
            is UiAction.PerformBlockUnblock -> performBlockUnblock(it.username)
        }
    }

    private fun fetchProfile(username: String) {
        execute {
            fetchOtherProfileApiUseCase.execute(username).collect { result ->
                when (result) {
                    is ApiResult.Error -> _uiSate.value = UiState.ApiError(result.message)
                    is ApiResult.Loading -> _uiSate.value = UiState.Loading
                    is ApiResult.Success -> {
                        _isBlocked.value = result.data.isBlocked
                        _uiSate.value = UiState.ShowProfileData(result.data)
                    }
                }
            }
        }
    }

    private fun performBlockUnblock(blockedUser: String) {
        execute {
            val params = PostBlockUnblockApiUseCase.Params(
                userBlocker = sharedPrefHelper.getString(SpKey.userName),
                blockedUser = blockedUser
            )

            postBlockUnblockApiUseCase.execute(params).collect { result ->
                when (result) {
                    is ApiResult.Error -> _uiEvent.send(
                        UiEvent.ShowToastMessage(
                            UiText.Dynamic(
                                result.message
                            )
                        )
                    )

                    is ApiResult.Loading -> _uiSate.value = UiState.Loading
                    is ApiResult.Success -> fetchProfile(blockedUser)
                }
            }
        }
    }
}