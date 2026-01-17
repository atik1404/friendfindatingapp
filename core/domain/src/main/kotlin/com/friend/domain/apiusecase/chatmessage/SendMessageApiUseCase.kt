package com.friend.domain.apiusecase.chatmessage

import com.friend.domain.base.ApiResult
import com.friend.domain.repository.remote.ChatMessagesRepository
import com.friend.domain.usecase.ApiUseCaseParams
import com.friend.entity.chatmessage.ConversationEntity
import kotlinx.coroutines.flow.Flow
import java.io.File
import javax.inject.Inject

class SendMessageApiUseCase @Inject constructor(
    private val repository: ChatMessagesRepository,
) : ApiUseCaseParams<SendMessageApiUseCase.Params, ConversationEntity> {

    data class Params(
        val toUsername: String,
        val content: String,
        val image: File? = null,
        val audio: File? = null,
        val video: File? = null,
        val audioDuration: String? = null,
        val videoDuration: String? = null,
    )

    override suspend fun execute(params: Params): Flow<ApiResult<ConversationEntity>> =
        repository.sendMessage(params)
}