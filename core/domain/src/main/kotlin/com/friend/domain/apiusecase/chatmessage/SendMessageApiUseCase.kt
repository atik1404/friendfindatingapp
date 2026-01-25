package com.friend.domain.apiusecase.chatmessage

import com.friend.domain.base.ApiResult
import com.friend.domain.repository.remote.ChatMessagesRepository
import com.friend.domain.usecase.ApiUseCaseParams
import com.friend.domain.validator.DataValidationResult
import com.friend.domain.validator.LoginIoResult
import com.friend.domain.validator.SendMessageIoResult
import com.friend.entity.chatmessage.ConversationEntity
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emptyFlow
import java.io.File
import javax.inject.Inject

class SendMessageApiUseCase @Inject constructor(
    private val repository: ChatMessagesRepository,
) : ApiUseCaseParams<SendMessageApiUseCase.Params, ConversationEntity> {
    val ioError = Channel<SendMessageIoResult>()

    data class Params(
        val toUsername: String,
        val content: String? = null,
        val deviceToken: String = "",
        val image: File? = null,
        val audio: File? = null,
        val video: File? = null,
        val audioDuration: Long? = null,
        val videoDuration: Long? = null,
    )

    override suspend fun execute(params: Params): Flow<ApiResult<ConversationEntity>> {
        return when (val validationResult = validation(params)) {
            is DataValidationResult.Failure<*> -> {
                ioError.send(validationResult.ioErrorResult as SendMessageIoResult)
                emptyFlow()
            }

            DataValidationResult.Success -> repository.sendMessage(params)
        }
    }

    private fun validation(params: Params): DataValidationResult {
        if (params.content.isNullOrEmpty() && params.video == null && params.audio == null && params.image == null)
            return DataValidationResult.Failure(SendMessageIoResult.InvalidMessage)

        if (params.audio != null) {
            if (params.audioDuration == null || params.audioDuration < 1000)
                return DataValidationResult.Failure(SendMessageIoResult.InvalidMessage)
        }

        if (params.video != null) {
            if (params.videoDuration == null || params.videoDuration < 1000)
                return DataValidationResult.Failure(SendMessageIoResult.InvalidMessage)
        }

        return DataValidationResult.Success
    }
}