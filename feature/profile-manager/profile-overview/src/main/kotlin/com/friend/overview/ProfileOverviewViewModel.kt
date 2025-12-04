package com.friend.overview

import com.friend.common.base.BaseViewModel
import com.friend.domain.apiusecase.credential.PostLogoutApiUseCase
import com.friend.domain.base.ApiResult
import com.friend.sharedpref.SharedPrefHelper
import com.friend.sharedpref.SpKey
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject

@HiltViewModel
class ProfileOverviewViewModel @Inject constructor(
    private val logoutApiUseCase: PostLogoutApiUseCase,
    private val sharedPrefHelper: SharedPrefHelper
) : BaseViewModel() {
    private val _uiState = MutableStateFlow<UiState>(UiState.Loading(false))
    val uiState: StateFlow<UiState> = _uiState.asStateFlow()

    private val _userInfo = MutableStateFlow(UserInfo())
    val userInfo: StateFlow<UserInfo> = _userInfo.asStateFlow()

    private val _uiEvent = Channel<UiEvent>()
    val uiEvent = _uiEvent.receiveAsFlow()

    val action: (UiAction) -> Unit = {
        when (it) {
            UiAction.PerformLogout -> performLogout()
        }
    }

    init {
        _userInfo.update {
            UserInfo(
                username = sharedPrefHelper.getString(SpKey.fullName),
                email = sharedPrefHelper.getString(SpKey.email),
                image = sharedPrefHelper.getString(SpKey.profilePicture)
            )
        }
    }

    private fun performLogout() {
        execute {
            logoutApiUseCase.execute().collect { result ->
                when (result) {
                    is ApiResult.Error -> _uiEvent.send(UiEvent.ShowMessage(result.message))
                    is ApiResult.Loading -> _uiState.value = UiState.Loading(result.loading)
                    is ApiResult.Success -> {
                        _uiEvent.send(UiEvent.NavigateToLoginScreen)
                        sharedPrefHelper.clearAllCache()
                    }
                }
            }
        }
    }
}