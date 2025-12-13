package com.friend.domain.apiusecase.credential

import com.friend.domain.base.ApiResult
import com.friend.domain.repository.remote.CredentialRepository
import com.friend.domain.usecase.ApiUseCaseParams
import com.friend.domain.validator.DataValidationResult
import com.friend.domain.validator.ProfileCompletionIoResult
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emptyFlow
import javax.inject.Inject

class PostProfileCompletionApiUseCase @Inject constructor(
    private val repository: CredentialRepository,
) : ApiUseCaseParams<PostProfileCompletionApiUseCase.Params, String> {
    val ioError = Channel<ProfileCompletionIoResult>()

    data class Params(
        val height: String,
        val weight: String,
        val eyes: String,
        val hair: String,
        val smoking: String,
        val drinking: String,
        val bodyType: String,
        val lookingFor: String,
        val aboutYou: String,
        val title: String,
        val whatsUp: String,
        val interestedIn: List<String>,
    )

    override suspend fun execute(params: Params): Flow<ApiResult<String>> {
        return when (val validationResult = validation(params)) {
            is DataValidationResult.Failure<*> -> {
                ioError.send(validationResult.ioErrorResult as ProfileCompletionIoResult)
                emptyFlow()
            }

            DataValidationResult.Success -> repository.postProfileCompletion(params)
        }
    }

    private fun validation(params: Params): DataValidationResult {
        if (params.height.isEmpty())
            return DataValidationResult.Failure(ProfileCompletionIoResult.InvalidHeight)

        if (params.weight.isEmpty())
            return DataValidationResult.Failure(ProfileCompletionIoResult.InvalidWeight)

        if (params.eyes.isEmpty())
            return DataValidationResult.Failure(ProfileCompletionIoResult.InvalidEyes)

        if (params.hair.isEmpty())
            return DataValidationResult.Failure(ProfileCompletionIoResult.InvalidHair)

        if (params.smoking.isEmpty())
            return DataValidationResult.Failure(ProfileCompletionIoResult.InvalidSmoking)

        if (params.drinking.isEmpty())
            return DataValidationResult.Failure(ProfileCompletionIoResult.InvalidDrinking)

        if (params.bodyType.isEmpty())
            return DataValidationResult.Failure(ProfileCompletionIoResult.InvalidBodyType)

        if (params.lookingFor.isEmpty())
            return DataValidationResult.Failure(ProfileCompletionIoResult.InvalidLookingFor)

        if (params.title.isEmpty())
            return DataValidationResult.Failure(ProfileCompletionIoResult.InvalidTitle)

        if (params.aboutYou.isEmpty())
            return DataValidationResult.Failure(ProfileCompletionIoResult.InvalidAboutYou)

        if (params.whatsUp.isEmpty())
            return DataValidationResult.Failure(ProfileCompletionIoResult.InvalidWhatsUp)

        if (params.interestedIn.isEmpty())
            return DataValidationResult.Failure(ProfileCompletionIoResult.InvalidInterestedIn)

        return DataValidationResult.Success
    }
}