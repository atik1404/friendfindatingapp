package com.friend.data.repoimpl.remote

import com.friend.data.NetworkBoundResource
import com.friend.data.apiservice.ChatMessageApiServices
import com.friend.data.mapper.chatmessage.ChatListApiMapper
import com.friend.data.mapper.chatmessage.MessageListApiMapper
import com.friend.data.mapper.mapFromApiResponse
import com.friend.domain.apiusecase.chatmessage.FetchMessageListApiUseCase
import com.friend.domain.apiusecase.chatmessage.FetchMessageSearchResultApiUseCase
import com.friend.domain.base.ApiResult
import com.friend.domain.repository.remote.ChatMessagesRepository
import com.friend.entity.chatmessage.ChatListItemApiEntity
import com.friend.entity.chatmessage.MessageListApiEntity
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class ChatMessageRepoImpl @Inject constructor(
    private val networkBoundResources: NetworkBoundResource,
    private val apiServices: ChatMessageApiServices,
    private val chatListApiMapper: ChatListApiMapper,
    private val messageListApiMapper: MessageListApiMapper,
) : ChatMessagesRepository {
    override suspend fun fetchChatList(pageNo: Int): Flow<ApiResult<List<ChatListItemApiEntity>>> {
        return mapFromApiResponse(
            result = networkBoundResources.downloadData {
                apiServices.fetchChatList(
                    page = pageNo
                )
            }, mapper = chatListApiMapper
        )
    }

    override suspend fun fetchMessageList(params: FetchMessageListApiUseCase.Params): Flow<ApiResult<MessageListApiEntity>> {
        return mapFromApiResponse(
            result = networkBoundResources.downloadData {
                apiServices.fetchMessageList(
                    params = params
                )
            }, mapper = messageListApiMapper
        )
    }

    override suspend fun searchMessage(params: FetchMessageSearchResultApiUseCase.Params): Flow<ApiResult<MessageListApiEntity>> {
        return mapFromApiResponse(
            result = networkBoundResources.downloadData {
                apiServices.searchMessage(
                    params = params
                )
            }, mapper = messageListApiMapper
        )
    }
}