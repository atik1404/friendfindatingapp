package com.friend.di.authrefresh

import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST


data class RefreshTokenApiParams(val refreshToken: String)

data class RefreshTokenApiResponse(val data: RefreshTokenResponse?)
data class RefreshTokenResponse(
    val authToken: String?,
    val expireAt: String?,
    val refreshToken: String?,
)

interface AuthRefreshApiService {
    @POST("api/Auth/v1/RefreshToken")
    fun refreshToken(@Body params: RefreshTokenApiParams): Call<RefreshTokenApiResponse>
}

class AuthRefreshServiceHolder {
    private var authRefreshApi: AuthRefreshApiService? = null
    fun getAuthRefreshApi(): AuthRefreshApiService? {
        return authRefreshApi
    }

    fun setAuthRefreshApi(authRefreshApi: AuthRefreshApiService) {
        this.authRefreshApi = authRefreshApi
    }
}
