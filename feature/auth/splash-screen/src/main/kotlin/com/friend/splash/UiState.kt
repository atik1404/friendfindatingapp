package com.friend.splash

sealed interface UiState {
    object Idle : UiState
    data class Error(val message: String) : UiState
}

sealed class UiEffect {
    object NavigateToHome : UiEffect()
    object NavigateToLogin : UiEffect()
    object NavigateToProfileComplete : UiEffect()
}

sealed class UiEvent {
    object CheckLoginStatus : UiEvent()
    object FetchProfile : UiEvent()
}