package com.friend.login

import com.friend.common.base.BaseViewModel
import com.friend.domain.apiusecase.credential.PostLoginApiUseCase
import com.friend.domain.base.ApiResult
import com.friend.domain.validator.LoginIoResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val postLoginApiUseCase: PostLoginApiUseCase
) : BaseViewModel() {
    val ioError get() = postLoginApiUseCase.ioError.receiveAsFlow()
    private val _uiState = MutableStateFlow(UiState())
    val uiState: StateFlow<UiState> = _uiState.asStateFlow()

    private val _uiEffect = Channel<UiEvent>()
    val uiEffect = _uiEffect.receiveAsFlow()

    init {
        formValidation()
    }

    val action: (UiAction) -> Unit = {
        when (it) {
            is UiAction.UsernameChanged -> onUserNameChanged(it.value)
            is UiAction.PasswordChanged -> onPasswordChanged(it.value)
            UiAction.PerformLogin -> performLoginApi()
            UiAction.ResetState -> _uiState.value = UiState()
        }
    }

    private fun performLoginApi() {
        val current = _uiState.value
        val params = PostLoginApiUseCase.Params(
            current.username.value,
            current.password.value
        )

        execute {
            postLoginApiUseCase.execute(params).collect { result ->
                when (result) {
                    is ApiResult.Success -> {
                        if (result.data.userName.isNotEmpty())
                            _uiEffect.send(UiEvent.NavigateToHome)
                        else handleApiError(result.data.message)
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
        _uiState.update { state ->
            state.copy(username = state.username.onChange(value))
        }
    }

    private fun onPasswordChanged(value: String) {
        _uiState.update { state ->
            state.copy(password = state.password.onChange(value))
        }
    }

    private fun handleApiError(message: String) {
        execute {
            _uiEffect.send(UiEvent.ShowMessage(message))
        }
    }

    // Validate, update error flags, and only then call API if valid.
    private fun formValidation() {
        execute {
            ioError.collect { error ->
                when (error) {
                    LoginIoResult.InvalidPassword -> _uiState.update { state ->
                        state.copy(
                            password = state.password.copy(
                                isValid = false
                            )
                        )
                    }

                    LoginIoResult.InvalidUsername -> _uiState.update { state ->
                        state.copy(
                            username = state.username.copy(
                                isValid = false
                            )
                        )
                    }
                }
            }
        }
    }
}