package com.friend.chatlist

import com.friend.common.base.BaseViewModel
import com.friend.common.constant.AppConstants
import com.friend.domain.apiusecase.chatmessage.FetchChatListApiUseCase
import com.friend.domain.base.ApiResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

@HiltViewModel
class ChatListViewModel @Inject constructor(
    private val chatListApiUseCase: FetchChatListApiUseCase,
) : BaseViewModel() {

    private val _uiState = MutableStateFlow(UiState())
    val uiState: StateFlow<UiState> = _uiState.asStateFlow()

    val action: (UiAction) -> Unit = {
        when (it) {
            UiAction.FetchChatList -> fetchChatList()
            UiAction.SearchByKeyword -> {}
            UiAction.LoadMore -> fetchChatList()
        }
    }

    private fun fetchChatList() {
        execute {
            val currentState = _uiState.value
            chatListApiUseCase.execute(currentState.pageNo).collect { result ->
                when (result) {
                    is ApiResult.Error -> _uiState.value =
                        _uiState.value.copy(error = result.message)

                    is ApiResult.Loading -> {
                        if (currentState.data.isEmpty())
                            _uiState.value = _uiState.value.copy(
                                isLoading = result.loading,
                                isLoadingMore = false
                            )
                        else _uiState.value =
                            _uiState.value.copy(isLoadingMore = result.loading, isLoading = false)
                    }

                    is ApiResult.Success -> {
                        val currentItems = _uiState.value.data
                        val updatedItems = currentItems + result.data
                        val pageNo = currentState.pageNo + 1
                        val hasMorePage = result.data.size >= AppConstants.DATA_PER_PAGE

                        _uiState.value = _uiState.value.copy(
                            data = updatedItems,
                            pageNo = pageNo,
                            hasMorePage = hasMorePage
                        )
                    }
                }
            }
        }
    }
}