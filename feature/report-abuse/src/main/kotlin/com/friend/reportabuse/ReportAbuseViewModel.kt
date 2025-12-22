package com.friend.reportabuse

import com.friend.common.base.BaseViewModel
import com.friend.domain.apiusecase.profilemanager.PostAbuseReportApiUseCase
import com.friend.domain.base.ApiResult
import com.friend.domain.validator.ReportAbuseIoResult
import com.friend.sharedpref.SharedPrefHelper
import com.friend.sharedpref.SpKey
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
class ReportAbuseViewModel @Inject constructor(
    private val postAbuseReportApiUseCase: PostAbuseReportApiUseCase,
    private val sharedPrefHelper: SharedPrefHelper
) : BaseViewModel() {
    val ioError get() = postAbuseReportApiUseCase.ioError.receiveAsFlow()
    private val _uiState = MutableStateFlow(UiState())
    val uiState: StateFlow<UiState> = _uiState.asStateFlow()

    private val _uiEvent = Channel<UiEvent>()
    val uiEvent = _uiEvent.receiveAsFlow()

    val action: (UiAction) -> Unit = {
        when (it) {
            is UiAction.SubmitReport -> submitReport(it.username)
            is UiAction.OnTextChange -> onChangeDescription(it.value)
        }
    }

    init {
        bindIoError()
    }

    private fun submitReport(username: String) {
        val current = _uiState.value
        execute {
            postAbuseReportApiUseCase.execute(
                PostAbuseReportApiUseCase.Params(
                    reportedUser = username,
                    reportedBy = sharedPrefHelper.getString(SpKey.userName),
                    reportNote = current.description.value
                )
            ).collect { result ->
                when (result) {
                    is ApiResult.Error ->
                        _uiEvent.send(UiEvent.ShowToastMessage(UiText.Dynamic(result.message)))

                    is ApiResult.Loading -> _uiState.update { it.copy(isSubmitting = result.loading) }
                    is ApiResult.Success -> _uiEvent.send(UiEvent.FinishScreen)
                }
            }
        }
    }

    private fun onChangeDescription(value: String) {
        _uiState.update { state ->
            state.copy(description = state.description.onChange(value))
        }
    }


    private fun bindIoError() {
        execute {
            ioError.collect { error ->
                when (error) {
                    ReportAbuseIoResult.InvalidNote -> _uiState.update { state ->
                        state.copy(
                            description = state.description.copy(
                                isValid = false
                            )
                        )
                    }
                }
            }
        }
    }
}