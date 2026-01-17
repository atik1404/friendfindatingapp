package com.friend.data.apiservice

import com.friend.apiresponse.chatmessage.ChatListApiResponse
import com.friend.apiresponse.chatmessage.ConversationApiResponse
import com.friend.apiresponse.search.CommonApiResponse
import com.friend.domain.apiusecase.chatmessage.SearchConversationApiUseCase
import com.friend.domain.apiusecase.chatmessage.ForwardMessageApiUseCase
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

interface ChatMessageApiServices {
    @GET("v1/Chats")
    suspend fun fetchChatList(
        @Query("pageNo") page: Int,
    ): Response<ChatListApiResponse>

    @GET("v1/ChatHistory")
    suspend fun fetchMessageList(
        @Query("toUsername") toUsername: String
    ): Response<ConversationApiResponse>

    @POST("v1/ChatHistorySearch")
    suspend fun searchMessage(
        @Body params: SearchConversationApiUseCase.Params
    ): Response<ConversationApiResponse>

    @POST("v1/MessageForward")
    suspend fun forwardMessages(
        @Body params: ForwardMessageApiUseCase.Params
    ): Response<CommonApiResponse>

    @POST("v1/SelectedMessageClean")
    suspend fun deleteMessages(
        @Body params: List<String>
    ): Response<CommonApiResponse>
}