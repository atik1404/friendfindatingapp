package com.friend.data.mapper.credential

import com.friend.apiresponse.credential.LoginApiResponse
import com.friend.common.constant.Gender
import com.friend.data.mapper.Mapper
import com.friend.entity.credential.LoginApiEntity
import com.friend.sharedpref.SharedPrefHelper
import com.friend.sharedpref.SpKey
import javax.inject.Inject

class LoginApiMapper @Inject constructor() : Mapper<LoginApiResponse, LoginApiEntity> {
    override fun mapFromApiResponse(response: LoginApiResponse): LoginApiEntity {
        return LoginApiEntity(
            message = response.message ?: "",
            accessToken = response.data?.authToken ?: "",
            refreshToken = response.data?.refreshToken ?: "",
            expireAt = response.data?.expireAt ?: "",
        )
    }
}