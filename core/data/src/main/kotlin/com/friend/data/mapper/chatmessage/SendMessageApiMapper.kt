package com.friend.data.mapper.chatmessage

import com.friend.apiresponse.chatmessage.SendMessageApiResponse
import com.friend.common.extfun.tryParseInt
import com.friend.data.mapper.Mapper
import com.friend.di.qualifier.AppImageBaseUrl
import com.friend.entity.chatmessage.ConversationEntity
import javax.inject.Inject

class SendMessageApiMapper @Inject constructor() :
    Mapper<SendMessageApiResponse, ConversationEntity> {

    @Inject
    @AppImageBaseUrl
    lateinit var imageBaseUrl: String

    override fun mapFromApiResponse(response: SendMessageApiResponse): ConversationEntity {
        val conversation = response.data
        return ConversationEntity(
            messageId = conversation?.id.orEmpty(),
            fromUsername = conversation?.fromUsername.orEmpty(),
            body = conversation?.body.orEmpty(),
            imageUrl = if (conversation?.imageURL != null && conversation.imageURL?.isNotEmpty() == true) "$imageBaseUrl${conversation.imageURL.orEmpty()}" else "",
            audioUrl = if (conversation?.audioURL != null && conversation.audioURL?.isNotEmpty() == true) "$imageBaseUrl${conversation.audioURL.orEmpty()}" else "",
            audioDuration = conversation?.audioDuration.orEmpty().tryParseInt(),
            videoUrl = if (conversation?.videoURL != null && conversation.videoURL?.isNotEmpty() == true) "$imageBaseUrl${conversation.videoURL.orEmpty()}" else "",
            videoDuration = conversation?.videoDuration.orEmpty().tryParseInt(),
            dateTime = conversation?.sendTime.orEmpty(),
            readableDateTime = "",
            isMyMessage = true
        )
    }
}