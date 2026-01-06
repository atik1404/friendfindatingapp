package com.friend.chatroom

import com.friend.common.base.BaseViewModel
import com.friend.domain.apiusecase.chatmessage.FetchMessageListApiUseCase
import com.friend.domain.base.ApiResult
import com.friend.sharedpref.SharedPrefHelper
import com.friend.sharedpref.SpKey
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

@HiltViewModel
class ChatRoomViewModel @Inject constructor(
    private val fetchMessageListApiUseCase: FetchMessageListApiUseCase,
    private val sharedPrefHelper: SharedPrefHelper
) : BaseViewModel() {
    private val _uiState = MutableStateFlow(UiState())
    val uiState = _uiState.asStateFlow()

    val action: (UiActon) -> Unit = { action ->
        when (action) {
            is UiActon.FetchMessages -> refreshMessages(action.username)
        }
    }

    private fun refreshMessages(toUsername: String) {
        execute {
            while (true) {
                fetchMessages(toUsername)
                delay(10000)
            }
        }
    }

    private fun fetchMessages(toUsername: String) {
        execute {
            val params = FetchMessageListApiUseCase.Params(
                fromUsername = sharedPrefHelper.getString(SpKey.userName),
                toUsername = toUsername
            )

            fetchMessageListApiUseCase.execute(params).collect { result ->
                when (result) {
                    is ApiResult.Error -> _uiState.value =
                        _uiState.value.copy(error = result.message)

                    is ApiResult.Loading -> {
                        if (!_uiState.value.isAlreadyFetched)
                            _uiState.value =
                                _uiState.value.copy(isLoading = result.loading)
                    }

                    is ApiResult.Success -> _uiState.value =
                        _uiState.value.copy(messages = result.data.data.reversed())
                }
            }
        }
    }
}