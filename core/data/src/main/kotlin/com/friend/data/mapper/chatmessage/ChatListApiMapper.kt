package com.friend.data.mapper.chatmessage

import com.friend.apiresponse.chatmessage.ChatListApiResponse
import com.friend.data.mapper.Mapper
import com.friend.di.qualifier.AppImageBaseUrl
import com.friend.entity.chatmessage.ChatListItemApiEntity
import javax.inject.Inject

class ChatListApiMapper @Inject constructor() :
    Mapper<ChatListApiResponse, List<ChatListItemApiEntity>> {
    @Inject
    @AppImageBaseUrl
    lateinit var imageBaseUrl: String
    override fun mapFromApiResponse(response: ChatListApiResponse): List<ChatListItemApiEntity> {
        return response.data?.map { item ->
            ChatListItemApiEntity(
                toUsername = item.toUsername.orEmpty(),
                notificationToken = item.notificationToken.orEmpty(),
                userImage = if (item.userImage != null) "$imageBaseUrl${item.userImage.orEmpty()}" else "",
                fullName = item.fullName ?: "",
                lastMessage = item.lastMessage ?: "",
                dateTime = item.lastMessageDateTime ?: ""
            )
        }?.toList() ?: emptyList()
    }
}