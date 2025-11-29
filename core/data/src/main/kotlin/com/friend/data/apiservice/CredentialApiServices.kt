package com.friend.data.apiservice

import com.friend.apiresponse.credential.ForgotPasswordApiResponse
import com.friend.apiresponse.credential.LoginApiResponse
import com.friend.domain.apiusecase.credential.PostLoginApiUseCase
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST
import retrofit2.http.Query

interface CredentialApiServices {
    @POST("v1/Login")
    suspend fun login(
        @Body params: PostLoginApiUseCase.Params
    ): Response<LoginApiResponse>

    suspend fun registration()
    suspend fun fetchProfile()
    suspend fun updateProfile()
    @POST("v1/ForgotPassword")
    suspend fun forgotPassword(
        @Query("email") email: String
    ) : Response<ForgotPasswordApiResponse>
    suspend fun resetPassword()
}