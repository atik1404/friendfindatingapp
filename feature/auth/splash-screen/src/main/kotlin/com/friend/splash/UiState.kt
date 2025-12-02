package com.friend.splash

sealed class UiEffect {
    object NavigateToHome : UiEffect()
    object NavigateToLogin : UiEffect()
}

sealed class UiEvent {
    object CheckLoginStatus : UiEvent()
}