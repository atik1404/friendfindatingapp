package com.friend.entity.chatmessage

data class ConversationApiEntity(
    val isBlocked: Boolean,
    val data: List<ConversationEntity>
)

data class ConversationEntity(
    val messageId: String,
    val fromUsername: String,
    val body: String,
    val imageUrl: String,
    val audioUrl: String,
    val audioDuration: Int,
    val videoUrl: String,
    val videoDuration: Int,
    val dateTime: String,
    val readableDateTime: String,
    val isMyMessage: Boolean,
    var isItemSelected: Boolean = false
)
