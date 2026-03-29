package com.friend.data.mapper.chatmessage

import com.friend.apiresponse.chatmessage.ChatListApiResponse
import com.friend.common.dateparser.DateTimeParser
import com.friend.common.dateparser.DateTimePatterns
import com.friend.data.mapper.Mapper
import com.friend.di.qualifier.AppFileBaseUrl
import com.friend.entity.chatmessage.ChatItemApiEntity
import javax.inject.Inject

class ChatListApiMapper @Inject constructor() :
    Mapper<ChatListApiResponse, List<ChatItemApiEntity>> {
    @Inject
    @AppFileBaseUrl
    lateinit var imageBaseUrl: String
    override fun mapFromApiResponse(response: ChatListApiResponse): List<ChatItemApiEntity> {
        return response.data?.map { item ->
            val sendDateTimeStr = item.lastMessageDateTime ?: ""
            val currentDateTime = DateTimeParser.nowLocalDateTime()

            val sendDateTime = DateTimeParser.parseToLocalDateTime(sendDateTimeStr)
            var effectiveDate = ""
            val effectiveTime = DateTimeParser.parseToPattern(sendDateTimeStr, DateTimePatterns.TIME_12_HM_AMPM)

            sendDateTime?.let { date ->
                val dayDiff = DateTimeParser.calendarDayDifference(currentDateTime, date)
                effectiveDate = DateTimeParser.formatRelativeDateLabel(dayDiff, sendDateTimeStr)
            }

            ChatItemApiEntity(
                toUsername = item.toUsername.orEmpty(),
                notificationToken = item.notificationToken.orEmpty(),
                userImage = if (item.userImage != null) "$imageBaseUrl${item.userImage.orEmpty()}" else "",
                fullName = item.fullName ?: "",
                lastMessage = item.lastMessage ?: "",
                dateTime = item.lastMessageDateTime ?: "",
                readableDateTime = "$effectiveDate $effectiveTime",
            )
        }?.toList() ?: emptyList()
    }
}