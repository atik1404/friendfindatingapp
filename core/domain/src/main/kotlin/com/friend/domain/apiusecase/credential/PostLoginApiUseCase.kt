package com.friend.domain.apiusecase.credential

import com.friend.domain.base.ApiResult
import com.friend.domain.repository.remote.CredentialRepository
import com.friend.domain.usecase.ApiUseCaseParams
import com.friend.entity.credential.LoginApiEntity
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class PostLoginApiUseCase @Inject constructor(
    private val repository: CredentialRepository,
) : ApiUseCaseParams<PostLoginApiUseCase.Params, LoginApiEntity> {
    data class Params(
        val username: String,
        val password: String,
    )

    override suspend fun execute(params: Params): Flow<ApiResult<LoginApiEntity>> = repository.performLogin(params)
}