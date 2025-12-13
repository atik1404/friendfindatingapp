package com.friend.data.repoimpl.remote

import com.friend.data.NetworkBoundResource
import com.friend.data.apiservice.CredentialApiServices
import com.friend.data.mapper.credential.CacheProfile
import com.friend.data.mapper.credential.ForgotPasswordApiMapper
import com.friend.data.mapper.credential.LoginApiMapper
import com.friend.data.mapper.credential.LogoutApiMapper
import com.friend.data.mapper.credential.CommonApiMapper
import com.friend.data.mapper.credential.RegistrationApiMapper
import com.friend.data.mapper.mapFromApiResponse
import com.friend.domain.apiusecase.credential.PostLoginApiUseCase
import com.friend.domain.apiusecase.credential.PostProfileCompletionApiUseCase
import com.friend.domain.apiusecase.credential.PostRegistrationApiUseCase
import com.friend.domain.base.ApiResult
import com.friend.domain.repository.remote.CredentialRepository
import com.friend.entity.credential.LoginApiEntity
import com.friend.entity.credential.UserApiEntity
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
    private val commonApiMapper: CommonApiMapper,
    private val cacheProfile: CacheProfile,
    private val registrationApiMapper: RegistrationApiMapper,
    private val sharedPrefHelper: SharedPrefHelper
) : CredentialRepository {
    override suspend fun performLogin(params: PostLoginApiUseCase.Params): Flow<ApiResult<LoginApiEntity>> {
        return mapFromApiResponse(
            result = networkBoundResources.downloadData {
                apiServices.login(
                    params
                )
            }, mapper = loginApiMapper
        ).map {
            if (it is ApiResult.Success && it.data.userName.isNotEmpty()) {
                sharedPrefHelper.putBool(SpKey.loginStatus, true)
                cacheProfile.cacheProfile(it.data)
            }
            it
        }
    }

    override suspend fun performRegistration(params: PostRegistrationApiUseCase.Params): Flow<ApiResult<UserApiEntity>> {
        return mapFromApiResponse(
            result = networkBoundResources.downloadData {
                apiServices.registration(
                    params
                )
            }, mapper = registrationApiMapper
        )
    }

    override suspend fun postForgotPassword(params: String): Flow<ApiResult<String>> {
        return mapFromApiResponse(
            result = networkBoundResources.downloadData {
                apiServices.forgotPassword(
                    params
                )
            }, mapper = forgotPasswordApiMapper
        )
    }

    override suspend fun postProfileCompletion(params: PostProfileCompletionApiUseCase.Params): Flow<ApiResult<String>> {
        return mapFromApiResponse(
            result = networkBoundResources.downloadData {
                apiServices.profileCompletion(
                    params
                )
            }, mapper = commonApiMapper
        )
    }

    override suspend fun performLogout(): Flow<ApiResult<String>> {
        return mapFromApiResponse(
            result = networkBoundResources.downloadData {
                apiServices.logout()
            }, mapper = logoutApiMapper
        )
    }
}