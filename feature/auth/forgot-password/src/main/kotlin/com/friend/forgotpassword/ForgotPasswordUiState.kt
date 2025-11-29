package com.friend.forgotpassword

data class UiState(
    val email: String = "",
    val isEmailValid : Boolean = true,
    val isLoading: Boolean = false,
)

sealed class UiEffect {
    data class ShowMessage(val message: String) : UiEffect()
    object BackToPreviousScreen : UiEffect()
}

sealed class UiEvent {
    data class EmailChanged(val value: String) : UiEvent()
    object FormValidator : UiEvent()
}