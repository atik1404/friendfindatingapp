package com.friend.data.mapper.auth

import com.friend.apiresponse.credential.ForgotPasswordApiResponse
import com.friend.data.mapper.Mapper
import javax.inject.Inject

class ForgotPasswordApiMapper @Inject constructor() : Mapper<ForgotPasswordApiResponse, String> {
    override fun mapFromApiResponse(response: ForgotPasswordApiResponse): String {
        return response.message ?: ""
    }
}