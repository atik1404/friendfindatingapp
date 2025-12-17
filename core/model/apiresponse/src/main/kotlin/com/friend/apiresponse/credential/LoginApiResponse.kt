package com.friend.apiresponse.credential

data class LoginApiResponse(
    val status_code: Int?,
    val message: String?,
    val data: LoginResponse?,
    val count: Int?
)

data class LoginResponse(
    val authToken: String?,
    val expireAt: String?,
    val refreshToken: String?
)