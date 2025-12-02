package com.friend.splash

import com.friend.common.base.BaseViewModel
import com.friend.sharedpref.SharedPrefHelper
import com.friend.sharedpref.SpKey
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.receiveAsFlow
import javax.inject.Inject

@HiltViewModel
class SplashViewModel @Inject constructor(
    private val sharedPrefHelper: SharedPrefHelper,
) : BaseViewModel() {

    private val _uiEffect = Channel<UiEffect>()
    val uiEffect = _uiEffect.receiveAsFlow()

    val action: (UiEvent) -> Unit = {
        when (it) {
            UiEvent.CheckLoginStatus -> checkLoginStatus()
        }
    }

    private fun checkLoginStatus() {
        execute {
            delay(2000)
            val isLoggedIn = sharedPrefHelper.getBoolean(SpKey.loginStatus)

            if (isLoggedIn)
                _uiEffect.send(UiEffect.NavigateToHome)
            else _uiEffect.send(UiEffect.NavigateToLogin)
        }
    }
}