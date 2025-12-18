package com.friend.data.apiservice

import com.friend.apiresponse.profilemanager.ProfileApiResponse
import com.friend.apiresponse.search.CommonApiResponse
import com.friend.domain.apiusecase.profilemanager.PostProfileUpdateApiUseCase
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface ProfileManagerApiServices {
    @GET("api/Profile/v1/GetProfileInformation")
    suspend fun fetchProfile(
    ): Response<ProfileApiResponse>

    @POST("api/Profile/v1/UpdateProfileInformation")
    suspend fun performProfileUpdate(
        @Body params: PostProfileUpdateApiUseCase.Params
    ): Response<CommonApiResponse>
}