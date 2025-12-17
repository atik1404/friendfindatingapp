package com.friend.entity.profilemanager

data class ProfileApiEntity(
    val message: String,
    val userName: String,
    val fullName: String,
    val email: String,
    val gender: String,
    val dateOfBirth: String,
)