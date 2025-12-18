package com.friend.data.apiservice

import com.friend.apiresponse.credential.ForgotPasswordApiResponse
import com.friend.apiresponse.credential.LoginApiResponse
import com.friend.apiresponse.credential.LogoutApiResponse
import com.friend.apiresponse.credential.RegistrationApiResponse
import com.friend.domain.apiusecase.credential.PostLoginApiUseCase
import com.friend.domain.apiusecase.credential.PostRegistrationApiUseCase
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST
import retrofit2.http.Query

interface CredentialApiServices {
    @POST("api/Auth/v1/Login")
    suspend fun performLogin(
        @Body params: PostLoginApiUseCase.Params
    ): Response<LoginApiResponse>

    @POST("api/Auth/v1/Register")
    suspend fun performRegistration(
        @Body params: PostRegistrationApiUseCase.Params
    ): Response<RegistrationApiResponse>

    @POST("v1/ForgotPassword")
    suspend fun performForgotPassword(
        @Query("email") email: String
    ): Response<ForgotPasswordApiResponse>

    @POST("v1/Logout")
    suspend fun performLogout(): Response<LogoutApiResponse>
}