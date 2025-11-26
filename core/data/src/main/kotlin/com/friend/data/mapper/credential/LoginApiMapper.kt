package com.friend.data.mapper.credential

import com.friend.apiresponse.credential.LoginApiResponse
import com.friend.data.mapper.Mapper
import com.friend.entity.credential.LoginApiEntity
import javax.inject.Inject

class LoginApiMapper @Inject constructor() : Mapper<LoginApiResponse, LoginApiEntity> {

    override fun mapFromApiResponse(type: LoginApiResponse): LoginApiEntity {
        return LoginApiEntity(
            accessToken = type.data?.token ?: "",
            userName = type.data?.username ?: ""
        )
    }
}