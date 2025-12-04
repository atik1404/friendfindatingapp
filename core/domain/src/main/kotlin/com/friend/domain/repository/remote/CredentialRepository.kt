package com.friend.domain.repository.remote

import com.friend.domain.apiusecase.credential.PostLoginApiUseCase
import com.friend.domain.base.ApiResult
import com.friend.entity.credential.LoginApiEntity
import kotlinx.coroutines.flow.Flow

interface CredentialRepository {
    suspend fun performLogin(params: PostLoginApiUseCase.Params): Flow<ApiResult<LoginApiEntity>>
    suspend fun postForgotPassword(params: String): Flow<ApiResult<String>>

    suspend fun performLogout(): Flow<ApiResult<String>>
}