package com.friend.apiresponse.profilemanager


data class ProfileApiResponse(
    val status_code: Int?,
    val message: String?,
    val data: List<ProfileDetailsResponse>?,
    val blockedUser: List<BlockedUser>?,
)

data class ProfileDetailsResponse(
    val username: String?,
    val name: String?,
    val gender: Int?,
    val birthDate: String?,
    val email: String?,
    val interestedIn: Int?,
    val country: String?,
    val state: String?,
    val city: String?,
    val zipCode: String?,
    val image: String?,
    val body_type: String?,
    val drinking: String?,
    val eyes: String?,
    val hair: String?,
    val height: String?,
    val interests: String?,
    val looking_for: String?,
    val smoking: String?,
    val tell_us_about_you: String?,
    val title: String?,
    val weight: String?,
    val what_are_you_looking_for: String?,
    val is_profile_complete: Int?,
)

data class BlockedUser(
    val userBlocker: String?,
    val blockedUser: String?
)