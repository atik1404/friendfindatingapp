package com.friend.chatroom

import com.friend.common.base.BaseViewModel
import com.friend.domain.apiusecase.chatmessage.DeleteMessagesApiUseCase
import com.friend.domain.apiusecase.chatmessage.FetchConversationsApiUseCase
import com.friend.domain.apiusecase.chatmessage.SearchConversationApiUseCase
import com.friend.domain.base.ApiResult
import com.friend.entity.chatmessage.MessageEntity
import com.friend.ui.common.UiText
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject
import com.friend.designsystem.R as Res

@HiltViewModel
class ChatRoomViewModel @Inject constructor(
    private val fetchConversationsApiUseCase: FetchConversationsApiUseCase,
    private val searchConversationApiUseCase: SearchConversationApiUseCase,
    private val deleteMessagesApiUseCase: DeleteMessagesApiUseCase,
) : BaseViewModel() {
    private val _uiState = MutableStateFlow(UiState())
    val uiState = _uiState.asStateFlow()

    private val _uiEvent = Channel<UiEvent>()
    val uiEvent = _uiEvent.receiveAsFlow()

    val action: (UiAction) -> Unit = { action ->
        when (action) {
            is UiAction.FetchMessages -> fetchMessages(action.username)//fetchRecentMessages(action.username)
            is UiAction.SearchMessage -> searchMessage(
                userName = action.username,
                keyword = action.keyword
            )

            UiAction.OnClearSearch -> clearSearch()
            UiAction.OnClearMessageSelection -> clearSelectedMessage()
            is UiAction.UpdateMessageSelectionStatus -> updateMessageSelectionStatus(action.item)
            UiAction.DeleteMessages -> deleteMessage()
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

            fetchConversationsApiUseCase.execute(toUsername).collect { result ->
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

    private fun searchMessage(userName: String, keyword: String) {
        execute {
            val params = SearchConversationApiUseCase.Params(
                toUsername = userName,
                searchValue = keyword
            )
            searchConversationApiUseCase.execute(params).collect { result ->
                when (result) {
                    is ApiResult.Error -> showToastMessage(UiText.Dynamic(result.message))

                    is ApiResult.Loading -> _uiState.value =
                        _uiState.value.copy(isLoading = result.loading)

                    is ApiResult.Success -> {
                        if (result.data.data.isNotEmpty()) {
                            _uiState.value = _uiState.value.copy(
                                messages = result.data.data.reversed(),
                                isSearchEnabled = true,
                                searchKey = keyword
                            )
                        } else showToastMessage(UiText.StringRes(Res.string.error_no_data_found))
                    }
                }
            }
        }
    }

    private fun deleteMessage() {
        execute {
            val selectedMessages = _uiState.value.messages.filter { it.isItemSelected }
            val params = selectedMessages.map { it.messageId }

            deleteMessagesApiUseCase.execute(params).collect { result ->
                when (result) {
                    is ApiResult.Error -> showToastMessage(UiText.Dynamic(result.message))
                    is ApiResult.Loading -> _uiState.value =
                        _uiState.value.copy(isLoading = result.loading)

                    is ApiResult.Success -> {
                        clearSelectedMessage()
                        _uiEvent.send(UiEvent.DeleteMessageComplete)
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

    private fun updateMessageSelectionStatus(item: MessageEntity) {
        val conversations = _uiState.value.messages.toMutableList()
        val index = conversations.indexOf(item)
        conversations[index] = item.copy(isItemSelected = !item.isItemSelected)
        _uiState.value = _uiState.value.copy(messages = conversations)
    }

    private fun clearSelectedMessage() {
        val conversations = _uiState.value.messages.toMutableList()
        conversations.forEach {
            it.isItemSelected = false
        }
        _uiState.value = _uiState.value.copy(messages = conversations)
    }

    private fun showToastMessage(message: UiText) {
        execute {
            _uiEvent.send(UiEvent.ShowToastMessage(message))
        }
    }
}