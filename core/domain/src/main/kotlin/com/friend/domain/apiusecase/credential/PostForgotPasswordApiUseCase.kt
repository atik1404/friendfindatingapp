package com.friend.domain.apiusecase.credential

import com.friend.domain.base.ApiResult
import com.friend.domain.repository.remote.CredentialRepository
import com.friend.domain.usecase.ApiUseCaseParams
import com.friend.domain.validator.DataValidationResult
import com.friend.domain.validator.ForgotPasswordIoResult
import com.friend.domain.validator.isEmailValid
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emptyFlow
import javax.inject.Inject

class PostForgotPasswordApiUseCase @Inject constructor(
    private val repository: CredentialRepository,
) : ApiUseCaseParams<String, String> {

    val ioError = Channel<ForgotPasswordIoResult>()

    override suspend fun execute(params: String): Flow<ApiResult<String>> {
        return when (val validationResult = validation(params)) {
            is DataValidationResult.Failure<*> -> {
                ioError.send(validationResult.ioErrorResult as ForgotPasswordIoResult)
                emptyFlow()
            }

            DataValidationResult.Success -> repository.postForgotPassword(params)
        }
    }

    private fun validation(params: String): DataValidationResult {
        if (!params.isEmailValid())
            return DataValidationResult.Failure(ForgotPasswordIoResult.InvalidEmail)

        return DataValidationResult.Success
    }
}