package com.friend.data.apiservice

import com.friend.apiresponse.profilemanager.ProfileApiResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface ProfileManagerApiServices {
    @GET("v1/FetchProfile")
    suspend fun fetchProfile(
        @Query("username") username: String,
    ): Response<ProfileApiResponse>
}