package com.friend.forwardmessage

import com.friend.common.base.BaseViewModel
import com.friend.common.constant.AppConstants
import com.friend.domain.apiusecase.chatmessage.FetchChatListApiUseCase
import com.friend.domain.apiusecase.chatmessage.ForwardMessageApiUseCase
import com.friend.domain.base.ApiResult
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
import com.friend.designsystem.R as Res

@HiltViewModel
class ForwardMessageViewModel @Inject constructor(
    private val chatListApiUseCase: FetchChatListApiUseCase,
    private val forwardMessageApiUseCase: ForwardMessageApiUseCase,
    private val sharedPrefHelper: SharedPrefHelper
) : BaseViewModel() {

    private val _uiState = MutableStateFlow(UiState())
    val uiState: StateFlow<UiState> = _uiState.asStateFlow()

    private val _uiEvent = Channel<UiEvent>()
    val uiEvent = _uiEvent.receiveAsFlow()

    val action: (UiAction) -> Unit = {
        when (it) {
            UiAction.FetchChatList -> fetchChatList()
            is UiAction.SearchByKeyword -> onSearchKeywordChange(it.value)
            is UiAction.UpdateSelection -> toggleSelection(it.username)
            UiAction.LoadMore -> fetchChatList()
            is UiAction.ForwardMessages -> forwardMessage(it.messageIds)
        }
    }

    private fun fetchChatList() {
        execute {
            val currentState = _uiState.value
            chatListApiUseCase.execute(currentState.pageNo).collect { result ->
                when (result) {
                    is ApiResult.Error -> _uiState.update { it.copy(error = result.message) }

                    is ApiResult.Loading -> {
                        if (currentState.data.isEmpty())
                            _uiState.value = _uiState.value.copy(
                                isLoading = result.loading,
                                isLoadingMore = false,
                                error = ""
                            )
                        else _uiState.update {
                            it.copy(
                                isLoadingMore = result.loading, isLoading = false,
                                error = ""
                            )
                        }

                    }

                    is ApiResult.Success -> {
                        val currentItems = _uiState.value.data
                        val updatedItems = currentItems + result.data
                        val pageNo = currentState.pageNo + 1
                        val hasMorePage = result.data.size >= AppConstants.DATA_PER_PAGE

                        _uiState.value = _uiState.value.copy(
                            data = updatedItems,
                            pageNo = pageNo,
                            hasMorePage = hasMorePage,
                            error = ""
                        )
                    }
                }
            }
        }
    }

    private fun onSearchKeywordChange(value: String) {
        execute {
            _uiState.value = _uiState.value.copy(searchKeyword = value)
        }
    }

    private fun toggleSelection(username: String) {
        val updatedList = _uiState.value.data.map {
            if (it.toUsername == username) {
                it.copy(isItemSelected = !it.isItemSelected)
            } else {
                it
            }
        }
        _uiState.value = _uiState.value.copy(data = updatedList)
    }

    private fun forwardMessage(messageIds: List<String>) {
        execute {
            if (!_uiState.value.data.any { it.isItemSelected }) {
                showToastMessage(UiText.StringRes(Res.string.error_invalid_selected_items))
                return@execute
            }
            val params = ForwardMessageApiUseCase.Params(
                fromUsername = sharedPrefHelper.getString(SpKey.userName),
                ids = messageIds,
                toUsernames = _uiState.value.data.filter { it.isItemSelected }.map { it.toUsername }
            )

            forwardMessageApiUseCase.execute(params).collect { result ->
                when (result) {
                    is ApiResult.Error -> showToastMessage(UiText.Dynamic(result.message))

                    is ApiResult.Loading -> _uiState.value =
                        _uiState.value.copy(
                            isLoadingMore = false, isLoading = result.loading,
                            error = ""
                        )

                    is ApiResult.Success -> {
                        showToastMessage(UiText.Dynamic(result.data))
                        _uiEvent.send(UiEvent.ForwardMessageComplete)
                    }
                }
            }
        }
    }

    private fun showToastMessage(message: UiText) {
        execute {
            _uiEvent.send(
                UiEvent.ShowToastMessage(
                    message
                )
            )
        }
    }
}