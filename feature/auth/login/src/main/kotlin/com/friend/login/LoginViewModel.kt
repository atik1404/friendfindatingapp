package com.friend.login

import com.friend.common.base.BaseViewModel
import com.friend.domain.apiusecase.credential.PostLoginApiUseCase
import com.friend.domain.base.ApiResult
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
            LoginUiEvent.Submit -> performLoginApi()
        }
    }

    private fun performLoginApi() {
        val current = _uiState.value
        val params = PostLoginApiUseCase.Params(current.email, current.password)

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
            it.copy(email = value, emailError = null)
        }
    }

    private fun onPasswordChanged(value: String) {
        _uiState.update {
            it.copy(password = value, passwordError = null)
        }
    }

    private fun handleApiError(message: String) {
        execute {
            _uiEffect.send(LoginUiEffect.ShowMessage(message))
        }
    }
}