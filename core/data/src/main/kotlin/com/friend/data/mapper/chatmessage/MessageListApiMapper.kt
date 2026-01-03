package com.friend.data.mapper.chatmessage

import com.friend.apiresponse.chatmessage.MessageListApiResponse
import com.friend.common.extfun.tryParseInt
import com.friend.data.mapper.Mapper
import com.friend.di.qualifier.AppImageBaseUrl
import com.friend.entity.chatmessage.MessageEntity
import com.friend.entity.chatmessage.MessageListApiEntity
import com.friend.sharedpref.SharedPrefHelper
import com.friend.sharedpref.SpKey
import javax.inject.Inject

class MessageListApiMapper @Inject constructor(
    private val sharedPrefHelper: SharedPrefHelper
) : Mapper<MessageListApiResponse, MessageListApiEntity> {

    @Inject
    @AppImageBaseUrl
    lateinit var imageBaseUrl: String

    override fun mapFromApiResponse(response: MessageListApiResponse): MessageListApiEntity {
        return MessageListApiEntity(
            isBlocked = response.isBlocked ?: false,
            data = response.data.orEmpty().map { message ->
                MessageEntity(
                    messageId = message.id.orEmpty(),
                    fromUsername = message.fromUsername.orEmpty(),
                    body = message.body.orEmpty(),
                    imageUrl = if (message.imageURL != null && message.imageURL?.isNotEmpty() == true) "$imageBaseUrl${message.imageURL.orEmpty()}" else "",
                    audioUrl = if (message.audioURL != null && message.audioURL?.isNotEmpty() == true) "$imageBaseUrl${message.audioURL.orEmpty()}" else "",
                    audioDuration = message.audioDuration.orEmpty().tryParseInt(),
                    videoUrl = if (message.videoURL != null && message.videoURL?.isNotEmpty() == true) "$imageBaseUrl${message.videoURL.orEmpty()}" else "",
                    videoDuration = message.videoDuration.orEmpty().tryParseInt(),
                    dateTime = message.sendTime.orEmpty(),
                    isMyMessage = message.fromUsername == sharedPrefHelper.getString(SpKey.userName)
                )
            }
        )
    }
}