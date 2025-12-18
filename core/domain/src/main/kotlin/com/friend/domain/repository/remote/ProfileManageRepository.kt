package com.friend.domain.repository.remote

import com.friend.domain.apiusecase.profilemanager.PostProfileUpdateApiUseCase
import com.friend.domain.base.ApiResult
import com.friend.entity.profilemanager.ProfileApiEntity
import kotlinx.coroutines.flow.Flow

interface ProfileManageRepository {
    suspend fun fetchProfile(): Flow<ApiResult<ProfileApiEntity>>

    suspend fun performProfileUpdate(params: PostProfileUpdateApiUseCase.Params): Flow<ApiResult<String>>
}