package com.friend.domain.apiusecase.chatmessage

import com.friend.domain.base.ApiResult
import com.friend.domain.repository.remote.ChatMessagesRepository
import com.friend.domain.usecase.ApiUseCaseParams
import com.friend.entity.chatmessage.ConversationApiEntity
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class FetchConversationsApiUseCase @Inject constructor(
    private val repository: ChatMessagesRepository,
) : ApiUseCaseParams<String, ConversationApiEntity> {

    override suspend fun execute(params: String): Flow<ApiResult<ConversationApiEntity>> =
        repository.fetchConversations(params)
}