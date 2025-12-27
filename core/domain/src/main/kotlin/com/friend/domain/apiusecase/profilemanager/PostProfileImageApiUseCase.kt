package com.friend.domain.apiusecase.profilemanager

import com.friend.domain.base.ApiResult
import com.friend.domain.repository.remote.ProfileManageRepository
import com.friend.domain.usecase.ApiUseCaseParams
import kotlinx.coroutines.flow.Flow
import java.io.File
import javax.inject.Inject

class PostProfileImageApiUseCase @Inject constructor(
    private val repository: ProfileManageRepository,
) : ApiUseCaseParams<PostProfileImageApiUseCase.Params, String> {
    data class Params(
        val username: String,
        val name: String,
        val image: File?,
    )

    override suspend fun execute(params: Params): Flow<ApiResult<String>> =
        repository.performProfileImageUpdate(params)
}