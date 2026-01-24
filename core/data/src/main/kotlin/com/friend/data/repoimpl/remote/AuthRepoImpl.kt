package com.friend.data.repoimpl.remote

import com.friend.data.NetworkBoundResource
import com.friend.data.apiservice.AuthApiServices
import com.friend.data.mapper.auth.CommonApiMapper
import com.friend.data.mapper.auth.ForgotPasswordApiMapper
import com.friend.data.mapper.auth.LoginApiMapper
import com.friend.data.mapper.auth.LogoutApiMapper
import com.friend.data.mapper.auth.RegistrationApiMapper
import com.friend.data.mapper.mapFromApiResponse
import com.friend.domain.apiusecase.auth.PostLoginApiUseCase
import com.friend.domain.apiusecase.auth.PostLogoutApiUseCase
import com.friend.domain.apiusecase.auth.PostRegistrationApiUseCase
import com.friend.domain.apiusecase.auth.PostPasswordChangeApiUseCase
import com.friend.domain.base.ApiResult
import com.friend.domain.repository.remote.AuthRepository
import com.friend.entity.auth.LoginApiEntity
import com.friend.sharedpref.SharedPrefHelper
import com.friend.sharedpref.SpKey
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class AuthRepoImpl @Inject constructor(
    private val networkBoundResources: NetworkBoundResource,
    private val apiServices: AuthApiServices,
    private val loginApiMapper: LoginApiMapper,
    private val forgotPasswordApiMapper: ForgotPasswordApiMapper,
    private val logoutApiMapper: LogoutApiMapper,
    private val registrationApiMapper: RegistrationApiMapper,
    private val commonApiMapper: CommonApiMapper,
    private val sharedPrefHelper: SharedPrefHelper
) : AuthRepository {
    override suspend fun performLogin(params: PostLoginApiUseCase.Params): Flow<ApiResult<LoginApiEntity>> {
        return mapFromApiResponse(
            result = networkBoundResources.downloadData {
                apiServices.performLogin(
                    params
                )
            }, mapper = loginApiMapper
        ).map {
            if (it is ApiResult.Success) {
                sharedPrefHelper.putBool(SpKey.loginStatus, true)
                sharedPrefHelper.putString(SpKey.authToken, it.data.accessToken)
                sharedPrefHelper.putString(SpKey.refreshToken, it.data.refreshToken)
                sharedPrefHelper.putString(SpKey.tokenExpireAt, it.data.expireAt)
            }
            it
        }
    }

    override suspend fun performGoogleLogin(params: String): Flow<ApiResult<LoginApiEntity>> {
        return mapFromApiResponse(
            result = networkBoundResources.downloadData {
                apiServices.performGoogleLogin(
                    params
                )
            }, mapper = loginApiMapper
        ).map {
            if (it is ApiResult.Success) {
                sharedPrefHelper.putBool(SpKey.loginStatus, true)
                sharedPrefHelper.putBool(SpKey.isLoginByGoogle, true)
                sharedPrefHelper.putString(SpKey.authToken, it.data.accessToken)
                sharedPrefHelper.putString(SpKey.refreshToken, it.data.refreshToken)
                sharedPrefHelper.putString(SpKey.tokenExpireAt, it.data.expireAt)
            }
            it
        }
    }

    override suspend fun performRegistration(params: PostRegistrationApiUseCase.Params): Flow<ApiResult<String>> {
        return mapFromApiResponse(
            result = networkBoundResources.downloadData {
                apiServices.performRegistration(
                    params
                )
            }, mapper = registrationApiMapper
        )
    }

    override suspend fun performForgotPassword(params: String): Flow<ApiResult<String>> {
        return mapFromApiResponse(
            result = networkBoundResources.downloadData {
                apiServices.performForgotPassword(
                    params
                )
            }, mapper = forgotPasswordApiMapper
        )
    }

    override suspend fun performPasswordChange(params: PostPasswordChangeApiUseCase.Params): Flow<ApiResult<String>> {
        return mapFromApiResponse(
            result = networkBoundResources.downloadData {
                apiServices.performPasswordChanged(
                    params
                )
            }, mapper = commonApiMapper
        )
    }

    override suspend fun performLogout(params: PostLogoutApiUseCase.Params): Flow<ApiResult<String>> {
        return mapFromApiResponse(
            result = networkBoundResources.downloadData {
                apiServices.performLogout(params)
            }, mapper = logoutApiMapper
        )
    }

    override suspend fun updateFcmToken(token: String): Flow<ApiResult<String>> {
        return mapFromApiResponse(
            result = networkBoundResources.downloadData {
                apiServices.updateFcmToken(token)
            }, mapper = commonApiMapper
        )
    }
}