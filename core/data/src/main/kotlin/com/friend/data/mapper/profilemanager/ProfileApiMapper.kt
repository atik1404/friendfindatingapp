package com.friend.data.mapper.profilemanager

import com.friend.apiresponse.profilemanager.ProfileApiResponse
import com.friend.common.constant.Gender
import com.friend.data.mapper.Mapper
import com.friend.di.qualifier.AppImageBaseUrl
import com.friend.entity.profilemanager.ProfileApiEntity
import com.friend.sharedpref.SharedPrefHelper
import com.friend.sharedpref.SpKey
import javax.inject.Inject

class ProfileApiMapper @Inject constructor() :
    Mapper<ProfileApiResponse, ProfileApiEntity> {


    @Inject
    @AppImageBaseUrl
    lateinit var imageBaseUrl: String

    override fun mapFromApiResponse(response: ProfileApiResponse): ProfileApiEntity {
        val profileData = response.data?.firstOrNull()

        return ProfileApiEntity(
            userName = profileData?.username.orEmpty(),
            fullName = profileData?.name.orEmpty(),
            email = profileData?.email.orEmpty(),
            gender = Gender.fromValue(profileData?.gender ?: -1).name,
            birthdate = profileData?.birthdate.orEmpty(),
            interestedIn = Gender.fromValue(profileData?.interestedIn ?: -1).name,
            country = profileData?.country.orEmpty(),
            state = profileData?.state.orEmpty(),
            city = profileData?.city.orEmpty(),
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
            isProfileComplete = (profileData != null && profileData.height != null),
            //isProfileComplete = profileData?.is_profile_complete ?: false
        )
    }
}

class CacheProfile @Inject constructor(
    private val sharedPrefHelper: SharedPrefHelper,
) {
    fun cacheProfile(data: ProfileApiEntity) {
        with(data) {
            sharedPrefHelper.putString(SpKey.userName, userName)
            sharedPrefHelper.putString(SpKey.fullName, fullName)
            sharedPrefHelper.putString(SpKey.gender, gender)
            sharedPrefHelper.putString(SpKey.email, email)
            sharedPrefHelper.putString(SpKey.dateOfBirth, birthdate)
            sharedPrefHelper.putString(SpKey.interestedIn, interestedIn)
            sharedPrefHelper.putString(SpKey.country, country)
            sharedPrefHelper.putString(SpKey.state, state)
            sharedPrefHelper.putString(SpKey.city, city)
            sharedPrefHelper.putString(SpKey.zipCode, zipCode)
            sharedPrefHelper.putString(SpKey.profilePicture, profilePicture)
            sharedPrefHelper.putString(SpKey.bodyType, bodyType)
            sharedPrefHelper.putString(SpKey.drinking, drinking)
            sharedPrefHelper.putString(SpKey.eyes, eyes)
            sharedPrefHelper.putString(SpKey.hair, hair)
            sharedPrefHelper.putString(SpKey.height, height)
            sharedPrefHelper.putString(SpKey.interests, interests)
            sharedPrefHelper.putString(SpKey.lookingFor, lookingFor)
            sharedPrefHelper.putString(SpKey.smoking, smoking)
            sharedPrefHelper.putString(SpKey.aboutYou, aboutYou)
            sharedPrefHelper.putString(SpKey.title, title)
            sharedPrefHelper.putString(SpKey.weight, weight)
            sharedPrefHelper.putString(SpKey.whatsUp, whatsUp)
        }
    }
}