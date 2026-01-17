package com.friend.domain.apiusecase.auth

import com.friend.domain.base.ApiResult
import com.friend.domain.repository.remote.AuthRepository
import com.friend.domain.usecase.ApiUseCaseNonParams
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class PostLogoutApiUseCase @Inject constructor(
    private val repository: AuthRepository,
) : ApiUseCaseNonParams<String> {
    override suspend fun execute(): Flow<ApiResult<String>> = repository.performLogout()
}