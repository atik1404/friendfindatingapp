package com.friend.domain.apiusecase.profilemanager

import com.friend.domain.base.ApiResult
import com.friend.domain.repository.remote.ProfileManageRepository
import com.friend.domain.usecase.ApiUseCaseParams
import com.friend.domain.validator.DataValidationResult
import com.friend.domain.validator.ProfileCompletionIoResult
import com.friend.domain.validator.RegistrationIoResult
import com.friend.domain.validator.isEmailValid
import com.friend.domain.validator.isNameValid
import com.friend.domain.validator.isPasswordValid
import com.friend.domain.validator.isUsernameValid
import com.google.gson.annotations.SerializedName
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emptyFlow
import javax.inject.Inject

class PostProfileUpdateApiUseCase @Inject constructor(
    private val repository: ProfileManageRepository,
) : ApiUseCaseParams<PostProfileUpdateApiUseCase.Params, String> {
    val ioError = Channel<ProfileCompletionIoResult>()

    data class Params(
        val username: String,
        val name: String,
        val gender: Int,
        val interestedIn: Int,
        val birthdate: String,
        val email: String,
        val country: String,
        val state: String,
        val city: String,
        val zipCode: String,
        val height: String,
        val weight: String,
        val eyes: String,
        val hair: String,
        val smoking: String,
        val drinking: String,
        @SerializedName("body_type")
        val bodyType: String,
        @SerializedName("looking_for")
        val lookingFor: String,
        @SerializedName("tell_us_about_you")
        val aboutYou: String,
        val title: String,
        @SerializedName("what_are_you_looking_for")
        val whatsUp: String,
        val interests: String,
    )

    override suspend fun execute(params: Params): Flow<ApiResult<String>> {
        return when (val validationResult = validation(params)) {
            is DataValidationResult.Failure<*> -> {
                ioError.send(validationResult.ioErrorResult as ProfileCompletionIoResult)
                emptyFlow()
            }

            DataValidationResult.Success -> repository.performProfileUpdate(params)
        }
    }

    private fun validation(params: Params): DataValidationResult {
        if (!params.username.isUsernameValid())
            return DataValidationResult.Failure(RegistrationIoResult.InvalidUsername)

        if (!params.name.isNameValid())
            return DataValidationResult.Failure(RegistrationIoResult.InvalidName)

        if (!params.email.isEmailValid())
            return DataValidationResult.Failure(RegistrationIoResult.InvalidEmail)

        if (params.gender == -1)
            return DataValidationResult.Failure(RegistrationIoResult.InvalidGender)

        if (params.interestedIn == -1)
            return DataValidationResult.Failure(RegistrationIoResult.InvalidInterested)

        if (params.birthdate.isEmpty())
            return DataValidationResult.Failure(RegistrationIoResult.InvalidBirthDate)

        if (params.country == "-1")
            return DataValidationResult.Failure(RegistrationIoResult.InvalidCountry)

        if (params.state == "-1")
            return DataValidationResult.Failure(RegistrationIoResult.InvalidState)

        if (params.city == "-1")
            return DataValidationResult.Failure(RegistrationIoResult.InvalidCity)

        if (params.zipCode.isEmpty())
            return DataValidationResult.Failure(RegistrationIoResult.InvalidPostCode)

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

        if (params.interests.isEmpty())
            return DataValidationResult.Failure(ProfileCompletionIoResult.InvalidInterestedIn)

        return DataValidationResult.Success
    }
}