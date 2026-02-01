package com.friend.overview

data class UserInfo(
    val fullname: String = "",
    val username: String = "",
    val email: String = "",
    val image: String = "",
    val isLoginByGoogle: Boolean = false
)

sealed interface UiState {
    data class Loading(val isLoading: Boolean) : UiState
}

sealed class UiEvent {
    data class ShowMessage(val message: String) : UiEvent()
    object NavigateToLoginScreen : UiEvent()
}

sealed class UiAction {
    object PerformLogout : UiAction()
    object PerformDeleteAccount : UiAction()
}