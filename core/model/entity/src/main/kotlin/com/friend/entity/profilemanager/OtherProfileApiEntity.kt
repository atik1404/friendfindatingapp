package com.friend.entity.profilemanager

data class OtherProfileApiEntity(
    val isBlocked: Boolean,
    val isPrivateProfile: Boolean,
    val profile: ProfileApiEntity
)
