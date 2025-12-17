package com.friend.entity.credential

data class LoginApiEntity(
    val message: String,
    val accessToken: String,
    val refreshToken: String,
    val expireAt: String,
)