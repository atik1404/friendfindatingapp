package com.friend.data.mapper.credential

import com.friend.apiresponse.credential.RegistrationApiResponse
import com.friend.common.constant.Gender
import com.friend.data.mapper.Mapper
import com.friend.entity.credential.UserApiEntity
import javax.inject.Inject

class RegistrationApiMapper @Inject constructor() : Mapper<RegistrationApiResponse, UserApiEntity> {
    override fun mapFromApiResponse(response: RegistrationApiResponse): UserApiEntity {
        val genderInt = response.data?.gender
        val genderString = if (genderInt == 1) {
            Gender.MALE.name
        } else {
            Gender.FEMALE.name
        }

        return UserApiEntity(
            message = response.message ?: "",
            userName = response.data?.username ?: "",
            fullName = response.data?.name ?: "",
            email = response.data?.email ?: "",
            gender = genderString,
            dateOfBirth = response.data?.birthdate ?: "",
        )
    }
}
