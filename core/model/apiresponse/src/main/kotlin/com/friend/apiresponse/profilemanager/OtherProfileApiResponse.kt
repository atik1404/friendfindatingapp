package com.friend.apiresponse.profilemanager

data class OtherProfileApiResponse(
    val status_code: Int?,
    val message: String?,
    val data: List<ProfileDetailsResponse>?,
    val isBlocked: Boolean?,
)