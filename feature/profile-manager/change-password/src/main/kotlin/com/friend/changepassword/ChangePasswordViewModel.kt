package com.friend.changepassword

import com.friend.common.base.BaseViewModel
import com.friend.domain.apiusecase.profilemanager.PostPasswordChangeApiUseCase
import com.friend.domain.base.ApiResult
import com.friend.domain.validator.PasswordChangeIoResult
import com.friend.sharedpref.SharedPrefHelper
import com.friend.sharedpref.SpKey
import com.friend.ui.common.UiText
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject
import com.friend.designsystem.R as Res

@HiltViewModel
class ChangePasswordViewModel @Inject constructor(
    private val postPasswordChangeApiUseCase: PostPasswordChangeApiUseCase,
    private val sharedPrefHelper: SharedPrefHelper
) : BaseViewModel() {

    private val ioError = postPasswordChangeApiUseCase.ioError.receiveAsFlow()
    private val _uiState = MutableStateFlow(UiState())
    val uiState = _uiState.asStateFlow()

    private val _uiEvent = Channel<UiEvent>()
    val uiEvent = _uiEvent.receiveAsFlow()

    val action: (UiAction) -> Unit = {
        when (it) {
            is UiAction.OnConfirmPasswordChange -> onConfirmPasswordChange(it.value)
            is UiAction.OnNewPasswordChange -> onNewPasswordChange(it.value)
            is UiAction.OnOldPasswordChange -> onOldPasswordChange(it.value)
            UiAction.PerformPasswordChanged -> performPasswordChanged()
            UiAction.ResetState -> _uiState.value = UiState()
        }
    }

    init {
        bindIoError()
    }

    private fun onOldPasswordChange(value: String) {
        _uiState.update { state ->
            state.copy(oldPassword = state.oldPassword.onChange(value))
        }
    }

    private fun onNewPasswordChange(value: String) {
        _uiState.update { state ->
            state.copy(newPassword = state.newPassword.onChange(value))
        }
    }

    private fun onConfirmPasswordChange(value: String) {
        _uiState.update { state ->
            state.copy(confirmPassword = state.confirmPassword.onChange(value))
        }
    }

    private fun performPasswordChanged() {
        val current = _uiState.value
        val params = PostPasswordChangeApiUseCase.Params(
            sharedPrefHelper.getString(SpKey.userName),
            current.oldPassword.value,
            current.newPassword.value,
            current.confirmPassword.value,
        )

        execute {
            postPasswordChangeApiUseCase.execute(params).collect { result ->
                when (result) {
                    is ApiResult.Error -> _uiEvent.send(
                        UiEvent.ShowToastMessage(
                            UiText.Dynamic(
                                result.message
                            )
                        )
                    )

                    is ApiResult.Loading -> _uiState.update { it.copy(isFormSubmitting = result.loading) }
                    is ApiResult.Success -> _uiEvent.send(UiEvent.BackToPreviousScreen)
                }
            }
        }
    }

    private fun bindIoError() {
        execute {
            ioError.collect { error ->
                when (error) {
                    PasswordChangeIoResult.InvalidConfirmPassword -> _uiState.update { state ->
                        state.copy(
                            confirmPassword = state.confirmPassword.copy(
                                isValid = false
                            )
                        )
                    }

                    PasswordChangeIoResult.InvalidNewPassword -> _uiState.update { state ->
                        state.copy(
                            newPassword = state.newPassword.copy(
                                isValid = false
                            )
                        )
                    }

                    PasswordChangeIoResult.InvalidOldPassword -> _uiState.update { state ->
                        state.copy(
                            oldPassword = state.oldPassword.copy(
                                isValid = false
                            )
                        )
                    }

                    PasswordChangeIoResult.PasswordNotMatched -> _uiEvent.send(
                        UiEvent.ShowToastMessage(
                            UiText.StringRes(Res.string.error_password_not_matched)
                        )
                    )
                }
            }
        }
    }
}