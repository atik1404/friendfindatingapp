package com.friend.reportabuse

import com.friend.domain.base.TextInput
import com.friend.ui.common.UiText

data class UiState(
    val isSubmitting: Boolean = false,
    val description: TextInput = TextInput()
)

sealed interface UiEvent {
    data class ShowToastMessage(val uiText: UiText) : UiEvent
    data object FinishScreen : UiEvent
}

sealed interface UiAction {
    data class SubmitReport(val username: String) : UiAction
    data class OnTextChange(val value: String) : UiAction
}