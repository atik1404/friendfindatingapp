package com.friend.domain.apiusecase.profilemanager

import com.friend.domain.repository.remote.ProfileManageRepository
import com.friend.domain.usecase.ApiUseCaseNonParams
import javax.inject.Inject

class UpdateOnlineStatusApiUseCase @Inject constructor(
    private val repository: ProfileManageRepository,
) : ApiUseCaseNonParams<String> {
    override suspend fun execute() = repository.updateOnlineStatus()
}