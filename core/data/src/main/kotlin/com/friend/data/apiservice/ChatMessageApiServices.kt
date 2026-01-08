package com.friend.data.apiservice

import com.friend.apiresponse.chatmessage.ChatListApiResponse
import com.friend.apiresponse.chatmessage.MessageListApiResponse
import com.friend.apiresponse.search.CommonApiResponse
import com.friend.domain.apiusecase.chatmessage.DeleteMessagesApiUseCase
import com.friend.domain.apiusecase.chatmessage.FetchMessageListApiUseCase
import com.friend.domain.apiusecase.chatmessage.FetchMessageSearchResultApiUseCase
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

    @POST("v1/SendMessageHistory")
    suspend fun fetchMessageList(
        @Body params: FetchMessageListApiUseCase.Params
    ): Response<MessageListApiResponse>

    @POST("v1/SendMessageHistorySearch")
    suspend fun searchMessage(
        @Body params: FetchMessageSearchResultApiUseCase.Params
    ): Response<MessageListApiResponse>

    @POST("v1/ForwardMessage")
    suspend fun forwardMessages(
        @Body params: ForwardMessageApiUseCase.Params
    ): Response<CommonApiResponse>

    @POST("v1/SelectedMessageHistoryClean")
    suspend fun deleteMessages(
        @Body params: List<String>
    ): Response<CommonApiResponse>
}