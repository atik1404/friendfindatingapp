package com.friend.data.apiservice

import com.friend.apiresponse.chatmessage.ChatListApiResponse
import com.friend.apiresponse.chatmessage.MessageListApiResponse
import com.friend.domain.apiusecase.chatmessage.FetchMessageListApiUseCase
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
}