package com.friend.domain.apiusecase.auth

import com.friend.domain.base.ApiResult
import com.friend.domain.repository.remote.AuthRepository
import com.friend.domain.usecase.ApiUseCaseParams
import com.friend.entity.auth.LoginApiEntity
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class PostGoogleLoginApiUseCase @Inject constructor(
    private val repository: AuthRepository,
) : ApiUseCaseParams<String, LoginApiEntity> {

    override suspend fun execute(params: String): Flow<ApiResult<LoginApiEntity>> =
        repository.performGoogleLogin(params)
}