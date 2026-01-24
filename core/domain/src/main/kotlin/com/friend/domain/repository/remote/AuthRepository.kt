package com.friend.domain.repository.remote

import com.friend.domain.apiusecase.auth.PostLoginApiUseCase
import com.friend.domain.apiusecase.auth.PostLogoutApiUseCase
import com.friend.domain.apiusecase.auth.PostRegistrationApiUseCase
import com.friend.domain.apiusecase.auth.PostPasswordChangeApiUseCase
import com.friend.domain.base.ApiResult
import com.friend.entity.auth.LoginApiEntity
import kotlinx.coroutines.flow.Flow

interface AuthRepository {
    suspend fun performLogin(params: PostLoginApiUseCase.Params): Flow<ApiResult<LoginApiEntity>>
    suspend fun performGoogleLogin(params: String): Flow<ApiResult<LoginApiEntity>>
    suspend fun performPasswordChange(params: PostPasswordChangeApiUseCase.Params): Flow<ApiResult<String>>
    suspend fun performRegistration(params: PostRegistrationApiUseCase.Params): Flow<ApiResult<String>>
    suspend fun performForgotPassword(params: String): Flow<ApiResult<String>>
    suspend fun performLogout(params: PostLogoutApiUseCase.Params): Flow<ApiResult<String>>
    suspend fun updateFcmToken(token: String): Flow<ApiResult<String>>
}