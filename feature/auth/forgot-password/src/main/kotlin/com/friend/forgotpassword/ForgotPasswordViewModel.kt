package com.friend.forgotpassword

import com.friend.common.base.BaseViewModel
import com.friend.domain.apiusecase.credential.PostForgotPasswordApiUseCase
import com.friend.domain.base.ApiResult
import com.friend.domain.validator.ForgotPasswordIoResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject

@HiltViewModel
class ForgotPasswordViewModel @Inject constructor(
    private val forgotPasswordUseCase: PostForgotPasswordApiUseCase
) : BaseViewModel() {
    val ioError get() = forgotPasswordUseCase.ioError.receiveAsFlow()
    private val _uiState = MutableStateFlow(UiState())
    val uiState: StateFlow<UiState> = _uiState.asStateFlow()

    private val _uiEvent = Channel<UiEvent>()
    val uiEffect = _uiEvent.receiveAsFlow()

    init {
        formValidation()
    }

    val action: (UiAction) -> Unit = {
        when (it) {
            is UiAction.EmailChanged -> onEmailChanged(it.value)
            UiAction.SendLinkToEmail -> forgotPassword()
            UiAction.ResetState -> _uiState.value = UiState()
        }
    }

    private fun forgotPassword() {
        execute {
            forgotPasswordUseCase.execute(_uiState.value.email.value).collect { result ->
                when (result) {
                    is ApiResult.Error -> showMessage(result.message)
                    is ApiResult.Loading -> onLoading(result.loading)
                    is ApiResult.Success -> {
                        showMessage(result.data)
                        _uiEvent.send(UiEvent.BackToPreviousScreen)
                    }
                }
            }
        }
    }

    private fun onLoading(isLoading: Boolean) {
        _uiState.update {
            it.copy(isLoading = isLoading)
        }
    }

    private fun onEmailChanged(value: String) {
        _uiState.update {
            it.copy(email = it.email.onChange(newValue = value))
        }
    }

    private fun showMessage(message: String) {
        execute {
            _uiEvent.send(UiEvent.ShowMessage(message))
        }
    }

    // Validate, update error flags, and only then call API if valid.
    private fun formValidation() {
        execute {
            ioError.collect { error ->
                when (error) {
                    ForgotPasswordIoResult.InvalidEmail -> _uiState.update {
                        it.copy(email = it.email.copy(isValid = false))
                    }
                }
            }
        }
    }
}