package com.friend.data.repoimpl.remote

import com.friend.data.NetworkBoundResource
import com.friend.data.apiservice.ChatMessageApiServices
import com.friend.data.mapper.chatmessage.ChatListApiMapper
import com.friend.data.mapper.chatmessage.ConversationsApiMapper
import com.friend.data.mapper.auth.CommonApiMapper
import com.friend.data.mapper.mapFromApiResponse
import com.friend.domain.apiusecase.chatmessage.SearchConversationApiUseCase
import com.friend.domain.apiusecase.chatmessage.ForwardMessageApiUseCase
import com.friend.domain.base.ApiResult
import com.friend.domain.repository.remote.ChatMessagesRepository
import com.friend.entity.chatmessage.ChatListItemApiEntity
import com.friend.entity.chatmessage.ConversationApiEntity
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class ChatMessageRepoImpl @Inject constructor(
    private val networkBoundResources: NetworkBoundResource,
    private val apiServices: ChatMessageApiServices,
    private val chatListApiMapper: ChatListApiMapper,
    private val conversationsApiMapper: ConversationsApiMapper,
    private val commonApiMapper: CommonApiMapper,
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

    override suspend fun fetchConversations(toUsername: String): Flow<ApiResult<ConversationApiEntity>> {
        return mapFromApiResponse(
            result = networkBoundResources.downloadData {
                apiServices.fetchMessageList(
                    toUsername = toUsername
                )
            }, mapper = conversationsApiMapper
        )
    }

    override suspend fun searchConversation(params: SearchConversationApiUseCase.Params): Flow<ApiResult<ConversationApiEntity>> {
        return mapFromApiResponse(
            result = networkBoundResources.downloadData {
                apiServices.searchMessage(
                    params = params
                )
            }, mapper = conversationsApiMapper
        )
    }

    override suspend fun forwardMessages(params: ForwardMessageApiUseCase.Params): Flow<ApiResult<String>> {
        return mapFromApiResponse(
            result = networkBoundResources.downloadData {
                apiServices.forwardMessages(
                    params = params
                )
            }, mapper = commonApiMapper
        )
    }

    override suspend fun deleteMessages(params: List<String>): Flow<ApiResult<String>> {
        return mapFromApiResponse(
            result = networkBoundResources.downloadData {
                apiServices.deleteMessages(
                    params = params
                )
            }, mapper = commonApiMapper
        )
    }
}