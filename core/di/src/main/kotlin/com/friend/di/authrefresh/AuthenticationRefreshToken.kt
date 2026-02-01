package com.friend.di.authrefresh

import com.friend.sharedpref.SharedPrefHelper
import com.friend.sharedpref.SpKey
import okhttp3.Authenticator
import okhttp3.Request
import okhttp3.Response
import okhttp3.Route
import javax.inject.Inject

class AuthenticationRefreshToken @Inject constructor(
    private var sharedPrefHelper: SharedPrefHelper,
    private var authRefreshServiceHolder: AuthRefreshServiceHolder,
) : Authenticator {
    override fun authenticate(route: Route?, response: Response): Request? {
        return if (response.code == 401) {
            val authenticatorCall = authRefreshServiceHolder.getAuthRefreshApi()?.refreshToken(
                RefreshTokenApiParams(
                    sharedPrefHelper.getString(SpKey.refreshToken)
                )
            )
            val refreshTokenResponse = authenticatorCall?.execute()
            if (refreshTokenResponse?.body() != null && refreshTokenResponse.isSuccessful && refreshTokenResponse.code() == 200) {
                refreshTokenResponse.body()?.let {
                    sharedPrefHelper.putString(SpKey.authToken, it.data?.authToken ?: "")
                    sharedPrefHelper.putString(SpKey.refreshToken, it.data?.refreshToken ?: "")
                    sharedPrefHelper.putString(SpKey.tokenExpireAt, it.data?.expireAt ?: "")
                    response.request.newBuilder().header("Authorization", it.data?.authToken ?: "").build()
                }
            } else null
        } else null
    }
}