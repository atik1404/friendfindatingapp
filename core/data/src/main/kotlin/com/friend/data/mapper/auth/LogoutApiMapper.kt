package com.friend.data.mapper.auth

import com.friend.apiresponse.credential.LogoutApiResponse
import com.friend.data.mapper.Mapper
import javax.inject.Inject

class LogoutApiMapper @Inject constructor() : Mapper<LogoutApiResponse, String> {
    override fun mapFromApiResponse(response: LogoutApiResponse): String {
        return response.message ?: ""
    }
}