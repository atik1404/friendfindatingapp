package com.friend.domain.repository.remote

import com.friend.domain.apiusecase.chatmessage.SearchConversationApiUseCase
import com.friend.domain.apiusecase.chatmessage.ForwardMessageApiUseCase
import com.friend.domain.base.ApiResult
import com.friend.entity.chatmessage.ChatItemApiEntity
import com.friend.entity.chatmessage.ConversationApiEntity
import kotlinx.coroutines.flow.Flow

interface ChatMessagesRepository {
    suspend fun fetchChatList(pageNo: Int): Flow<ApiResult<List<ChatItemApiEntity>>>
    suspend fun fetchConversations(toUsername: String): Flow<ApiResult<ConversationApiEntity>>
    suspend fun searchConversation(params: SearchConversationApiUseCase.Params): Flow<ApiResult<ConversationApiEntity>>
    suspend fun forwardMessages(params: ForwardMessageApiUseCase.Params): Flow<ApiResult<String>>
    suspend fun deleteMessages(params: List<String>): Flow<ApiResult<String>>
}