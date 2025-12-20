package com.friend.data.mapper.profilemanager

import com.friend.apiresponse.profilemanager.OtherProfileApiResponse
import com.friend.common.constant.Gender
import com.friend.data.mapper.Mapper
import com.friend.di.qualifier.AppImageBaseUrl
import com.friend.entity.profilemanager.OtherProfileApiEntity
import com.friend.entity.profilemanager.ProfileApiEntity
import javax.inject.Inject

class OtherProfileApiMapper @Inject constructor() :
    Mapper<OtherProfileApiResponse, OtherProfileApiEntity> {
    @Inject
    @AppImageBaseUrl
    lateinit var imageBaseUrl: String

    override fun mapFromApiResponse(response: OtherProfileApiResponse): OtherProfileApiEntity {
        val profileData = response.data?.firstOrNull()

        return OtherProfileApiEntity(
            isBlocked = response.isBlocked ?: false,
            profile = ProfileApiEntity(
                userName = profileData?.username.orEmpty(),
                fullName = profileData?.name.orEmpty(),
                email = profileData?.email.orEmpty(),
                gender = Gender.fromValue(profileData?.gender ?: -1).name,
                birthdate = profileData?.birthDate.orEmpty(),
                interestedIn = Gender.fromValue(profileData?.interestedIn ?: -1).name,
                country = profileData?.country.orEmpty().ifEmpty { "N/A" },
                state = profileData?.state.orEmpty().ifEmpty { "N/A" },
                city = profileData?.city.orEmpty().ifEmpty { "N/A" },
                zipCode = profileData?.zipCode.orEmpty(),
                profilePicture = if (profileData?.image != null) "${imageBaseUrl}${profileData.image.orEmpty()}" else "",
                bodyType = profileData?.body_type.orEmpty(),
                drinking = profileData?.drinking.orEmpty(),
                eyes = profileData?.eyes.orEmpty(),
                hair = profileData?.hair.orEmpty(),
                height = profileData?.height.orEmpty(),
                interests = profileData?.interests.orEmpty(),
                lookingFor = profileData?.looking_for.orEmpty(),
                smoking = profileData?.smoking.orEmpty(),
                aboutYou = profileData?.tell_us_about_you.orEmpty(),
                title = profileData?.title.orEmpty(),
                weight = profileData?.weight.orEmpty(),
                whatsUp = profileData?.what_are_you_looking_for.orEmpty(),
                isProfileComplete = true,
            )
        )
    }
}