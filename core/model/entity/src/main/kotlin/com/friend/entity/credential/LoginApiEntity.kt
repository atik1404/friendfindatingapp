package com.friend.entity.credential

data class LoginApiEntity(
    val message: String,
    val accessToken: String,
    val userName: String,
    val fullName: String,
    val email: String,
    val gender: String,
    val dateOfBirth: String
)