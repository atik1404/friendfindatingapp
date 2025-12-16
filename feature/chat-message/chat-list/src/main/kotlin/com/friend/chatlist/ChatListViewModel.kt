package com.friend.chatlist

import com.friend.common.base.BaseViewModel
import com.friend.domain.apiusecase.chatmessage.FetchChatListApiUseCase
import com.friend.domain.base.ApiResult
import com.friend.entity.chatmessage.ChatListItemApiEntity
import com.friend.sharedpref.SharedPrefHelper
import com.friend.sharedpref.SpKey
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

@HiltViewModel
class ChatListViewModel @Inject constructor(
    private val chatListApiUseCase: FetchChatListApiUseCase,
    private val sharedPrefHelper: SharedPrefHelper,
) : BaseViewModel() {
    private var pageNumber = 0

    private val _uiState = MutableStateFlow<UiState>(UiState.Success(emptyList()))
    val uiState: StateFlow<UiState> = _uiState

    val action: (UiAction) -> Unit = {
        when (it) {
            UiAction.FetchChatList -> {
                pageNumber = 0
                fetchChatList()
            }

            UiAction.SearchByKeyword -> {}
            UiAction.LoadMore -> fetchChatList()
        }
    }

    private fun fetchChatList() {
        execute {
            val userName = sharedPrefHelper.getString(SpKey.userName)
            val params = FetchChatListApiUseCase.Params(userName, pageNumber)
            chatListApiUseCase.execute(params).collect { result ->
                when (result) {
                    is ApiResult.Error -> _uiState.value = UiState.Error(result.message)
                    is ApiResult.Loading -> _uiState.value = UiState.Loading
                    is ApiResult.Success -> {
                        if (result.data.isEmpty()) {
                            _uiState.value = UiState.NoDataFound
                        } else {
                            _uiState.value = UiState.Success(result.data)
                            //pageNumber++
                        }
                    }
                }
            }
        }
    }
}