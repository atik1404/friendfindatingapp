package com.friend.data.mapper.chatmessage

import com.friend.apiresponse.chatmessage.ConversationApiResponse
import com.friend.common.dateparser.DateTimeParser
import com.friend.common.dateparser.DateTimePatterns
import com.friend.common.extfun.tryParseInt
import com.friend.data.mapper.Mapper
import com.friend.di.qualifier.AppImageBaseUrl
import com.friend.entity.chatmessage.MessageEntity
import com.friend.entity.chatmessage.ConversationApiEntity
import com.friend.sharedpref.SharedPrefHelper
import com.friend.sharedpref.SpKey
import javax.inject.Inject

class ConversationsApiMapper @Inject constructor(
    private val sharedPrefHelper: SharedPrefHelper
) : Mapper<ConversationApiResponse, ConversationApiEntity> {

    @Inject
    @AppImageBaseUrl
    lateinit var imageBaseUrl: String

    override fun mapFromApiResponse(response: ConversationApiResponse): ConversationApiEntity {
        var lastDate: String? = null

        return ConversationApiEntity(
            isBlocked = response.isBlocked ?: false,
            data = response.data.orEmpty().map { message ->
                val sendDateTimeStr = message.sendTime ?: ""
                val currentDateTime = DateTimeParser.nowLocalDateTime()

                val sendDateTime = DateTimeParser.parseToDateTime(sendDateTimeStr)
                val readableSendDateTime =
                    DateTimeParser.parseToPattern(sendDateTimeStr, DateTimePatterns.SQL_YMD)
                var effectiveDate = ""

                sendDateTime?.let { date ->
                    val dayDiff = DateTimeParser.calendarDayDifference(currentDateTime, date)
                    if (readableSendDateTime.isNotEmpty() && readableSendDateTime != lastDate) {
                        effectiveDate =
                            DateTimeParser.formatRelativeDateLabel(dayDiff, sendDateTimeStr)
                        lastDate = readableSendDateTime
                    } else effectiveDate = ""
                }

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
                    readableDateTime = effectiveDate,
                    isMyMessage = message.fromUsername == sharedPrefHelper.getString(SpKey.userName)
                )
            }
        )
    }
}