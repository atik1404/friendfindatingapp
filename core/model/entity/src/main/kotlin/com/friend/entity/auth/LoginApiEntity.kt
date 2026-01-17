package com.friend.entity.auth

data class LoginApiEntity(
    val message: String,
    val accessToken: String,
    val refreshToken: String,
    val expireAt: String,
    val isUserExist: Boolean
)