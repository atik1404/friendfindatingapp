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
            accessToken = response.data?.token ?: "",
            userName = response.data?.username ?: "",
            fullName = response.data?.name ?: "",
            email = response.data?.email ?: "",
            gender = if (response.data?.gender == 1) Gender.MALE.name else Gender.FEMALE.name,
            dateOfBirth = response.data?.birthdate ?: ""
        )
    }
}

class CacheProfile @Inject constructor(
    private val sharedPrefHelper: SharedPrefHelper,
) {
    fun cacheProfile(data: LoginApiEntity) {
        with(data) {
            sharedPrefHelper.putString(SpKey.authToken, data.accessToken)
            sharedPrefHelper.putString(SpKey.userName, data.userName)
            sharedPrefHelper.putString(SpKey.fullName, data.fullName)
            sharedPrefHelper.putString(SpKey.gender, data.gender)
            sharedPrefHelper.putString(SpKey.email, data.email)
            sharedPrefHelper.putString(SpKey.dateOfBirth, data.dateOfBirth)
        }
    }
}