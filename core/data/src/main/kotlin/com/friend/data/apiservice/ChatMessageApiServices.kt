package com.friend.data.apiservice

import com.friend.apiresponse.chatmessage.ChatListApiResponse
import com.friend.apiresponse.chatmessage.ConversationApiResponse
import com.friend.apiresponse.chatmessage.SendMessageApiResponse
import com.friend.apiresponse.search.CommonApiResponse
import com.friend.domain.apiusecase.chatmessage.SearchConversationApiUseCase
import com.friend.domain.apiusecase.chatmessage.ForwardMessageApiUseCase
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part
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

    @Multipart
    @POST("v1/MessageSend")
    suspend fun sendMessage(
        @Part("ToUsername") toUsername: RequestBody,
        @Part("Body") body: RequestBody,
        @Part("DeviceToken") deviceToken: RequestBody,
        @Part("AudioDuration") audioDuration: RequestBody,
        @Part("VideoDuration") videoDuration: RequestBody,
        @Part image: MultipartBody.Part?,
        @Part audio: MultipartBody.Part?,
        @Part video: MultipartBody.Part?,
    ): Response<SendMessageApiResponse>
}