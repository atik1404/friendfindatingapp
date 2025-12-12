package com.friend.domain.apiusecase.credential

import com.friend.domain.base.ApiResult
import com.friend.domain.repository.remote.CredentialRepository
import com.friend.domain.usecase.ApiUseCaseParams
import com.friend.domain.validator.DataValidationResult
import com.friend.domain.validator.RegistrationIoResult
import com.friend.domain.validator.isEmailValid
import com.friend.domain.validator.isNameValid
import com.friend.domain.validator.isPasswordValid
import com.friend.domain.validator.isUsernameValid
import com.friend.entity.credential.UserApiEntity
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emptyFlow
import timber.log.Timber
import javax.inject.Inject

class PostRegistrationApiUseCase @Inject constructor(
    private val repository: CredentialRepository,
) : ApiUseCaseParams<PostRegistrationApiUseCase.Params, UserApiEntity> {

    val ioError = Channel<RegistrationIoResult>()

    data class Params(
        val username: String,
        val password: String,
        val email: String,
        val name: String,
        val gender: Int = -1,
        val interestedIn: Int = -1,
        val birthdate: String,
        val birthdate2: String = birthdate,
        val country: String,
        val state: String,
        val zipCode: String,
        val city: String,
        val active: Int = 1,
        val receiveEmails: Int = 1,
        val userIP: String = "",
        val messageVerificationsLeft: Int = 5,
        val languageId: Int = 1,
        val billingDetails: String? = null,
        val invitedBy: String = "",
        val incomingMessagesRestrictions: String? = null,
        val affiliateID: Int = 0,
        val options: Int = 0,
        val longitude: Int = 0,
        val latitude: Int = 0,
        val tokenUniqueId: String = "",
        val credits: Int = 0,
        val moderationScore: Int = 0,
        val spamSuspected: Int = 0,
        val faceControlApproved: Int = 0,
        val profileSkin: String = "",
        val statusText: String = "",
        val featuredMember: Int = 0,
        val mySpaceID: String = "",
        val facebookID: Int = 0,
        val eventsSettings: Int = 0,
    )

    override suspend fun execute(params: Params): Flow<ApiResult<UserApiEntity>> {
        return when (val validationResult = validation(params)) {
            is DataValidationResult.Failure<*> -> {
                Timber.e("Validation failed")
                ioError.send(validationResult.ioErrorResult as RegistrationIoResult)
                emptyFlow()
            }

            DataValidationResult.Success -> {
                Timber.e("Validation success")
                repository.performRegistration(params)
            }
        }
    }

    private fun validation(params: Params): DataValidationResult {
        if (!params.username.isUsernameValid())
            return DataValidationResult.Failure(RegistrationIoResult.InvalidUsername)

        if (!params.name.isNameValid())
            return DataValidationResult.Failure(RegistrationIoResult.InvalidName)

        if (!params.email.isEmailValid())
            return DataValidationResult.Failure(RegistrationIoResult.InvalidEmail)

        if (!params.password.isPasswordValid())
            return DataValidationResult.Failure(RegistrationIoResult.InvalidPassword)

        if (params.gender == -1)
            return DataValidationResult.Failure(RegistrationIoResult.InvalidGender)

        if (params.interestedIn == -1)
            return DataValidationResult.Failure(RegistrationIoResult.InvalidInterested)

        if (params.birthdate.isEmpty())
            return DataValidationResult.Failure(RegistrationIoResult.InvalidBirthDate)

        if (params.country.isEmpty())
            return DataValidationResult.Failure(RegistrationIoResult.InvalidCountry)

        if (params.state.isEmpty())
            return DataValidationResult.Failure(RegistrationIoResult.InvalidState)

        if (params.city.isEmpty())
            return DataValidationResult.Failure(RegistrationIoResult.InvalidCity)

        if (params.zipCode.isEmpty())
            return DataValidationResult.Failure(RegistrationIoResult.InvalidPostCode)

        return DataValidationResult.Success
    }
}