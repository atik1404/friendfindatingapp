package com.friend.data.apiservice

import com.friend.apiresponse.credential.LoginApiResponse
import com.friend.domain.apiusecase.credential.PostLoginApiUseCase
import retrofit2.Response
import retrofit2.http.POST

interface CredentialApiServices {
    @POST("v1/Login")
    suspend fun login(
        params: PostLoginApiUseCase.Params
    ): Response<LoginApiResponse>

    suspend fun registration()
    suspend fun fetchProfile()
    suspend fun updateProfile()
    suspend fun forgotPassword()
    suspend fun resetPassword()
}