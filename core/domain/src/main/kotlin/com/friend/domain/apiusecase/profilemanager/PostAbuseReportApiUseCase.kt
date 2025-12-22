package com.friend.domain.apiusecase.profilemanager

import com.friend.domain.base.ApiResult
import com.friend.domain.repository.remote.CredentialRepository
import com.friend.domain.usecase.ApiUseCaseParams
import com.friend.domain.validator.DataValidationResult
import com.friend.domain.validator.ReportAbuseIoResult
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emptyFlow
import javax.inject.Inject

class PostAbuseReportApiUseCase @Inject constructor(
    private val repository: CredentialRepository,
) : ApiUseCaseParams<PostAbuseReportApiUseCase.Params, String> {
    val ioError = Channel<ReportAbuseIoResult>()

    data class Params(
        val reportedBy: String,
        val reportedUser: String,
        val reportNote: String,
    )

    override suspend fun execute(params: Params): Flow<ApiResult<String>> {
        return when (val validationResult = validation(params)) {
            is DataValidationResult.Failure<*> -> {
                ioError.send(validationResult.ioErrorResult as ReportAbuseIoResult)
                emptyFlow()
            }

            DataValidationResult.Success -> repository.performAbuseReport(params)
        }
    }

    private fun validation(params: Params): DataValidationResult {
        if (params.reportNote.isEmpty())
            return DataValidationResult.Failure(ReportAbuseIoResult.InvalidNote)


        return DataValidationResult.Success
    }
}