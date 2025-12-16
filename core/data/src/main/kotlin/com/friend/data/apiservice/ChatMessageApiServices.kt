package com.friend.data.apiservice

import com.friend.apiresponse.chatmessage.ChatListApiResponse
import retrofit2.Response
import retrofit2.http.POST
import retrofit2.http.Query

interface ChatMessageApiServices {
    @POST("v1/SendMessageToList")
    suspend fun fetchChatList(
        @Query("fromUsername") username: String,
        @Query("pageNo") page: Int,
    ): Response<ChatListApiResponse>
}