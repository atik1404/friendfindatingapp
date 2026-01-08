package com.friend.chatroom

import com.friend.common.base.BaseViewModel
import com.friend.domain.apiusecase.chatmessage.DeleteMessagesApiUseCase
import com.friend.domain.apiusecase.chatmessage.FetchMessageListApiUseCase
import com.friend.domain.apiusecase.chatmessage.FetchMessageSearchResultApiUseCase
import com.friend.domain.apiusecase.chatmessage.ForwardMessageApiUseCase
import com.friend.domain.base.ApiResult
import com.friend.entity.chatmessage.MessageEntity
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
import com.friend.designsystem.R as Res

@HiltViewModel
class ChatRoomViewModel @Inject constructor(
    private val fetchMessageListApiUseCase: FetchMessageListApiUseCase,
    private val fetchMessageSearchResultApiUseCase: FetchMessageSearchResultApiUseCase,
    private val forwardMessageApiUseCase: ForwardMessageApiUseCase,
    private val deleteMessagesApiUseCase: DeleteMessagesApiUseCase,
    private val sharedPrefHelper: SharedPrefHelper
) : BaseViewModel() {
    private val _uiState = MutableStateFlow(UiState())
    val uiState = _uiState.asStateFlow()

    private val _uiEvent = Channel<UiEvent>()
    val uiEvent = _uiEvent.receiveAsFlow()

    val action: (UiActon) -> Unit = { action ->
        when (action) {
            is UiActon.FetchMessages -> fetchMessages(action.username)//fetchRecentMessages(action.username)
            is UiActon.SearchMessage -> searchMessage(
                userName = action.username,
                keyword = action.keyword
            )

            UiActon.OnClearSearch -> clearSearch()
            UiActon.OnClearMessageSelection -> clearSelectedMessage()
            is UiActon.UpdateMessageSelectionStatus -> updateMessageSelectionStatus(action.item)
            UiActon.DeleteMessages -> deleteMessage()
            is UiActon.ForwardMessages -> forwardMessage(action.toUserNames)
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

    private fun searchMessage(userName: String, keyword: String) {
        execute {
            val params = FetchMessageSearchResultApiUseCase.Params(
                fromUsername = sharedPrefHelper.getString(SpKey.userName),
                toUsername = userName,
                searchValue = keyword
            )
            fetchMessageSearchResultApiUseCase.execute(params).collect { result ->
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

    private fun forwardMessage(toUsernames: List<String>) {
        execute {
            val params = ForwardMessageApiUseCase.Params(
                ids = _uiState.value.messages.filter { it.isItemSelected }.map { it.messageId },
                toUsernames = toUsernames
            )

            forwardMessageApiUseCase.execute(params).collect { result ->
                when (result) {
                    is ApiResult.Error -> showToastMessage(UiText.Dynamic(result.message))
                    is ApiResult.Loading -> _uiState.value =
                        _uiState.value.copy(isLoading = result.loading)

                    is ApiResult.Success -> {
                        clearSelectedMessage()
                        _uiEvent.send(UiEvent.ForwardMessageComplete)
                    }
                }
            }
        }
    }

    private fun deleteMessage() {
        execute {
            val selectedMessages = _uiState.value.messages.filter { it.isItemSelected }
            if (selectedMessages.isEmpty()) {
                showToastMessage(UiText.StringRes(Res.string.error_invalid_selected_items))
                return@execute
            }
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