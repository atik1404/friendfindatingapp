package com.friend.login

import com.friend.domain.base.TextInput

// UI state for the Login screen
data class UiState(
    val username: TextInput = TextInput(),
    val password: TextInput = TextInput(),
    val isLoading: Boolean = false,
    val isSubmitting: Boolean = false,
)

sealed class UiEvent {
    data class ShowMessage(val message: String) : UiEvent()
    object NavigateToHome : UiEvent()
}

sealed class UiAction {
    data class UsernameChanged(val value: String) : UiAction()
    data class PasswordChanged(val value: String) : UiAction()
    object PerformLogin : UiAction()
    object ResetState : UiAction()
}