package com.friend.login

// UI state for the Login screen
data class LoginUiState(
    val username: String = "",
    val password: String = "",
    val isUsernameValid: Boolean = true,
    val isPasswordValid: Boolean = true,
    val isLoading: Boolean = false,
)

sealed class LoginUiEffect {
    data class ShowMessage(val message: String) : LoginUiEffect()
    object NavigateToHome : LoginUiEffect()
}

// UI events from the Composable to ViewModel
sealed class LoginUiEvent {
    data class UsernameChanged(val value: String) : LoginUiEvent()
    data class PasswordChanged(val value: String) : LoginUiEvent()
    object FormValidator : LoginUiEvent()
}