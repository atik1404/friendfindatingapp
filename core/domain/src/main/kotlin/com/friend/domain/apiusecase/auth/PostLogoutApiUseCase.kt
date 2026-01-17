package com.friend.domain.apiusecase.auth

import com.friend.domain.base.ApiResult
import com.friend.domain.repository.remote.AuthRepository
import com.friend.domain.usecase.ApiUseCaseNonParams
import com.friend.domain.usecase.ApiUseCaseParams
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class PostLogoutApiUseCase @Inject constructor(
    private val repository: AuthRepository,
) : ApiUseCaseParams<PostLogoutApiUseCase.Params, String> {
    data class Params(
        val refreshToken: String
    )
    override suspend fun execute(params: Params): Flow<ApiResult<String>> =
        repository.performLogout(params)
}