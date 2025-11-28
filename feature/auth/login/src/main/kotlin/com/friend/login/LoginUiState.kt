package com.friend.login

// UI state for the Login screen
data class LoginUiState(
    val email: String = "",
    val password: String = "",
    val emailError: String? = null,
    val passwordError: String? = null,
    val isLoading: Boolean = false,
    val isLoginButtonEnabled: Boolean = true
)

// One-time UI effects (snackbar, navigation, etc.)
sealed class LoginUiEffect {
    data class ShowMessage(val message: String) : LoginUiEffect()
    object NavigateToHome : LoginUiEffect()
}

// UI events from the Composable to ViewModel
sealed class LoginUiEvent {
    data class EmailChanged(val value: String) : LoginUiEvent()
    data class PasswordChanged(val value: String) : LoginUiEvent()
    object Submit : LoginUiEvent()
}