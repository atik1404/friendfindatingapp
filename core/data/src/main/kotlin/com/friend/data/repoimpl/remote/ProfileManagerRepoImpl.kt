package com.friend.data.repoimpl.remote

import com.friend.data.NetworkBoundResource
import com.friend.data.apiservice.ProfileManagerApiServices
import com.friend.data.mapper.mapFromApiResponse
import com.friend.data.mapper.profilemanager.CacheProfile
import com.friend.data.mapper.profilemanager.ProfileApiMapper
import com.friend.domain.base.ApiResult
import com.friend.domain.repository.remote.ProfileManageRepository
import com.friend.entity.profilemanager.ProfileApiEntity
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class ProfileManagerRepoImpl @Inject constructor(
    private val networkBoundResources: NetworkBoundResource,
    private val apiServices: ProfileManagerApiServices,
    private val cacheProfile: CacheProfile,
    private val profileApiMapper: ProfileApiMapper,
) : ProfileManageRepository {
    override suspend fun fetchProfile(): Flow<ApiResult<ProfileApiEntity>> {
        return mapFromApiResponse(
            result = networkBoundResources.downloadData {
                apiServices.fetchProfile()
            }, mapper = profileApiMapper
        ).map {
            if (it is ApiResult.Success) {
                cacheProfile.cacheProfile(it.data)
            }
            it
        }
    }
}