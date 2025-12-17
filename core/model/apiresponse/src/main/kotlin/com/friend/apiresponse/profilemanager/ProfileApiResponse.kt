package com.friend.apiresponse.profilemanager


data class ProfileApiResponse(
    val status_code: Int?,
    val message: String?,
    //val data: ProfileDetailsResponse?,
    val data: List<ProfileDetails>?,
)

data class ProfileDetails(
    val questionID: Int?,
    val value: String?,
    val approved: Int?,
)

data class ProfileDetailsResponse(
    val token: String?,
    val message: String?,
    val username: String?,
    val email: String?,
    val name: String?,
    val gender: Int?,
    val active: Int?,
    val receiveEmails: Int?,
    val interestedIn: Int?,
    val birthdate: String?,
    val country: String?,
    val state: String?,
    val zipCode: String?,
    val city: String?,
    val userIP: String?,
    val messageVerificationsLeft: Int?,
    val languageId: Int?,
    val affiliateID: Int?,
    val options: Int?,
    val longitude: Int?,
    val latitude: Int?,
    val tokenUniqueId: String?,
    val credits: Int?,
    val moderationScore: Int?,
    val spamSuspected: Int?,
    val faceControlApproved: Int?,
    val profileSkin: String?,
    val featuredMember: Int?,
    val eventsSettings: Int?
)