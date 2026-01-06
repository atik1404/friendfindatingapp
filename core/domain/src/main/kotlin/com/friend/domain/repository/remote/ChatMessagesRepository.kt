package com.friend.domain.repository.remote

import com.friend.domain.apiusecase.chatmessage.FetchMessageListApiUseCase
import com.friend.domain.apiusecase.chatmessage.FetchMessageSearchResultApiUseCase
import com.friend.domain.base.ApiResult
import com.friend.entity.chatmessage.ChatListItemApiEntity
import com.friend.entity.chatmessage.MessageListApiEntity
import kotlinx.coroutines.flow.Flow

interface ChatMessagesRepository {
    suspend fun fetchChatList(pageNo: Int): Flow<ApiResult<List<ChatListItemApiEntity>>>

    suspend fun fetchMessageList(params: FetchMessageListApiUseCase.Params): Flow<ApiResult<MessageListApiEntity>>

    suspend fun searchMessage(params: FetchMessageSearchResultApiUseCase.Params): Flow<ApiResult<MessageListApiEntity>>
}