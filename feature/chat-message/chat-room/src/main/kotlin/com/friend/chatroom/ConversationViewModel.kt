package com.friend.chatroom

import android.app.Application
import androidx.lifecycle.viewModelScope
import com.friend.chatroom.utils.AudioRecorder
import com.friend.common.base.BaseViewModel
import com.friend.common.utils.FilesUtils
import com.friend.domain.apiusecase.chatmessage.DeleteMessagesApiUseCase
import com.friend.domain.apiusecase.chatmessage.FetchConversationsApiUseCase
import com.friend.domain.apiusecase.chatmessage.SearchConversationApiUseCase
import com.friend.domain.apiusecase.chatmessage.SendMessageApiUseCase
import com.friend.domain.base.ApiResult
import com.friend.domain.validator.SendMessageIoResult
import com.friend.entity.chatmessage.ConversationEntity
import com.friend.sharedpref.SharedPrefHelper
import com.friend.sharedpref.SpKey
import com.friend.ui.common.UiText
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import java.io.File
import javax.inject.Inject
import com.friend.designsystem.R as Res

@HiltViewModel
class ConversationViewModel @Inject constructor(
    private val application: Application,
    private val fetchConversationsApiUseCase: FetchConversationsApiUseCase,
    private val searchConversationApiUseCase: SearchConversationApiUseCase,
    private val deleteMessagesApiUseCase: DeleteMessagesApiUseCase,
    private val sendMessageApiUseCase: SendMessageApiUseCase,
    private val sharedPrefHelper: SharedPrefHelper,
) : BaseViewModel() {
    val ioError get() = sendMessageApiUseCase.ioError.receiveAsFlow()
    private val _uiState = MutableStateFlow(
        UiState(
            fromUsername = sharedPrefHelper.getString(SpKey.userName)
        )
    )
    val uiState = _uiState.asStateFlow()

    private val _uiEvent = Channel<UiEvent>()
    val uiEvent = _uiEvent.receiveAsFlow()

    private var pollingJob: Job? = null

    private val audioRecorder = AudioRecorder()

    private val _lastRecordedFile = MutableStateFlow<File?>(null)
    private var totalConversationLength = 0

    val action: (UiAction) -> Unit = { action ->
        when (action) {
            is UiAction.FetchMessages -> fetchMessages(action.username)
            is UiAction.SearchMessage -> searchMessage(
                userName = action.username,
                keyword = action.keyword
            )

            UiAction.OnClearSearch -> clearSearch()
            UiAction.OnClearMessageSelection -> clearSelectedMessage()
            is UiAction.UpdateMessageSelectionStatus -> toggleMessageSelection(action.item)
            UiAction.DeleteMessages -> deleteMessage()
            is UiAction.SendMessage -> sendMessage(action.toUsername)
            is UiAction.OnChangeTextMessage -> onChangeTextMessage(action.message)
            is UiAction.OnChangeAudioAttachment -> onChangeAudioFile(action.file)
            is UiAction.OnChangeImageAttachment -> onChangeImageFile(action.file)
            is UiAction.OnChangeVideoAttachment -> onChangeVideoFile(action.file)
            UiAction.OnCancelRecording -> cancelRecording()
            UiAction.OnStartRecording -> startRecording()
            UiAction.OnStopRecording -> {
                stopRecording()
            }
        }
    }

    init {
        handelIoError()
    }

    fun startPolling(toUsername: String) {
        if (pollingJob?.isActive == true) return

        pollingJob = viewModelScope.launch {
            while (isActive) {
                fetchMessages(toUsername)
                delay(10_000L)
            }
        }
    }

    fun stopPolling() {
        pollingJob?.cancel()
        pollingJob = null
    }

    private fun fetchMessages(toUsername: String) {
        execute {
            if (_uiState.value.isSearchEnabled) return@execute
            fetchConversationsApiUseCase.execute(toUsername).collect { result ->
                when (result) {
                    is ApiResult.Error -> _uiState.value =
                        _uiState.value.copy(error = result.message)

                    is ApiResult.Loading -> {
                        if (!_uiState.value.isAlreadyFetched)
                            _uiState.update { it.copy(isLoading = result.loading) }
                    }

                    is ApiResult.Success -> {
                        val selectedItems =
                            _uiState.value.conversations.filter { it.isItemSelected }
                                .map { it.messageId }.toSet()

                        val conversations = result.data.data.map {
                            it.copy(isItemSelected = selectedItems.contains(it.messageId))
                        }
                        val isMyMessage = conversations.lastOrNull()?.isMyMessage

                        if (totalConversationLength != 0 && totalConversationLength < conversations.size && isMyMessage == false) {
                            showIncomingMessageLoading()
                            delay(500)
                            updateConversations(conversations, result.data.isBlocked)
                            _uiEvent.send(UiEvent.ResetScroll)
                        } else updateConversations(conversations, result.data.isBlocked)

                        totalConversationLength = conversations.size
                    }
                }
            }
        }
    }

    private fun updateConversations(conversation: List<ConversationEntity>, isBlocked: Boolean) {
        _uiState.update {
            it.copy(
                conversations = conversation.reversed(),
                isSearchEnabled = false,
                isIncoming = false,
                isBlocked = isBlocked,
                error = "",
            )
        }
    }

    private fun sendMessage(username: String) {
        execute {
            val currentState = _uiState.value.messageContent
            val videoDuration = FilesUtils.getFileDurationMs(currentState.video)
            val audioDuration = FilesUtils.getFileDurationMs(currentState.audio)

            val params = SendMessageApiUseCase.Params(
                toUsername = username,
                content = currentState.textMessage,
                image = currentState.image,
                video = currentState.video,
                videoDuration = videoDuration,
                audio = currentState.audio,
                audioDuration = audioDuration
            )

            sendMessageApiUseCase.execute(params).collect { result ->
                when (result) {
                    is ApiResult.Error -> showToastMessage(UiText.Dynamic(result.message))
                    is ApiResult.Loading -> _uiState.value =
                        _uiState.value.copy(
                            messageContent = _uiState.value.messageContent.copy(
                                isSending = result.loading
                            )
                        )

                    is ApiResult.Success -> {
                        val conversations = _uiState.value.conversations.toMutableList()
                        conversations.add(0, result.data)
                        _uiState.update {
                            it.copy(
                                conversations = conversations,
                                messageContent = MessageState()
                            )
                        }
                        _uiEvent.send(UiEvent.ResetScroll)
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
                                conversations = result.data.data.reversed(),
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
            val selectedMessages = _uiState.value.conversations.filter { it.isItemSelected }
            val params = selectedMessages.map { it.messageId }

            deleteMessagesApiUseCase.execute(params).collect { result ->
                when (result) {
                    is ApiResult.Error -> showToastMessage(UiText.Dynamic(result.message))
                    is ApiResult.Loading -> _uiState.value =
                        _uiState.value.copy(isLoading = result.loading)

                    is ApiResult.Success -> {
                        clearSelectedMessage()
                        clearSearch()
                        _uiState.update {
                            it.copy(
                                conversations = emptyList(),
                            )
                        }
                        _uiEvent.send(UiEvent.DeleteMessageComplete)
                    }
                }
            }
        }
    }

    private fun clearSearch() {
        _uiState.value = _uiState.value.copy(isSearchEnabled = false, searchKey = "")
    }

    private fun toggleMessageSelection(item: ConversationEntity) {
        val updatedList = _uiState.value.conversations.map {
            if (it.messageId == item.messageId) {
                it.copy(isItemSelected = !it.isItemSelected)
            } else {
                it
            }
        }

        _uiState.value = _uiState.value.copy(conversations = updatedList)
    }

    private fun clearSelectedMessage() {
        val conversations = _uiState.value.conversations.toMutableList()
        conversations.forEach {
            it.isItemSelected = false
        }
        _uiState.value = _uiState.value.copy(conversations = conversations)
    }

    private fun showToastMessage(message: UiText) {
        execute {
            _uiEvent.send(UiEvent.ShowToastMessage(message))
        }
    }

    private fun onChangeTextMessage(value: String) {
        execute {
            _uiState.update {
                it.copy(messageContent = it.messageContent.copy(textMessage = value))
            }
        }
    }

    private fun onChangeImageFile(file: File?) {
        execute {
            _uiState.update {
                it.copy(messageContent = it.messageContent.copy(image = file))
            }
        }
    }

    private fun onChangeAudioFile(file: File?) {
        execute {
            _uiState.update {
                it.copy(messageContent = it.messageContent.copy(audio = file))
            }
        }
    }

    private fun onChangeVideoFile(file: File?) {
        execute {
            _uiState.update {
                it.copy(messageContent = it.messageContent.copy(video = file))
            }
        }
    }

    private fun startRecording() {
        val file = audioRecorder.start(application)
        _lastRecordedFile.value = file
    }

    private fun stopRecording() {
        _lastRecordedFile.value = audioRecorder.stop()
        _uiState.update {
            it.copy(messageContent = it.messageContent.copy(audio = _lastRecordedFile.value))
        }
    }

    private fun cancelRecording() {
        _lastRecordedFile.value = null
        _uiState.update {
            it.copy(messageContent = it.messageContent.copy(audio = null))
        }
    }

    override fun onCleared() {
        audioRecorder.release()
        super.onCleared()
    }

    private fun handelIoError() {
        execute {
            ioError.collect { error ->
                when (error) {
                    SendMessageIoResult.InvalidMessage -> _uiState.value = _uiState.value.copy(
                        messageContent = MessageState()
                    )
                }
            }
        }
    }

    private fun showIncomingMessageLoading() {
        _uiState.value = _uiState.value.copy(isIncoming = true)
    }
}