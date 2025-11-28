package com.friend.login

import com.friend.common.base.BaseViewModel
import com.friend.domain.apiusecase.credential.PostLoginApiUseCase
import com.friend.domain.base.ApiResult
import com.friend.ui.validator.isPasswordValid
import com.friend.ui.validator.isUsernameValid
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val postLoginApiUseCase: PostLoginApiUseCase
) : BaseViewModel() {
    private val _uiState = MutableStateFlow(LoginUiState())
    val uiState: StateFlow<LoginUiState> = _uiState.asStateFlow()

    private val _uiEffect = Channel<LoginUiEffect>()
    val uiEffect = _uiEffect.receiveAsFlow()

    val action: (LoginUiEvent) -> Unit = {
        when (it) {
            is LoginUiEvent.EmailChanged -> onUserNameChanged(it.value)
            is LoginUiEvent.PasswordChanged -> onPasswordChanged(it.value)
            LoginUiEvent.FormValidator -> formValidation()
        }
    }

    private fun performLoginApi() {
        val current = _uiState.value
        val params = PostLoginApiUseCase.Params(current.username, current.password)

        execute {
            postLoginApiUseCase.execute(params).collect { result ->
                when (result) {
                    is ApiResult.Success -> {
                        Timber.e("Login success: ${result.data.userName}")
                    }

                    is ApiResult.Loading -> onLoading(result.loading)
                    is ApiResult.Error -> handleApiError(result.message)
                }
            }
        }
    }

    private fun onLoading(isLoading: Boolean) {
        _uiState.update {
            it.copy(isLoading = isLoading)
        }
    }

    private fun onUserNameChanged(value: String) {
        _uiState.update {
            it.copy(username = value, isUsernameValid = true)
        }
    }

    private fun onPasswordChanged(value: String) {
        _uiState.update {
            it.copy(password = value, isPasswordValid = true)
        }
    }

    private fun handleApiError(message: String) {
        execute {
            _uiEffect.send(LoginUiEffect.ShowMessage(message))
        }
    }

    // Validate, update error flags, and only then call API if valid.
    private fun formValidation() {
        val current = _uiState.value
        val usernameValid = current.username.isUsernameValid()
        val passwordValid = current.password.isPasswordValid()
        val formValid = usernameValid && passwordValid

        _uiState.update {
            it.copy(
                isUsernameValid = usernameValid,
                isPasswordValid = passwordValid,
            )
        }

        if (formValid) {
            performLoginApi()
        }
    }
}