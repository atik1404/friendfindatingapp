package com.friend.data.mapper.auth

import com.friend.apiresponse.credential.RegistrationApiResponse
import com.friend.data.mapper.Mapper
import javax.inject.Inject

class RegistrationApiMapper @Inject constructor() : Mapper<RegistrationApiResponse, String> {
    override fun mapFromApiResponse(response: RegistrationApiResponse): String {
        return response.message ?: ""
    }
}
