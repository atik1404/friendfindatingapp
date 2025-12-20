package com.friend.domain.apiusecase.profilemanager

import com.friend.domain.base.ApiResult
import com.friend.domain.repository.remote.ProfileManageRepository
import com.friend.domain.usecase.ApiUseCaseParams
import com.friend.domain.validator.DataValidationResult
import com.friend.domain.validator.LoginIoResult
import com.friend.domain.validator.PasswordChangeIoResult
import com.friend.domain.validator.isPasswordMatched
import com.friend.domain.validator.isPasswordValid
import com.friend.domain.validator.isUsernameValid
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emptyFlow
import javax.inject.Inject

class PostPasswordChangeApiUseCase @Inject constructor(
    private val repository: ProfileManageRepository,
) : ApiUseCaseParams<PostPasswordChangeApiUseCase.Params, String> {

    val ioError = Channel<PasswordChangeIoResult>()

    data class Params(
        val username: String,
        val oldPassword: String,
        val newPassword: String,
        val confirmPassword: String,
    )

    override suspend fun execute(params: Params): Flow<ApiResult<String>> {
        return when (val validationResult = validation(params)) {
            is DataValidationResult.Failure<*> -> {
                ioError.send(validationResult.ioErrorResult as PasswordChangeIoResult)
                emptyFlow()
            }

            DataValidationResult.Success -> repository.performPasswordChange(params)
        }
    }

    private fun validation(params: Params): DataValidationResult {
        if (!params.oldPassword.isPasswordValid())
            return DataValidationResult.Failure(PasswordChangeIoResult.InvalidOldPassword)

        if (!params.newPassword.isPasswordValid())
            return DataValidationResult.Failure(PasswordChangeIoResult.InvalidNewPassword)

        if (!params.confirmPassword.isPasswordValid())
            return DataValidationResult.Failure(PasswordChangeIoResult.InvalidConfirmPassword)

        if (!params.newPassword.isPasswordMatched(params.confirmPassword))
            return DataValidationResult.Failure(PasswordChangeIoResult.PasswordNotMatched)

        return DataValidationResult.Success
    }
}
