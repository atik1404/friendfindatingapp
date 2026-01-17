package com.friend.domain.apiusecase.chatmessage

import com.friend.domain.base.ApiResult
import com.friend.domain.repository.remote.ChatMessagesRepository
import com.friend.domain.usecase.ApiUseCaseParams
import com.friend.entity.chatmessage.ConversationApiEntity
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class SearchConversationApiUseCase @Inject constructor(
    private val repository: ChatMessagesRepository,
) : ApiUseCaseParams<SearchConversationApiUseCase.Params, ConversationApiEntity> {

    data class Params(
        val toUsername: String,
        val searchValue: String
    )

    override suspend fun execute(params: Params): Flow<ApiResult<ConversationApiEntity>> {
        return repository.searchConversation(params)
    }
}