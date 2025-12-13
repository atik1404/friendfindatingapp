package com.friend.profilecompletion

import com.friend.common.base.BaseViewModel
import com.friend.ui.common.UiText
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject

@HiltViewModel
class ProfileCompletionViewModel @Inject constructor() : BaseViewModel() {
    //val ioError get() = postRegistrationApiUseCase.ioError.receiveAsFlow()

    private val _formUiState = MutableStateFlow(UiState())
    val uiState: StateFlow<UiState> = _formUiState.asStateFlow()

    private val _uiEvent = Channel<UiEvent>()
    val uiEvent = _uiEvent.receiveAsFlow()

    val action: (UiAction) -> Unit = {
        when (it) {
            UiAction.ResetState -> _formUiState.value = UiState()
            is UiAction.AboutYouChanged -> onAboutYouChange(it.value)
            is UiAction.BodyTypeChanged -> onBodyTypeChange(it.value)
            is UiAction.DrinkingChanged -> onDrinkingChange(it.value)
            is UiAction.EyesChanged -> onEyesChange(it.value)
            is UiAction.HairChanged -> onHeightChange(it.value)
            is UiAction.HeightChanged -> onHeightChange(it.value)
            is UiAction.InterestsChanged -> {}
            is UiAction.LookingForChanged -> onLookingForChange(it.value)
            is UiAction.SmokingChanged -> onSmokingChange(it.value)
            is UiAction.TitleChanged -> onTitleChange(it.value)
            is UiAction.WeightChanged -> onWeightChange(it.value)
        }
    }

    private fun onHeightChange(value: String) {
        updateForm { state ->
            state.copy(height = value)
        }
    }

    private fun onWeightChange(value: String) {
        updateForm { state ->
            state.copy(weight = value)
        }
    }

    private fun onEyesChange(value: String) {
        updateForm { state ->
            state.copy(eyes = value)
        }
    }

    private fun onHairChange(value: String) {
        updateForm { state ->
            state.copy(hair = value)
        }
    }

    private fun onSmokingChange(value: String) {
        updateForm { state ->
            state.copy(smoking = value)
        }
    }

    private fun onDrinkingChange(value: String) {
        updateForm { state ->
            state.copy(drinking = value)
        }
    }

    private fun onBodyTypeChange(value: String) {
        updateForm { state ->
            state.copy(bodyType = value)
        }
    }

    private fun onLookingForChange(value: String) {
        updateForm { state ->
            state.copy(lookingFor = value)
        }
    }

    private fun onTitleChange(value: String) {
        updateForm { state ->
            state.copy(title = state.title.onChange(value))
        }
    }

    private fun onAboutYouChange(value: String) {
        updateForm { state ->
            state.copy(aboutYou = state.aboutYou.onChange(value))
        }
    }

    private fun onWhatsUpChange(value: String) {
        updateForm { state ->
            state.copy(whatsUp = state.whatsUp.onChange(value))
        }
    }

    private fun setLoading(value: Boolean) {
        updateForm { state ->
            state.copy(isSubmitting = value)
        }
    }

    /** Update only the form part of the UiState in a single place to reduce repetition. */
    private inline fun updateForm(transform: (UiState) -> UiState) {
        _formUiState.update { state -> transform(state) }
    }

    private fun bindIoError() {
        execute {

        }
    }

    private fun setToastMessage(message: UiText) {
        execute {
            _uiEvent.send(UiEvent.ShowToastMessage(message))
        }
    }
}