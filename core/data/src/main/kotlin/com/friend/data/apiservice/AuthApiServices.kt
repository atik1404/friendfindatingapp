package com.friend.data.apiservice

import com.friend.apiresponse.credential.ForgotPasswordApiResponse
import com.friend.apiresponse.credential.LoginApiResponse
import com.friend.apiresponse.credential.LogoutApiResponse
import com.friend.apiresponse.credential.RegistrationApiResponse
import com.friend.apiresponse.search.CommonApiResponse
import com.friend.domain.apiusecase.auth.PostLoginApiUseCase
import com.friend.domain.apiusecase.auth.PostLogoutApiUseCase
import com.friend.domain.apiusecase.auth.PostRegistrationApiUseCase
import com.friend.domain.apiusecase.auth.PostPasswordChangeApiUseCase
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST
import retrofit2.http.Query

interface AuthApiServices {
    @POST("api/Auth/v1/Login")
    suspend fun performLogin(
        @Body params: PostLoginApiUseCase.Params
    ): Response<LoginApiResponse>

    @POST("api/Auth/v1/LoginWithGoogle")
    suspend fun performGoogleLogin(
        @Query("email") email: String
    ): Response<LoginApiResponse>

    @POST("api/Auth/v1/Register")
    suspend fun performRegistration(
        @Body params: PostRegistrationApiUseCase.Params
    ): Response<RegistrationApiResponse>

    @POST("api/Auth/v1/ForgotPassword")
    suspend fun performForgotPassword(
        @Query("email") email: String
    ): Response<ForgotPasswordApiResponse>

    @POST("api/Auth/v1/PasswordChange")
    suspend fun performPasswordChanged(
        @Body params: PostPasswordChangeApiUseCase.Params
    ): Response<CommonApiResponse>

    @POST("api/Auth/v1/Logout")
    suspend fun performLogout(
        @Body params: PostLogoutApiUseCase.Params
    ): Response<LogoutApiResponse>

    @POST("v1/UserDelete")
    suspend fun performUserDelete(
        @Query("deleteReason") params: String
    ): Response<CommonApiResponse>

    @POST("v1/UpdateNotificationToken")
    suspend fun updateFcmToken(
        @Query("token") token: String
    ): Response<CommonApiResponse>
}