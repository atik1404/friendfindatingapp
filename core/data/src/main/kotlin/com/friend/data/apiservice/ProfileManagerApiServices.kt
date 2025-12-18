package com.friend.data.apiservice

import com.friend.apiresponse.profilemanager.ProfileApiResponse
import retrofit2.Response
import retrofit2.http.GET

interface ProfileManagerApiServices {
    @GET("api/Profile/v1/GetProfileInformation")
    suspend fun fetchProfile(
    ): Response<ProfileApiResponse>
}