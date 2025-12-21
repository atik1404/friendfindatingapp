package com.friend.domain.apiusecase.profilemanager

import com.friend.domain.base.ApiResult
import com.friend.domain.repository.remote.CredentialRepository
import com.friend.domain.usecase.ApiUseCaseParams
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class PostAbuseReportApiUseCase @Inject constructor(
    private val repository: CredentialRepository,
) : ApiUseCaseParams<PostAbuseReportApiUseCase.Params, String> {
    data class Params(
        val reportedBy: String,
        val reportedUser: String,
        val reportNote: String,
    )

    override suspend fun execute(params: Params): Flow<ApiResult<String>> =
        repository.performAbuseReport(params)
}