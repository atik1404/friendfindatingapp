package com.friend.data.repoimpl.remote

import com.friend.data.NetworkBoundResource
import com.friend.data.apiservice.CredentialApiServices
import com.friend.data.mapper.credential.ForgotPasswordApiMapper
import com.friend.data.mapper.credential.LoginApiMapper
import com.friend.data.mapper.credential.LogoutApiMapper
import com.friend.data.mapper.credential.RegistrationApiMapper
import com.friend.data.mapper.mapFromApiResponse
import com.friend.domain.apiusecase.credential.PostLoginApiUseCase
import com.friend.domain.apiusecase.credential.PostRegistrationApiUseCase
import com.friend.domain.base.ApiResult
import com.friend.domain.repository.remote.CredentialRepository
import com.friend.entity.credential.LoginApiEntity
import com.friend.sharedpref.SharedPrefHelper
import com.friend.sharedpref.SpKey
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class CredentialRepoImpl @Inject constructor(
    private val networkBoundResources: NetworkBoundResource,
    private val apiServices: CredentialApiServices,
    private val loginApiMapper: LoginApiMapper,
    private val forgotPasswordApiMapper: ForgotPasswordApiMapper,
    private val logoutApiMapper: LogoutApiMapper,
    private val registrationApiMapper: RegistrationApiMapper,
    private val sharedPrefHelper: SharedPrefHelper
) : CredentialRepository {
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

    override suspend fun performLogout(): Flow<ApiResult<String>> {
        return mapFromApiResponse(
            result = networkBoundResources.downloadData {
                apiServices.performLogout()
            }, mapper = logoutApiMapper
        )
    }
}