package com.friend.domain.repository.remote

import com.friend.domain.apiusecase.chatmessage.FetchChatListApiUseCase
import com.friend.domain.base.ApiResult
import com.friend.entity.chatmessage.ChatListItemApiEntity
import kotlinx.coroutines.flow.Flow

interface ChatMessagesRepository {
    suspend fun fetchChatList(params: FetchChatListApiUseCase.Params): Flow<ApiResult<List<ChatListItemApiEntity>>>
}