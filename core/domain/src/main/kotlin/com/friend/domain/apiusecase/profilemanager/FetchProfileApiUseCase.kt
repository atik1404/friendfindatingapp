package com.friend.domain.apiusecase.profilemanager

import com.friend.domain.base.ApiResult
import com.friend.domain.repository.remote.ProfileManageRepository
import com.friend.domain.usecase.ApiUseCaseNonParams
import com.friend.domain.usecase.ApiUseCaseParams
import com.friend.entity.profilemanager.ProfileApiEntity
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class FetchProfileApiUseCase @Inject constructor(
    private val repository: ProfileManageRepository,
) : ApiUseCaseNonParams<ProfileApiEntity> {

    override suspend fun execute(): Flow<ApiResult<ProfileApiEntity>> =
        repository.fetchProfile()
}