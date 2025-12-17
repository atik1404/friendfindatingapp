package com.friend.domain.repository.remote

import com.friend.domain.base.ApiResult
import com.friend.entity.profilemanager.ProfileApiEntity
import kotlinx.coroutines.flow.Flow

interface ProfileManageRepository {
    suspend fun fetchProfile(userName: String): Flow<ApiResult<ProfileApiEntity>>
}