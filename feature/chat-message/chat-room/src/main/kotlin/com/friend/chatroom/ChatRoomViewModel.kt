package com.friend.chatroom

import com.friend.common.base.BaseViewModel
import com.friend.domain.apiusecase.chatmessage.FetchMessageListApiUseCase
import com.friend.domain.apiusecase.chatmessage.FetchMessageSearchResultApiUseCase
import com.friend.domain.base.ApiResult
import com.friend.sharedpref.SharedPrefHelper
import com.friend.sharedpref.SpKey
import com.friend.ui.common.UiText
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject

@HiltViewModel
class ChatRoomViewModel @Inject constructor(
    private val fetchMessageListApiUseCase: FetchMessageListApiUseCase,
    private val fetchMessageSearchResultApiUseCase: FetchMessageSearchResultApiUseCase,
    private val sharedPrefHelper: SharedPrefHelper
) : BaseViewModel() {
    private val _uiState = MutableStateFlow(UiState())
    val uiState = _uiState.asStateFlow()

    private val _uiEvent = Channel<UiEvent>()
    val uiEvent = _uiEvent.receiveAsFlow()

    val action: (UiActon) -> Unit = { action ->
        when (action) {
            is UiActon.FetchMessages -> fetchRecentMessages(action.username)
            is UiActon.SearchMessage -> searchMessage(userName = action.username)
            is UiActon.OnSearchKeywordChange -> onSearchKeywordChange(action.value)
            UiActon.OnClearSearch -> clearSearch()
        }
    }

    private fun fetchRecentMessages(toUsername: String) {
        execute {
            val currentState = _uiState.value
            while (true) {
                if (!currentState.isSearchEnabled) {
                    fetchMessages(toUsername)
                    delay(10000)
                }
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

                    is ApiResult.Success -> {
                        _uiState.value =
                            _uiState.value.copy(
                                messages = result.data.data.reversed(),
                                isSearchEnabled = false
                            )
                    }
                }
            }
        }
    }

    private fun searchMessage(userName: String) {
        execute {
            val params = FetchMessageSearchResultApiUseCase.Params(
                fromUsername = sharedPrefHelper.getString(SpKey.userName),
                toUsername = userName,
                searchValue = _uiState.value.searchKey
            )
            fetchMessageSearchResultApiUseCase.execute(params).collect { result ->
                when (result) {
                    is ApiResult.Error ->
                        _uiEvent.send(UiEvent.ShowToastMessage(UiText.Dynamic(result.message)))

                    is ApiResult.Loading -> _uiState.value =
                        _uiState.value.copy(isLoading = result.loading)

                    is ApiResult.Success -> {
                        _uiState.value.copy(
                            messages = result.data.data.reversed(),
                            isSearchEnabled = true,
                        )
                    }
                }
            }
        }
    }

    private fun clearSearch() {
        execute {
            _uiState.update {
                it.copy(isSearchEnabled = false, searchKey = "")
            }
        }
    }

    private fun onSearchKeywordChange(value: String) {
        execute {
            _uiState.update {
                it.copy(searchKey = value)
            }
        }
    }
}