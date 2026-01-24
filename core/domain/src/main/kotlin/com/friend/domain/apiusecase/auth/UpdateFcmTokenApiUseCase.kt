package com.friend.domain.apiusecase.auth

import com.friend.domain.base.ApiResult
import com.friend.domain.repository.remote.AuthRepository
import com.friend.domain.usecase.ApiUseCaseParams
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class UpdateFcmTokenApiUseCase @Inject constructor(
    private val repository: AuthRepository,
) : ApiUseCaseParams<String, String> {

    override suspend fun execute(params: String): Flow<ApiResult<String>> =
        repository.updateFcmToken(params)
}