package com.friend.data.mapper.profilemanager

import com.friend.apiresponse.profilemanager.ProfileApiResponse
import com.friend.common.constant.Gender
import com.friend.data.mapper.Mapper
import com.friend.entity.profilemanager.ProfileApiEntity
import com.friend.sharedpref.SharedPrefHelper
import com.friend.sharedpref.SpKey
import javax.inject.Inject

class ProfileApiMapper @Inject constructor() :
    Mapper<ProfileApiResponse, ProfileApiEntity> {

    override fun mapFromApiResponse(response: ProfileApiResponse): ProfileApiEntity {
        return ProfileApiEntity(
            message = response.message.orEmpty(),
            userName = "",//response.data?.username.orEmpty(),
            fullName = "Atik Faysal",//response.data?.name.orEmpty(),
            email = "atik@gmail.com",//response.data?.email.orEmpty(),
            gender = Gender.fromValue(1).name,//Gender.fromValue(response.data?.gender ?: -1).name,
            dateOfBirth = "1995-01-01"//response.data?.birthdate.orEmpty(),
        )
    }
}

class CacheProfile @Inject constructor(
    private val sharedPrefHelper: SharedPrefHelper,
) {
    fun cacheProfile(data: ProfileApiEntity) {
        with(data) {
            sharedPrefHelper.putString(
                SpKey.userName,
                data.userName.ifEmpty { "atik121" })//TODO remove this static name
            sharedPrefHelper.putString(SpKey.fullName, data.fullName)
            sharedPrefHelper.putString(SpKey.gender, data.gender)
            sharedPrefHelper.putString(SpKey.email, data.email)
            sharedPrefHelper.putString(SpKey.dateOfBirth, data.dateOfBirth)
        }
    }
}