package com.friend.data.apiservice

import com.friend.apiresponse.chatmessage.ChatListApiResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

interface ChatMessageApiServices {
    @GET("v1/Chats")
    suspend fun fetchChatList(
        @Query("pageNo") page: Int,
    ): Response<ChatListApiResponse>
}