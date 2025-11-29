package com.friend.forgotpassword

import com.friend.common.base.BaseViewModel
import com.friend.domain.apiusecase.credential.PostForgotPasswordApiUseCase
import com.friend.domain.base.ApiResult
import com.friend.ui.validator.isUsernameValid
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

    private val _uiState = MutableStateFlow(UiState())
    val uiState: StateFlow<UiState> = _uiState.asStateFlow()

    private val _uiEffect = Channel<UiEffect>()
    val uiEffect = _uiEffect.receiveAsFlow()

    val action: (UiEvent) -> Unit = {
        when (it) {
            is UiEvent.EmailChanged -> onEmailChanged(it.value)
            UiEvent.FormValidator -> formValidation()
        }
    }

    private fun forgotPassword() {
        execute {
            forgotPasswordUseCase.execute(_uiState.value.email).collect { result ->
                when (result) {
                    is ApiResult.Error -> showMessage(result.message)
                    is ApiResult.Loading -> onLoading(result.loading)
                    is ApiResult.Success -> {
                        showMessage(result.data)
                        _uiEffect.send(UiEffect.BackToPreviousScreen)
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
            it.copy(email = value, isEmailValid = true)
        }
    }

    private fun showMessage(message: String) {
        execute {
            _uiEffect.send(UiEffect.ShowMessage(message))
        }
    }

    // Validate, update error flags, and only then call API if valid.
    private fun formValidation() {
        val current = _uiState.value
        val isEmailValid = current.email.isUsernameValid()

        _uiState.update {
            it.copy(
                email = current.email,
                isEmailValid = isEmailValid,
            )
        }

        if (isEmailValid) {
            forgotPassword()
        }
    }
}