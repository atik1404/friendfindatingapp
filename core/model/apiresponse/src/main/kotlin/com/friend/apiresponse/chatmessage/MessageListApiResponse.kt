package com.friend.apiresponse.chatmessage

data class MessageListApiResponse(
    val status_code: Int?,
    val message: String?,
    val data: List<MessageResponse>?,
    val isBlocked: Boolean?,
    val count: Int?,
)

data class MessageResponse(
    val id: String?,
    val fromUsername: String?,
    val body: String?,
    val imageURL: String?,
    val audioURL: String?,
    val audioDuration: String?,
    val videoURL: String?,
    val videoDuration: String?,
    val sendTime: String?,
)