package com.friend.domain.repository.remote

import com.friend.domain.apiusecase.credential.PostLoginApiUseCase
import com.friend.domain.apiusecase.credential.PostRegistrationApiUseCase
import com.friend.domain.base.ApiResult
import com.friend.entity.credential.LoginApiEntity
import kotlinx.coroutines.flow.Flow

interface CredentialRepository {
    suspend fun performLogin(params: PostLoginApiUseCase.Params): Flow<ApiResult<LoginApiEntity>>
    suspend fun performRegistration(params: PostRegistrationApiUseCase.Params): Flow<ApiResult<String>>
    suspend fun performForgotPassword(params: String): Flow<ApiResult<String>>
    suspend fun performLogout(): Flow<ApiResult<String>>
}