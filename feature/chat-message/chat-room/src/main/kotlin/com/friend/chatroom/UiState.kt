package com.friend.chatroom

import android.graphics.Bitmap
import com.friend.entity.chatmessage.ConversationEntity
import com.friend.ui.common.UiText
import java.io.File

data class MessageState(
    val textMessage: String = "",
    val image: File? = null,
    val audio: File? = null,
    val video: File? = null,
    val audioDuration: String = "",
    val videoDuration: String = "",
    val isSending: Boolean = false,
) {
    val isSendEnable = textMessage.isNotEmpty() || image != null || audio != null || video != null
}

data class UiState(
    val conversations: List<ConversationEntity> = emptyList(),
    val isLoading: Boolean = false,
    val error: String = "",
    val isSearchEnabled: Boolean = false,
    val searchKey: String = "",
    val messageContent: MessageState = MessageState()
) {
    val isAlreadyFetched: Boolean get() = conversations.isNotEmpty()
    val isAnyItemSelected: Boolean get() = conversations.any { it.isItemSelected }
}

sealed interface UiEvent {
    data class ShowToastMessage(val message: UiText) : UiEvent
    data object DeleteMessageComplete : UiEvent
    data object ResetScroll : UiEvent
}

sealed interface UiAction {
    data class FetchMessages(val username: String) : UiAction
    data class SearchMessage(val username: String, val keyword: String) : UiAction
    data object OnClearSearch : UiAction
    data object OnClearMessageSelection : UiAction
    data object DeleteMessages : UiAction
    data object OnStartRecording : UiAction
    data object OnStopRecording : UiAction
    data object OnCancelRecording : UiAction
    data class SendMessage(val toUsername: String) : UiAction
    data class OnChangeTextMessage(val message: String) : UiAction
    data class OnChangeImageAttachment(val file: File?) : UiAction
    data class OnChangeVideoAttachment(val file: File?) : UiAction
    data class OnChangeAudioAttachment(val file: File?) : UiAction
    data class UpdateMessageSelectionStatus(val item: ConversationEntity) : UiAction
}