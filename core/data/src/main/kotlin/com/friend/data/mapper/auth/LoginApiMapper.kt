package com.friend.data.mapper.auth

import com.friend.apiresponse.credential.LoginApiResponse
import com.friend.data.mapper.Mapper
import com.friend.entity.auth.LoginApiEntity
import javax.inject.Inject

class LoginApiMapper @Inject constructor() : Mapper<LoginApiResponse, LoginApiEntity> {
    override fun mapFromApiResponse(response: LoginApiResponse): LoginApiEntity {
        return LoginApiEntity(
            message = response.message ?: "",
            accessToken = response.data?.authToken ?: "",
            refreshToken = response.data?.refreshToken ?: "",
            expireAt = response.data?.expireAt ?: "",
            isUserExist = response.count == 1,
        )
    }
}