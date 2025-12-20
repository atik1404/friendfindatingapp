package com.friend.data.apiservice

import com.friend.apiresponse.profilemanager.OtherProfileApiResponse
import com.friend.apiresponse.profilemanager.ProfileApiResponse
import com.friend.apiresponse.search.CommonApiResponse
import com.friend.domain.apiusecase.profilemanager.PostPasswordChangeApiUseCase
import com.friend.domain.apiusecase.profilemanager.PostProfileUpdateApiUseCase
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

interface ProfileManagerApiServices {
    @GET("api/Profile/v1/GetProfileInformation")
    suspend fun fetchProfile(
    ): Response<ProfileApiResponse>

    @GET("api/Profile/v1/GetOtherProfileInformation")
    suspend fun fetchOtherProfile(
        @Query("otherUsername") username: String
    ): Response<OtherProfileApiResponse>

    @POST("api/Profile/v1/UpdateProfileInformation")
    suspend fun performProfileUpdate(
        @Body params: PostProfileUpdateApiUseCase.Params
    ): Response<CommonApiResponse>

    @POST("v1/PasswordChange")
    suspend fun performPasswordChanged(
        @Body params: PostPasswordChangeApiUseCase.Params
    ): Response<CommonApiResponse>
}