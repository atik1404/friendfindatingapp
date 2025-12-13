package com.friend.data.mapper.credential

import com.friend.apiresponse.search.CommonApiResponse
import com.friend.data.mapper.Mapper
import javax.inject.Inject

class CommonApiMapper @Inject constructor() : Mapper<CommonApiResponse, String> {
    override fun mapFromApiResponse(response: CommonApiResponse): String {
        return response.message ?: ""
    }
}