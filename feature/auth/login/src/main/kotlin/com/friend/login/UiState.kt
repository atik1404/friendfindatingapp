package com.friend.login

// UI state for the Login screen
data class UiState(
    val username: String = "",
    val password: String = "",
    val isUsernameValid: Boolean = true,
    val isPasswordValid: Boolean = true,
    val isLoading: Boolean = false,
)

sealed class UiEvent {
    data class ShowMessage(val message: String) : UiEvent()
    object NavigateToHome : UiEvent()
}

// UI events from the Composable to ViewModel
sealed class UiAction {
    data class UsernameChanged(val value: String) : UiAction()
    data class PasswordChanged(val value: String) : UiAction()
    object FormValidator : UiAction()
}