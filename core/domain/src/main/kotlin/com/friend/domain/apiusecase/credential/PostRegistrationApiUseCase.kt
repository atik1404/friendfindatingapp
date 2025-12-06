package com.friend.domain.apiusecase.credential

import com.friend.domain.base.ApiResult
import com.friend.domain.repository.remote.CredentialRepository
import com.friend.domain.usecase.ApiUseCaseParams
import com.friend.entity.credential.UserApiEntity
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class PostRegistrationApiUseCase @Inject constructor(
    private val repository: CredentialRepository,
) : ApiUseCaseParams<PostRegistrationApiUseCase.Params, UserApiEntity> {

    data class Params(
        val username: String,
        val password: String,
        val email: String,
        val name: String,
        val gender: Int,
        val interestedIn: Int,
        val birthdate: String,
        val birthdate2: String,
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

    override suspend fun execute(params: Params): Flow<ApiResult<UserApiEntity>> =
        repository.performRegistration(params)
}