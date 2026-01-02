package com.friend.data.mapper.chatmessage

import com.friend.apiresponse.chatmessage.MessageListApiResponse
import com.friend.common.extfun.tryParseInt
import com.friend.data.mapper.Mapper
import com.friend.entity.chatmessage.MessageEntity
import com.friend.entity.chatmessage.MessageListApiEntity
import com.friend.sharedpref.SharedPrefHelper
import com.friend.sharedpref.SpKey
import javax.inject.Inject

class MessageListApiMapper @Inject constructor(
    private val sharedPrefHelper: SharedPrefHelper
) :
    Mapper<MessageListApiResponse, MessageListApiEntity> {

    override fun mapFromApiResponse(response: MessageListApiResponse): MessageListApiEntity {
        return MessageListApiEntity(
            isBlocked = response.isBlocked ?: false,
            data = response.data.orEmpty().map { message ->
                MessageEntity(
                    messageId = message.id.orEmpty(),
                    fromUsername = message.fromUsername.orEmpty(),
                    body = message.body.orEmpty(),
                    imageUrl = message.imageURL.orEmpty(),
                    audioUrl = message.audioURL.orEmpty(),
                    audioDuration = message.audioDuration.orEmpty().tryParseInt(),
                    videoUrl = message.videoURL.orEmpty(),
                    videoDuration = message.videoDuration.orEmpty().tryParseInt(),
                    dateTime = message.sendTime.orEmpty(),
                    isMyMessage = message.fromUsername == sharedPrefHelper.getString(SpKey.userName)
                )
            }
        )
    }
}