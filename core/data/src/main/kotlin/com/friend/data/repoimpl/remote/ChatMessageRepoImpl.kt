package com.friend.data.repoimpl.remote

import com.friend.data.NetworkBoundResource
import com.friend.data.apiservice.ChatMessageApiServices
import com.friend.data.mapper.chatmessage.ChatListApiMapper
import com.friend.data.mapper.mapFromApiResponse
import com.friend.domain.apiusecase.chatmessage.FetchChatListApiUseCase
import com.friend.domain.base.ApiResult
import com.friend.domain.repository.remote.ChatMessagesRepository
import com.friend.entity.chatmessage.ChatListItemApiEntity
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class ChatMessageRepoImpl @Inject constructor(
    private val networkBoundResources: NetworkBoundResource,
    private val apiServices: ChatMessageApiServices,
    private val chatListApiMapper: ChatListApiMapper
) : ChatMessagesRepository {
    override suspend fun fetchChatList(params: FetchChatListApiUseCase.Params): Flow<ApiResult<List<ChatListItemApiEntity>>> {
        return mapFromApiResponse(
            result = networkBoundResources.downloadData {
                apiServices.fetchChatList(
                    username = params.fromUsername,
                    page = params.pageNo
                )
            }, mapper = chatListApiMapper
        )
    }
}