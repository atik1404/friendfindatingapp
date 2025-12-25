package com.friend.domain.apiusecase.profilemanager

import com.friend.domain.base.ApiResult
import com.friend.domain.repository.remote.CredentialRepository
import com.friend.domain.usecase.ApiUseCaseParams
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class PostProfileImageApiUseCase @Inject constructor(
    private val repository: CredentialRepository,
) : ApiUseCaseParams<PostProfileImageApiUseCase.Params, String> {
    data class Params(
        val userBlocker: String,
        val blockedUser: String,
    )

    override suspend fun execute(params: Params): Flow<ApiResult<String>> =
        repository.performProfileImageUpdate(params)
}