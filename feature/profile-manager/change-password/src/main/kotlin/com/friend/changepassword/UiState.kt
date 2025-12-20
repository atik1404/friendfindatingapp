package com.friend.changepassword

import com.friend.domain.base.TextInput
import com.friend.ui.common.UiText

data class UiState(
    val oldPassword: TextInput = TextInput(),
    val newPassword: TextInput = TextInput(),
    val confirmPassword: TextInput = TextInput(),
    val isFormSubmitting: Boolean = false
)

sealed interface UiEvent {
    data class ShowToastMessage(val message: UiText) : UiEvent
    object BackToPreviousScreen : UiEvent
}

sealed interface UiAction {
    data class OnOldPasswordChange(val value: String) : UiAction
    data class OnNewPasswordChange(val value: String) : UiAction
    data class OnConfirmPasswordChange(val value: String) : UiAction
    object PerformPasswordChanged : UiAction
    object ResetState : UiAction
}