package com.friend.apiresponse.chatmessage

data class ChatListApiResponse(
    val statusCode: Int?,
    val message: String?,
    val data: List<ChatListItemResponse>?,
    val count: Int?
)

data class ChatListItemResponse(
    val toUsername: String?,
    val notificationToken: String?,
    val userImage: String?,
    val fullName: String?,
    val lastMessage: String?,
    val lastMessageDateTime: String?,
)