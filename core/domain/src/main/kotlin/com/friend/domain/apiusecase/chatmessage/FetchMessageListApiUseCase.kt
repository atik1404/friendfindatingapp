package com.friend.domain.apiusecase.chatmessage

import com.friend.domain.base.ApiResult
import com.friend.domain.repository.remote.ChatMessagesRepository
import com.friend.domain.usecase.ApiUseCaseParams
import com.friend.entity.chatmessage.MessageListApiEntity
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class FetchMessageListApiUseCase @Inject constructor(
    private val repository: ChatMessagesRepository,
) : ApiUseCaseParams<FetchMessageListApiUseCase.Params, MessageListApiEntity> {

    data class Params(
        val fromUsername: String,
        val toUsername: String,
    )

    override suspend fun execute(params: Params): Flow<ApiResult<MessageListApiEntity>> {
        return repository.fetchMessageList(params)
    }
}