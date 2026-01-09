package com.friend.domain.apiusecase.credential

import com.friend.domain.base.ApiResult
import com.friend.domain.repository.remote.CredentialRepository
import com.friend.domain.usecase.ApiUseCaseParams
import com.friend.entity.credential.LoginApiEntity
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class PostGoogleLoginApiUseCase @Inject constructor(
    private val repository: CredentialRepository,
) : ApiUseCaseParams<String, LoginApiEntity> {

    override suspend fun execute(params: String): Flow<ApiResult<LoginApiEntity>> =
        repository.performGoogleLogin(params)
}