package com.friend.splash

import com.friend.common.base.BaseViewModel
import com.friend.domain.apiusecase.profilemanager.FetchProfileApiUseCase
import com.friend.domain.base.ApiResult
import com.friend.sharedpref.SharedPrefHelper
import com.friend.sharedpref.SpKey
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import javax.inject.Inject

@HiltViewModel
class SplashViewModel @Inject constructor(
    private val sharedPrefHelper: SharedPrefHelper,
    private val fetchProfileApiUseCase: FetchProfileApiUseCase
) : BaseViewModel() {

    private val _uiState = MutableStateFlow<UiState>(UiState.Idle)
    val uiSate = _uiState

    private val _uiEffect = Channel<UiEffect>()
    val uiEffect = _uiEffect.receiveAsFlow()

    val action: (UiEvent) -> Unit = {
        when (it) {
            UiEvent.CheckLoginStatus -> checkLoginStatus()
            UiEvent.FetchProfile -> fetchProfile()
        }
    }

    private fun checkLoginStatus() {
        execute {
            val isLoggedIn = sharedPrefHelper.getBoolean(SpKey.loginStatus)
            if (isLoggedIn)
                fetchProfile()
            else _uiEffect.send(UiEffect.NavigateToLogin)
        }
    }

    private fun fetchProfile() {
        execute {
            fetchProfileApiUseCase.execute().collect { result ->
                when (result) {
                    is ApiResult.Error -> handleApiError(result.code, result.message)
                    is ApiResult.Loading -> _uiState.value = UiState.Idle
                    is ApiResult.Success -> {
                        if (result.data.isProfileComplete)
                            _uiEffect.send(UiEffect.NavigateToHome)
                        else _uiEffect.send(UiEffect.NavigateToProfileComplete)
                    }
                }
            }
        }
    }

    private fun handleApiError(statusCode: Int, message: String) {
        execute {
            if (statusCode == 401 || statusCode == 4001) {
                sharedPrefHelper.clearAllCache()
                _uiEffect.send(UiEffect.NavigateToLogin)
            } else _uiState.value = UiState.Error(message)
        }
    }
}