package com.friend.entity.chatmessage

import java.io.Serializable

data class ChatListItemApiEntity(
    val toUsername: String,
    val notificationToken: String,
    val userImage: String,
    val fullName: String,
    val lastMessage: String,
    val dateTime: String,
)