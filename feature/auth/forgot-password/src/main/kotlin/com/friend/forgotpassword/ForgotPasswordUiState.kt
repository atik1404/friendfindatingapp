package com.friend.forgotpassword

import com.friend.domain.base.TextInput

data class UiState(
    val email: TextInput = TextInput(),
    val isLoading: Boolean = false,
)

sealed class UiEvent {
    data class ShowMessage(val message: String) : UiEvent()
    object BackToPreviousScreen : UiEvent()
}

sealed class UiAction {
    data class EmailChanged(val value: String) : UiAction()
    object SendLinkToEmail : UiAction()
    object ResetState : UiAction()
}