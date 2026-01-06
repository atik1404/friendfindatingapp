package com.friend.domain.apiusecase.chatmessage

import com.friend.domain.base.ApiResult
import com.friend.domain.repository.remote.ChatMessagesRepository
import com.friend.domain.usecase.ApiUseCaseParams
import com.friend.entity.chatmessage.MessageListApiEntity
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class FetchMessageSearchResultApiUseCase @Inject constructor(
    private val repository: ChatMessagesRepository,
) : ApiUseCaseParams<FetchMessageSearchResultApiUseCase.Params, MessageListApiEntity> {

    data class Params(
        val fromUsername: String,
        val toUsername: String,
        val searchValue: String
    )

    override suspend fun execute(params: Params): Flow<ApiResult<MessageListApiEntity>> {
        return repository.searchMessage(params)
    }
}