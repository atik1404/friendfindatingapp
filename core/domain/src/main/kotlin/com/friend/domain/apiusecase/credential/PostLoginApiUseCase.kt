package com.friend.domain.apiusecase.credential

import com.friend.domain.base.ApiResult
import com.friend.domain.repository.remote.CredentialRepository
import com.friend.domain.usecase.ApiUseCaseParams
import com.friend.domain.validator.DataValidationResult
import com.friend.domain.validator.LoginIoResult
import com.friend.domain.validator.isPasswordValid
import com.friend.domain.validator.isUsernameValid
import com.friend.entity.credential.LoginApiEntity
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emptyFlow
import javax.inject.Inject

class PostLoginApiUseCase @Inject constructor(
    private val repository: CredentialRepository,
) : ApiUseCaseParams<PostLoginApiUseCase.Params, LoginApiEntity> {
    val ioError = Channel<LoginIoResult>()

    data class Params(
        val username: String,
        val password: String,
    )

    override suspend fun execute(params: Params): Flow<ApiResult<LoginApiEntity>> {
        return when (val validationResult = validation(params)) {
            is DataValidationResult.Failure<*> -> {
                ioError.send(validationResult.ioErrorResult as LoginIoResult)
                emptyFlow()
            }

            DataValidationResult.Success -> repository.performLogin(params)
        }
    }
    private fun validation(params: Params): DataValidationResult {
        if (!params.username.isUsernameValid())
            return DataValidationResult.Failure(LoginIoResult.InvalidUsername)

        if (!params.password.isPasswordValid())
            return DataValidationResult.Failure(LoginIoResult.InvalidPassword)

        return DataValidationResult.Success
    }
}