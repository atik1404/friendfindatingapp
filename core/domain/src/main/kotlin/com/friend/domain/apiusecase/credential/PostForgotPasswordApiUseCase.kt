package com.friend.domain.apiusecase.credential

import com.friend.domain.base.ApiResult
import com.friend.domain.repository.remote.CredentialRepository
import com.friend.domain.usecase.ApiUseCaseParams
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class PostForgotPasswordApiUseCase @Inject constructor(
    private val repository: CredentialRepository,
) : ApiUseCaseParams<String, String> {

    override suspend fun execute(params: String): Flow<ApiResult<String>> =
        repository.postForgotPassword(params)
}