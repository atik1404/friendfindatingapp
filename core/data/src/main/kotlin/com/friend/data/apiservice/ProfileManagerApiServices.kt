package com.friend.data.apiservice

import com.friend.apiresponse.profilemanager.OtherProfileApiResponse
import com.friend.apiresponse.profilemanager.ProfileApiResponse
import com.friend.apiresponse.search.CommonApiResponse
import com.friend.domain.apiusecase.profilemanager.PostAbuseReportApiUseCase
import com.friend.domain.apiusecase.profilemanager.PostBlockUnblockApiUseCase
import com.friend.domain.apiusecase.profilemanager.PostPasswordChangeApiUseCase
import com.friend.domain.apiusecase.profilemanager.PostProfileImageApiUseCase
import com.friend.domain.apiusecase.profilemanager.PostProfileUpdateApiUseCase
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part
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

    @POST("v1/SendAbuseReport")
    suspend fun performReportAbuse(
        @Body params: PostAbuseReportApiUseCase.Params
    ): Response<CommonApiResponse>

    @POST("v1/BlockUnblockUser")
    suspend fun performBlockUnblock(
        @Body params: PostBlockUnblockApiUseCase.Params
    ): Response<CommonApiResponse>

    @Multipart
    @POST("v1/AddPhoto")
    suspend fun performProfileImageUpdate(
        @Part("Username") username: RequestBody,
        @Part("PhotoAlbumID") photoAlbum: RequestBody,
        @Part("Name") name: RequestBody,
        @Part("Description") description: RequestBody,
        @Part("Approved") approve: RequestBody,
        @Part("ApprovedDate") approveDate: RequestBody,
        @Part image: MultipartBody.Part?,
    ): Response<CommonApiResponse>
}