package com.friend.login

import com.friend.common.base.BaseViewModel
import com.friend.domain.apiusecase.auth.PostGoogleLoginApiUseCase
import com.friend.domain.apiusecase.auth.PostLoginApiUseCase
import com.friend.domain.apiusecase.profilemanager.FetchProfileApiUseCase
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
    private val postLoginApiUseCase: PostLoginApiUseCase,
    private val postGoogleLoginApiUseCase: PostGoogleLoginApiUseCase,
    private val fetchProfileApiUseCase: FetchProfileApiUseCase
) : BaseViewModel() {
    val ioError get() = postLoginApiUseCase.ioError.receiveAsFlow()
    private val _uiState = MutableStateFlow(UiState())
    val uiState: StateFlow<UiState> = _uiState.asStateFlow()

    private val _uiEvent = Channel<UiEvent>()
    val uiEvent = _uiEvent.receiveAsFlow()

    init {
        formValidation()
    }

    val action: (UiAction) -> Unit = {
        when (it) {
            is UiAction.UsernameChanged -> onUserNameChanged(it.value)
            is UiAction.PasswordChanged -> onPasswordChanged(it.value)
            is UiAction.ShowErrorMessage -> showToastMessage(it.value)
            is UiAction.PerformGoogleLogin -> performGoogleLogin(it.email)
            UiAction.PerformLogin -> performLogin()
        }
    }

    private fun performLogin() {
        val current = _uiState.value
        val params = PostLoginApiUseCase.Params(
            current.username.value,
            current.password.value
        )

        execute {
            postLoginApiUseCase.execute(params).collect { result ->
                when (result) {
                    is ApiResult.Success -> fetchProfile()
                    is ApiResult.Loading -> _uiState.update {
                        it.copy(isSubmitting = result.loading)
                    }

                    is ApiResult.Error -> showToastMessage(result.message)
                }
            }
        }
    }

    private fun performGoogleLogin(email: String) {
        execute {
            postGoogleLoginApiUseCase.execute(email).collect { result ->
                when (result) {
                    is ApiResult.Success -> {
                        if (result.data.isUserExist)
                            fetchProfile()
                        else _uiEvent.send(UiEvent.NavigateToRegistration(email))
                    }

                    is ApiResult.Loading -> onLoading(result.loading)

                    is ApiResult.Error -> {
                        showToastMessage(result.message)
                        _uiEvent.send(UiEvent.NavigateToRegistration(email))
                    }
                }
            }
        }
    }

    private fun fetchProfile() {
        execute {
            fetchProfileApiUseCase.execute().collect { result ->
                when (result) {
                    is ApiResult.Error -> showToastMessage(result.message)
                    is ApiResult.Loading -> onLoading(result.loading)
                    is ApiResult.Success -> _uiEvent.send(UiEvent.NavigateToHome)
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

    private fun showToastMessage(message: String) {
        execute {
            _uiEvent.send(UiEvent.ShowMessage(message))
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