package com.friend.domain.apiusecase.profilemanager

import com.friend.domain.base.ApiResult
import com.friend.domain.repository.remote.ProfileManageRepository
import com.friend.domain.usecase.ApiUseCaseParams
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class PostBlockUnblockApiUseCase @Inject constructor(
    private val repository: ProfileManageRepository,
) : ApiUseCaseParams<String, String> {

    override suspend fun execute(params: String): Flow<ApiResult<String>> =
        repository.performBlockUnblock(params)
}